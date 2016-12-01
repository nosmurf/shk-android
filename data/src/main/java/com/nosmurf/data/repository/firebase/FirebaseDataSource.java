package com.nosmurf.data.repository.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import rx.Observable;

public interface FirebaseDataSource {
    Observable<Boolean> doLogin(GoogleSignInAccount account);
}
