package com.nosmurf.domain.repository;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import model.TokenHashed;
import rx.Observable;

public interface Repository {

    Observable<Void> doLogin(GoogleSignInAccount account);

    Observable<TokenHashed> getHashedToken();
}
