package com.nosmurf.shk.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nosmurf.shk.R;
import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Inject;

import static com.nosmurf.shk.view.activity.LoginActivity.RC_SIGN_IN;

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private static final String TAG = "LoginPresenter";

    private GoogleApiClient googleApiClient;

    @Inject
    public LoginPresenter() {
        super();
    }

    @Override
    public void initialize() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(view.getContext().getString(R.string.server_client_id))
                .requestEmail()
                .build();

        FragmentActivity context = (FragmentActivity) view.getContext();
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(context, connectionResult -> {
                    // TODO: 01/12/2016 change hardcoded string
                    view.showError("Fail");
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    public void destroy() {

    }

    public void onSignInClick() {
        navigator.navigateToSignIngActivity(googleApiClient, ((RootActivity) view.getContext()));

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                Log.i(TAG, "onActivityResult: " + acct.getDisplayName());
            } else {
                Log.e(TAG, "onActivityResult: " + result.getStatus().toString());
            }
        }
    }

    public interface View extends Presenter.View {

    }
}
