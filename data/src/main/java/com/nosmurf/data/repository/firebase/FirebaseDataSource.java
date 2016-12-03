package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<String> uploadPhoto(String homeId, String imagePath);

    Observable<String> doLogin(GoogleSignInAccount account, String parentEmail);

    Observable<String> getCurrentUser();

    Observable<Boolean> hasCurrentUser();

    Observable<String> getGroupId(String uid);

    Observable<String> getPersonId(String groupId);

    Observable<Boolean> hasMicrosoftId();

    Observable<Void> saveMicrosoftId(PersonReference personReference);

    Observable<TokenHashed> getHashedToken();

    Observable<Key> getKey(String parentId);

    Observable<String> saveMicrosoftGroupId();

    Observable<Boolean> hasGroupOnMicrosoft(String uid);
}
