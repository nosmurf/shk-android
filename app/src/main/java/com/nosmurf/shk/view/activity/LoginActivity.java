package com.nosmurf.shk.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.SignInButton;
import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerLoginComponent;
import com.nosmurf.shk.internal.di.component.LoginComponent;
import com.nosmurf.shk.internal.di.module.LoginModule;
import com.nosmurf.shk.presenter.LoginPresenter;
import com.nosmurf.shk.presenter.Presenter;
import com.nosmurf.shk.utils.AnimationUtils;
import com.nosmurf.shk.view.FingerPrintDialog;

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

    @Bind(R.id.reveal)
    View reveal;

    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.image_container)
    LinearLayout emailContainer;

    @Inject
    LoginPresenter loginPresenter;

    public static Intent getCallingIntent(RootActivity activity) {
        return new Intent(activity, LoginActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
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
        continueButton.setOnClickListener(v -> loginPresenter.onContinueClick());
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

        emailContainer.setVisibility(View.GONE);

        AnimationUtils.enterReveal(reveal, () -> {
            done.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            int green = ContextCompat.getColor(this, R.color.light_green_500);
            container.setBackgroundColor(green);

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(green));
        }, 1);
    }

    @Override
    public void toggleSignInButton(boolean show) {
        signInButton.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showFingerPrintDialog(FingerPrintDialog.OnFingerPrintDialogListener onFingerPrintDialogListener) {
        FingerPrintDialog dialog = new FingerPrintDialog();
        dialog.setParams(onFingerPrintDialogListener);
        dialog.show(getSupportFragmentManager(), "fragment_container");

    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }
}
