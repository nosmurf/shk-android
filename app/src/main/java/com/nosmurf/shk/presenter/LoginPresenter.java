package com.nosmurf.shk.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nosmurf.domain.usecase.DoLoginUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.R;
import com.nosmurf.shk.view.activity.RootActivity;
import com.pro100svitlo.fingerprintAuthHelper.FahErrorType;
import com.pro100svitlo.fingerprintAuthHelper.FahListener;
import com.pro100svitlo.fingerprintAuthHelper.FingerprintAuthHelper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;

import static com.nosmurf.shk.view.activity.LoginActivity.RC_SIGN_IN;

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private static final String TAG = "LoginPresenter";

    private GoogleApiClient googleApiClient;

    private DoLoginUseCase doLoginUseCase;

    private FingerprintAuthHelper fingerprintAuthHelper;

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
                    view.hideProgress();
                    view.showError(R.string.google_signin_error);
                    view.toggleSignInButton(true);
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Override
    public void destroy() {
        fingerprintAuthHelper.stopListening();
        fingerprintAuthHelper.onDestroy();
    }

    public void onSignInClick() {
        view.showProgress(R.string.loging);
        view.toggleSignInButton(false);
        navigator.navigateToSignIngActivity(googleApiClient, ((RootActivity) view.getContext()));

    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                doLoginUseCase.execute(account, new PresenterSubscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                        view.showCompletedUI();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        // Nothing to do
                    }
                });

            } else {
                view.showError(R.string.google_signin_error);
            }
        }
    }

    public void onContinueClick() {
        fingerprintAuthHelper = new FingerprintAuthHelper
                .Builder(view.getContext(),
                new FahListener() {
                    @Override
                    public void onFingerprintStatus(boolean authSuccessful, int errorType, @NotNull CharSequence charSequence) {
                        if (authSuccessful) {
                            navigator.navigateToNfcActivity((RootActivity) view.getContext());
                        } else if (fingerprintAuthHelper != null) {
                            // do some stuff here in case auth failed
                            switch (errorType) {
                                case FahErrorType.General.LOCK_SCREEN_DISABLED:
                                case FahErrorType.General.NO_FINGERPRINTS:
                                    fingerprintAuthHelper.showSecuritySettingsDialog();
                                    break;
                                case FahErrorType.Auth.AUTH_NOT_RECOGNIZED:
                                    view.showError(R.string.try_again);
                                    break;
                                case FahErrorType.Auth.AUTH_TO_MANY_TRIES:
                                    view.showError(R.string.try_again);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFingerprintListening(boolean b, long l) {
                        // Nothing to do
                    }
                }).build();

        if (fingerprintAuthHelper.isHardwareEnable()) {
            fingerprintAuthHelper.startListening();
        }
    }

    public interface View extends Presenter.View {
        void showCompletedUI();

        void toggleSignInButton(boolean show);
    }
}
