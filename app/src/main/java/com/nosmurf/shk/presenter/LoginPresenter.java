package com.nosmurf.shk.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nosmurf.domain.usecase.DoLoginUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.R;
import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Inject;
import javax.inject.Named;

import static com.nosmurf.shk.view.activity.LoginActivity.RC_SIGN_IN;

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private static final String TAG = "LoginPresenter";

    private GoogleApiClient googleApiClient;

    private DoLoginUseCase doLoginUseCase;

    @Inject
    public LoginPresenter(@Named("doLoginUseCase") UseCase doLoginUseCase) {
        this.doLoginUseCase = (DoLoginUseCase) doLoginUseCase;
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
                GoogleSignInAccount account = result.getSignInAccount();
                doLoginUseCase.execute(account, new PresenterSubscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        // Nothing to do
                    }
                });

            } else {
                Log.e(TAG, "onActivityResult: " + result.getStatus().toString());
            }
        }
    }

    public interface View extends Presenter.View {

    }
}
