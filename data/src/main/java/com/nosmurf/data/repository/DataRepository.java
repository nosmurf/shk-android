package com.nosmurf.data.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.data.repository.network.NetworkDataSource;
import com.nosmurf.data.repository.persistence.PersistenceDataSource;
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

    private PersistenceDataSource persistenceDataSource;

    @Inject
    public DataRepository(FirebaseDataSource firebaseDataSource, NetworkDataSource networkDataSource, PersistenceDataSource persistenceDataSource) {
        this.firebaseDataSource = firebaseDataSource;
        this.networkDataSource = networkDataSource;
        this.persistenceDataSource = persistenceDataSource;
    }

    @Override
    public Observable<Void> uploadPhoto(String imagePath) {
        String homeId = persistenceDataSource.getHomeId();
        return Observable.zip(firebaseDataSource.uploadPhoto(homeId, imagePath), firebaseDataSource.getPersonId(homeId),
                (imageUrl, microsoftId) -> new ImageReference(imageUrl, homeId, microsoftId))
                .flatMap(imageReference -> networkDataSource.addFaceOnMicrosoftFaceAPI(imageReference));
        // TODO: 03/12/2016 train API 
    }

    @Override
    public Observable<Void> doLogin(GoogleSignInAccount account, String parentEmail) {

        return firebaseDataSource.doLogin(account, parentEmail)
                .flatMap(new Func1<String, Observable<PersonReference>>() {
                    @Override
                    public Observable<PersonReference> call(String userId) {
                        return firebaseDataSource.hasGroupOnMicrosoft(userId)
                                .flatMap(hasMicrosoftGroup -> {
                                    if (hasMicrosoftGroup) {
                                        return Observable.just(new PersonReference(userId, userId));
                                    } else {
                                        return networkDataSource.createGroupOnMicrosoftFaceAPI(userId)
                                                .flatMap(microsoftGroupId -> {
                                                    persistenceDataSource.setHomeId(microsoftGroupId);
                                                    return firebaseDataSource.saveMicrosoftGroupId();
                                                })
                                                .map(groupId -> new PersonReference(userId, groupId));
                                    }
                                });
                    }
                })
                .flatMap(personReference -> networkDataSource.createPersonOnMicrosoftFaceAPI(personReference)
                        .map(microsoftId -> {
                            personReference.setMicrosoftId(microsoftId);
                            return personReference;
                        }))
                .flatMap(firebaseDataSource::saveMicrosoftId);


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
