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
                .zip(firebaseDataSource.uploadPhoto(imagePath), firebaseDataSource.getGroupId(null), firebaseDataSource.getPersonId(),
                        ImageReference::new)
                .flatMap(new Func1<ImageReference, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(ImageReference imageReference) {
                        return Observable.empty();
                    }
                });
        //.flatMap(imageReference -> networkDataSource.addFaceOnMicrosoftFaceAPI(imageReference));
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
                                                .flatMap(s -> firebaseDataSource.saveMicrosoftGroupId())
                                                .map(groupId -> new PersonReference(userId, groupId));
                                    }
                                });
                    }
                })
                .flatMap(personReference -> {
                    return networkDataSource.createPersonOnMicrosoftFaceAPI(personReference);
                })
                .flatMap(microsoftId -> firebaseDataSource.saveMicrosoftId(microsoftId));



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
