package com.nosmurf.shk.navigation;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nosmurf.shk.view.activity.NfcActivity;
import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Inject;

import static com.nosmurf.shk.view.activity.LoginActivity.RC_SIGN_IN;

public class Navigator {


    @Inject
    public Navigator() {

    }

    public void navigateToSignIngActivity(GoogleApiClient apiClient, RootActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void navigateToNfcActivity(Context context) {
        Intent intent = NfcActivity.getCallingIntent(context);
        context.startActivity(intent);
    }
}
