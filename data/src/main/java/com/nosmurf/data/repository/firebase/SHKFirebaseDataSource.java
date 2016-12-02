package com.nosmurf.data.repository.firebase;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";

    StorageReference storageReference;

    private final FirebaseAuth firebaseAuth;


    @Inject
    public SHKFirebaseDataSource() {
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public Observable<Boolean> uploadPhoto(String imagePath) {
        File image = new File(imagePath);
        Uri uri = Uri.fromFile(image);

        // TODO: 01/12/2016 Change "photos" to USER ID
        final StorageReference userPhotosRef = storageReference.child("photos").child(uri.getLastPathSegment());

        return Observable.create(subscriber -> {
            userPhotosRef.putFile(uri)
                    .addOnSuccessListener(task -> {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    })
                    .addOnFailureListener(subscriber::onError);
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
}