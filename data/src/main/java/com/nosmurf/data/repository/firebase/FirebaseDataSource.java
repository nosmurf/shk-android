package com.nosmurf.data.repository.firebase;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<Uri> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account);

}
