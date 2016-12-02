package com.nosmurf.domain.repository;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface Repository {

    Observable<Void> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account);

    Observable<TokenHashed> getHashedToken();

    Observable<Key> getKey();
}
