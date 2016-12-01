package com.nosmurf.domain.repository;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import rx.Observable;

public interface Repository {

    Observable<Boolean> doLogin(GoogleSignInAccount account);
}
