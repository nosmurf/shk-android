package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<Boolean> uploadPhoto(String imagePath);

    Observable<Void> doLogin(GoogleSignInAccount account);
    
}
