package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface FirebaseDataSource {
    Observable<Void> doLogin(GoogleSignInAccount account);

    Observable<TokenHashed> getHashedToken();
}
