package com.nosmurf.data.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.data.repository.network.NetworkDataSource;
import com.nosmurf.domain.repository.Repository;

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
                    public Observable<Boolean> call(Boolean logged) {
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
}
