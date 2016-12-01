package com.nosmurf.shk.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.SignInButton;
import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerLoginComponent;
import com.nosmurf.shk.internal.di.component.LoginComponent;
import com.nosmurf.shk.internal.di.module.LoginModule;
import com.nosmurf.shk.presenter.LoginPresenter;
import com.nosmurf.shk.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;

public class LoginActivity extends RootActivity implements LoginPresenter.View {


    public static final int RC_SIGN_IN = 9001;

    public static final String TAG = "LoginActivity";

    LoginComponent loginComponent;

    @Bind(R.id.container)
    RelativeLayout container;

    @Bind(R.id.sign_in_button)
    SignInButton signInButton;

    @Bind(R.id.progress)
    ContentLoadingProgressBar progress;

    @Bind(R.id.done)
    ImageView done;

    @Bind(R.id.continue_button)
    Button continueButton;

    @Inject
    LoginPresenter loginPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public Presenter getPresenter() {
        return loginPresenter;
    }

    @Override
    protected void initializeInjector() {
        loginComponent = DaggerLoginComponent.builder()
                .appComponent(getAppComponent())
                .loginModule(new LoginModule())
                .build();

        loginComponent.inject(this);
    }

    @Override
    protected void initializeUI() {

    }

    @Override
    protected void initializePresenter() {
        loginPresenter.start(this);
    }

    @Override
    protected void registerListeners() {
        signInButton.setOnClickListener(v -> loginPresenter.onSignInClick());
    }

    @Override
    public void showProgress(String message) {
        progress.setVisibility(View.VISIBLE);
        if (message != null) {
            showSnackbar(message);
        }
    }

    @Override
    public void showProgress(int messageId) {
        progress.setVisibility(View.VISIBLE);
        if (messageId != 0) {
            showSnackbar(messageId);
        }
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void showError(int messageId) {
        showSnackbar(messageId);
    }

    private void showSnackbar(int messageId) {
        Snackbar.make(container, messageId, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginPresenter.onActivityResult(requestCode, data);
    }

    @Override
    public void showCompletedUI() {
        hideProgress();
        done.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleSignInButton(boolean show) {
        signInButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
