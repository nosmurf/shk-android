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

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";
    private static final String GROUP_ID = "";

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
    public Observable<String> uploadPhoto(String imagePath) {
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
        }).flatMap(new Func1<Uri, Observable<String>>() {
            @Override
            public Observable<String> call(Uri uri) {
                return Observable.create((Subscriber<? super String> subscriber) -> {
                    DatabaseReference usersReference = databaseReference.child("users/" + firebaseAuth.getCurrentUser().getUid());
                    usersReference.child("images").push().setValue(uri.toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            subscriber.onNext(uri.toString());
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
    public Observable<Boolean> hasCurrentUser() {
        return Observable.just(firebaseAuth.getCurrentUser() != null);
    }

    @Override
    public Observable<String> getGroupId() {
        return Observable.create(subscriber -> {
            databaseReference.child("groupId").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String groupId = dataSnapshot.getValue(String.class);
                    subscriber.onNext(groupId);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            });
        });
    }

    @Override
    public Observable<String> getPersonId() {
        return Observable.create(subscriber -> {
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("microsoftId")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subscriber.onNext(dataSnapshot.getValue(String.class));
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Observable<Boolean> hasMicrosoftId() {
        return Observable.create(subscriber -> {
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("microsoftId")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subscriber.onNext(dataSnapshot.getValue(String.class) != null);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Observable<Void> saveMicrosoftId(String microsoftId) {
        return Observable.create(subscriber -> {
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("microsoftId")
                    .setValue(microsoftId);
        });
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