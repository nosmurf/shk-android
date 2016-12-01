package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Inject;

import model.TokenHashed;
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

    @Override
    public Observable<TokenHashed> getHashedToken() {
        return Observable.create(subscriber -> {
            String token = firebaseAuth.getCurrentUser().getUid();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-384");
                for (byte b : token.getBytes()) {
                    messageDigest.update(b);

                }

                // FIXME: 01/12/2016 remove hardcoded numbers
                subscriber.onNext(new TokenHashed(12, Arrays.copyOfRange(messageDigest.digest(), 0, 16)));
                subscriber.onNext(new TokenHashed(13, Arrays.copyOfRange(messageDigest.digest(), 16, 32)));
                subscriber.onNext(new TokenHashed(14, Arrays.copyOfRange(messageDigest.digest(), 32, 48)));

                subscriber.onCompleted();

            } catch (NoSuchAlgorithmException exception) {
                subscriber.onError(exception);
            }

        });
    }
}