package com.nosmurf.domain.repository;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import rx.Observable;

public interface Repository {

    Observable<Uri> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account);

}
