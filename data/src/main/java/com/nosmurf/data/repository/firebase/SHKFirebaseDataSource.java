package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class SHKFirebaseDataSource implements FirebaseDataSource {

    public static final String TAG = "FirebaseDatabaseSource";

    private final FirebaseAuth firebaseAuth;

    @Inject
    public SHKFirebaseDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
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