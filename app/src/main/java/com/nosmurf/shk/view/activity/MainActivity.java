package com.nosmurf.shk.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.RelativeLayout;

import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerMainComponent;
import com.nosmurf.shk.internal.di.component.MainComponent;
import com.nosmurf.shk.presenter.MainPresenter;
import com.nosmurf.shk.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends RootActivity implements MainPresenter.View {

    public static final String TAG = "MainActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    MainComponent mainComponent;

    @Inject
    MainPresenter mainPresenter;

    @Bind(R.id.container)
    RelativeLayout container;

    @Bind(R.id.progress)
    ContentLoadingProgressBar progress;

    @Bind(R.id.camera)
    FloatingActionButton camera;

    public static Intent getCallingIntent(RootActivity rootActivity) {
        return new Intent(rootActivity, MainActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    public Presenter getPresenter() {
        return mainPresenter;
    }

    @Override
    protected void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .appComponent(getAppComponent())
                .build();

        mainComponent.inject(this);
    }

    @Override
    protected void initializeUI() {

    }

    @Override
    protected void initializePresenter() {
        mainPresenter.start(this);
    }

    @Override
    protected void registerListeners() {
        camera.setOnClickListener(view -> mainPresenter.takeAPhoto(REQUEST_IMAGE_CAPTURE));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mainPresenter.showAndUploadPhoto();
        }
    }

    @Override
    public void showImage(Bitmap bitmap) {
        // TODO: 01/12/2016 Show Photo
    }

    public Context getContext() {
        return this;
    }

    private void showSnackbar(int messageId) {
        Snackbar.make(container, messageId, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

}
