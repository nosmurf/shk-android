package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<String> uploadPhoto(String imagePath);

    Observable<String> doLogin(GoogleSignInAccount account, String parentEmail);

    Observable<String> getCurrentUser();

    Observable<Boolean> hasCurrentUser();

    Observable<String> getGroupId(String uid);

    Observable<String> getPersonId();

    Observable<Boolean> hasMicrosoftId();

    Observable<Void> saveMicrosoftId(String microsoftId);

    Observable<TokenHashed> getHashedToken();

    Observable<Key> getKey();

    Observable<String> saveMicrosoftGroupId();

    Observable<Boolean> hasGroupOnMicrosoft(String uid);
}
