package com.nosmurf.domain.repository;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nosmurf.domain.model.TokenHashed;

import rx.Observable;

public interface Repository {

    Observable<Void> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account);

    Observable<TokenHashed> getHashedToken();
}
