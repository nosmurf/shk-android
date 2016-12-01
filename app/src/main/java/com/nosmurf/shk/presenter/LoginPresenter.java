package com.nosmurf.shk.presenter;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private GoogleApiClient googleApiClient;

    @Inject
    public LoginPresenter() {
        super();
    }

    @Override
    public void initialize() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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


    }

    public interface View extends Presenter.View {

    }
}
