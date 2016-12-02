package com.nosmurf.data.repository.firebase;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nosmurf.data.exception.UserNotFoundException;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";

    private static final String USERS_PATH = "users/";

    private static final String IMAGES = "IMAGES";

    private static final String ALGORITHM = "SHA-384";

    private static final String KEY = "key";

    private final StorageReference storageReference;

    private final FirebaseAuth firebaseAuth;

    private final DatabaseReference databaseReference;


    @Inject
    public SHKFirebaseDataSource() {
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public Observable<Void> uploadPhoto(String imagePath) {
        File image = new File(imagePath);
        Uri uri = Uri.fromFile(image);

        final StorageReference userPhotosRef = storageReference.child(firebaseAuth.getCurrentUser().getUid()).child(uri.getLastPathSegment());

        return Observable.create((Subscriber<? super Uri> subscriber) -> {
            userPhotosRef.putFile(uri)
                    .addOnSuccessListener(task -> {
                        subscriber.onNext(task.getDownloadUrl());
                        subscriber.onCompleted();
                    })
                    .addOnFailureListener(subscriber::onError);
        }).flatMap(new Func1<Uri, Observable<Void>>() {
            @Override
            public Observable<Void> call(Uri uri) {
                return Observable.create((Subscriber<? super Void> subscriber) -> {
                    DatabaseReference usersReference = databaseReference.child(USERS_PATH + firebaseAuth.getCurrentUser().getUid());
                    usersReference.child(IMAGES).push().setValue(uri.toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            subscriber.onCompleted();
                        }
                    }).addOnFailureListener(subscriber::onError);

                });
            }
        });
    }


    @Override
    public Observable<Void> doLogin(GoogleSignInAccount account) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(task.getException());
                            }
                        });
            }
        });
    }

    @Override
    public Observable<TokenHashed> getHashedToken() {
        return Observable.create((Subscriber<? super TokenHashed> subscriber) -> {
            String token = firebaseAuth.getCurrentUser().getUid();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
                for (byte b : token.getBytes()) {
                    messageDigest.update(b);
                }

                // FIXME: 01/12/2016 remove hardcoded numbers
                subscriber.onNext(new TokenHashed(16, Arrays.copyOfRange(messageDigest.digest(), 0, 16)));
                subscriber.onNext(new TokenHashed(17, Arrays.copyOfRange(messageDigest.digest(), 16, 32)));
                subscriber.onNext(new TokenHashed(18, Arrays.copyOfRange(messageDigest.digest(), 32, 48)));

                subscriber.onCompleted();

            } catch (NoSuchAlgorithmException exception) {
                subscriber.onError(exception);
            }

        }).delay(100, TimeUnit.MILLISECONDS);
    }

    @Override
    public Observable<Key> getKey() {
        return Observable.create(new Observable.OnSubscribe<Key>() {
            @Override
            public void call(Subscriber<? super Key> subscriber) {
                DatabaseReference usersReference = databaseReference.child(USERS_PATH + firebaseAuth.getCurrentUser().getUid());
                usersReference.child(KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object value = dataSnapshot.getValue();
                        if (value == null) {
                            usersReference.child(KEY).setValue(getRandomHexString());
                        } else {
                            subscriber.onNext(new Key(4, 19, (String) value));
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(new RuntimeException(databaseError.getMessage()));
                    }
                });

            }
        });
    }

    private String getRandomHexString() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int nBytes = 12;
        while (sb.length() < nBytes) {
            sb.append(Integer.toHexString(random.nextInt()));
        }

        return sb.toString().substring(0, nBytes);
    }

    public Observable<Boolean> hasCurrentUser() {
        return Observable.just(firebaseAuth.getCurrentUser() != null);
    }

    @Override
    public Observable<String> getCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return Observable.just(currentUser.getUid());
        } else {
            return Observable.error(new UserNotFoundException());
        }
    }
}