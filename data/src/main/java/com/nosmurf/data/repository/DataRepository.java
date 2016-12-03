package com.nosmurf.data.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.data.repository.network.NetworkDataSource;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;
import com.nosmurf.domain.repository.Repository;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataRepository implements Repository {

    private FirebaseDataSource firebaseDataSource;

    private NetworkDataSource networkDataSource;

    @Inject
    public DataRepository(FirebaseDataSource firebaseDataSource, NetworkDataSource networkDataSource) {
        this.firebaseDataSource = firebaseDataSource;
        this.networkDataSource = networkDataSource;
    }

    @Override
    public Observable<Void> uploadPhoto(String imagePath) {
        return Observable
                .zip(firebaseDataSource.uploadPhoto(imagePath), firebaseDataSource.getGroupId(), firebaseDataSource.getPersonId(),
                        ImageReference::new)
                .flatMap(imageReference -> networkDataSource.addFaceOnMicrosoftFaceAPI(imageReference));
    }

    @Override
    public Observable<Void> doLogin(GoogleSignInAccount account) {

        return firebaseDataSource.doLogin(account)
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return firebaseDataSource.hasGroupOnMicrosoft();
                    }
                })
                .filter((hasGroup) -> !hasGroup)
                .flatMap(new Func1<Boolean, Observable<String>>() {
                    @Override
                    public Observable<String> call(Boolean aBoolean) {
                        return firebaseDataSource.getCurrentUser();
                    }
                }).flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String userId) {
                        return networkDataSource.createGroupOnMicrosoftFaceAPI(userId);
                    }
                }).flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String s) {
                        return firebaseDataSource.saveMicrosoftGroupId();
                    }
                })
                .flatMap(new Func1<Boolean, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Boolean aBoolean) {
                        return firebaseDataSource.hasMicrosoftId();
                    }
                }).filter((aBoolean1) -> !aBoolean1)
                .flatMap(new Func1<Boolean, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(Boolean aBoolean) {
                        return Observable.zip(firebaseDataSource.getCurrentUser(), firebaseDataSource.getGroupId(),
                                PersonReference::new)
                                .flatMap(personReference -> networkDataSource.createPersonOnMicrosoftFaceAPI(personReference))
                                .flatMap(microsoftId -> firebaseDataSource.saveMicrosoftId(microsoftId));
                    }
                });
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
