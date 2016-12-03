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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";
    private static final String GROUP_ID = "";

    private static final String GROUPS_PATH = "groups/";

    private static final String IMAGES = "IMAGES";

    private static final String ALGORITHM = "SHA-384";

    private static final String KEY = "key";

    private static final String USERS_PATH = "users/";

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
                    DatabaseReference groupsReference = databaseReference.child(GROUPS_PATH + firebaseAuth.getCurrentUser().getUid());
                    groupsReference.child(IMAGES).push().setValue(uri.toString()).addOnCompleteListener(task -> {
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
    public Observable<String> doLogin(GoogleSignInAccount account, String parentEmail) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                String parentUid = uid;
                                if (!parentEmail.equals("")) {
                                    DatabaseReference groupsReference = databaseReference.child(GROUPS_PATH);
                                    groupsReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Map<String, Object> groups = (HashMap<String, Object>) dataSnapshot.getValue();

                                            Iterator it = groups.entrySet().iterator();
                                            while (it.hasNext()) {
                                                Map.Entry pair = (Map.Entry) it.next();
                                                String key = (String) pair.getKey();
                                                Map<String, Object> value = (HashMap<String, Object>) pair.getValue();
                                                if (value.get("parentEmail").equals(parentEmail)) {
                                                    groupsReference.removeEventListener(this);
                                                    insertUser(account, key, uid, "user", subscriber);
                                                }
                                                it.remove(); // avoids a ConcurrentModificationException
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            subscriber.onError(databaseError.toException());
                                        }
                                    });
                                } else {
                                    insertUser(account, parentUid, uid, "admin", subscriber);
                                }
                            } else {
                                subscriber.onError(task.getException());
                            }
                        });
            }
        });
    }

    private void insertUser(GoogleSignInAccount account, String parentUid, String uid, String role, Subscriber<? super String> subscriber) {
        DatabaseReference groupsReference = databaseReference.child(GROUPS_PATH + parentUid);
        if (parentUid.equals(uid)) {
            groupsReference.child("parentEmail").setValue(account.getEmail());
            groupsReference.child(USERS_PATH).setValue(parentUid);
        }

        groupsReference.child(USERS_PATH).child(uid).child("role").setValue(role);
        groupsReference.child(USERS_PATH).child(uid).child("name").setValue(account.getDisplayName());
        groupsReference.child(USERS_PATH).child(uid).child("email").setValue(account.getEmail());
        subscriber.onNext(parentUid);
        subscriber.onCompleted();
    }

    @Override
    public Observable<Boolean> hasCurrentUser() {
        return Observable.just(firebaseAuth.getCurrentUser() != null);
    }

    @Override
    public Observable<String> getGroupId(String uid) {
        return Observable.create(subscriber -> {
            DatabaseReference groupReference = databaseReference.child(GROUPS_PATH + uid);
            groupReference.child("microsoftGroupId").addValueEventListener(new ValueEventListener() {
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
            DatabaseReference groupReference = databaseReference.child(GROUPS_PATH + firebaseAuth.getCurrentUser().getUid());
            groupReference.child(USERS_PATH + firebaseAuth.getCurrentUser().getUid()).child("microsoftId")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            subscriber.onNext(dataSnapshot.getValue(String.class) != null);
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
            DatabaseReference groupReference = databaseReference.child(GROUPS_PATH + firebaseAuth.getCurrentUser().getUid());
            DatabaseReference userReference = groupReference.child(USERS_PATH + firebaseAuth.getCurrentUser().getUid());
            userReference.child("microsoftId")
                    .setValue(microsoftId)
                    .addOnCompleteListener(task -> subscriber.onCompleted());
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
                String uid = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference groupsReference = databaseReference.child(GROUPS_PATH + uid);
                groupsReference.child(KEY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Object value = dataSnapshot.getValue();
                        if (value == null) {
                            groupsReference.child(KEY).setValue(getRandomHexString());
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

    @Override
    public Observable<String> saveMicrosoftGroupId() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference groupReference = databaseReference.child(GROUPS_PATH + uid);
        return Observable.create(subscriber -> {
            groupReference.child("microsoftGroupId").setValue(uid).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    subscriber.onNext(uid);
                    subscriber.onCompleted();
                }
            }).addOnFailureListener(subscriber::onError);
        });
    }

    @Override
    public Observable<Boolean> hasGroupOnMicrosoft(String uid) {
        return Observable.create(subscriber -> {
            DatabaseReference groupsReference = databaseReference.child(GROUPS_PATH + uid);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    groupsReference.removeEventListener(this);
                    subscriber.onNext(dataSnapshot.getValue(String.class) != null);
                    subscriber.onCompleted();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    subscriber.onError(databaseError.toException());
                }
            };
            groupsReference.child("microsoftGroupId").addValueEventListener(valueEventListener);
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