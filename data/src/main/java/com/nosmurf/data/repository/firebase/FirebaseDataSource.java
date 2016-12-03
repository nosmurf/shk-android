package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<String> uploadPhoto(String imagePath);

    Observable<Boolean> doLogin(GoogleSignInAccount account);

    Observable<String> getCurrentUser();

    Observable<Boolean> hasCurrentUser();

    Observable<String> getGroupId();

    Observable<String> getPersonId();

    Observable<Boolean> hasMicrosoftId();

    Observable<Void> saveMicrosoftId(String microsoftId);

    Observable<TokenHashed> getHashedToken();

    Observable<Key> getKey();

    Observable<Void> saveMicrosoftGroupId();

    Observable<Boolean> hasGroupOnMicrosoft();
}
