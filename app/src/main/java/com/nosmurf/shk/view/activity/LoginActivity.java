package com.nosmurf.shk.view.activity;

import android.content.Context;
import android.content.Intent;

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

    @Bind(R.id.sign_in_button)
    SignInButton signInButton;

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
        // TODO: 23/11/2016
    }

    @Override
    public void showProgress(int messageId) {
        // TODO: 23/11/2016
    }

    @Override
    public void hideProgress() {
        // TODO: 23/11/2016
    }

    @Override
    public void showError(String message) {
        // TODO: 23/11/2016
    }

    @Override
    public void showError(int messageId) {
        // TODO: 23/11/2016
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginPresenter.onActivityResult(requestCode, data);
    }
}
