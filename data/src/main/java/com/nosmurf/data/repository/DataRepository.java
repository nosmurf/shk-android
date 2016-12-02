package com.nosmurf.data.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;
import com.nosmurf.domain.repository.Repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DataRepository implements Repository {

    private FirebaseDataSource firebaseDataSource;

    //private NetworkDataSource networkDataSource;

    @Inject
    public DataRepository(FirebaseDataSource firebaseDataSource/*, NetworkDataSource networkDataSource*/) {
        this.firebaseDataSource = firebaseDataSource;
        //this.networkDataSource = networkDataSource;
    }

    @Override
    public Observable<Void> uploadPhoto(String imagePath) {
        return firebaseDataSource.uploadPhoto(imagePath);
    }

    @Override
    public Observable<Void> doLogin(GoogleSignInAccount account) {
        return firebaseDataSource.doLogin(account);
    }

    @Override
    public Observable<TokenHashed> getHashedToken() {
        return firebaseDataSource.getHashedToken();
    }

    @Override
    public Observable<Key> getKey() {
        return firebaseDataSource.getKey();
    }

    @Override
    public Observable<Boolean> hasCurrentUser() {
        return firebaseDataSource.hasCurrentUser()
                .delay(500, TimeUnit.MILLISECONDS);
    }
}
