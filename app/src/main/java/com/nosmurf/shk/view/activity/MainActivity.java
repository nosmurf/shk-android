package com.nosmurf.shk.view.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;

import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerMainComponent;
import com.nosmurf.shk.internal.di.component.MainComponent;
import com.nosmurf.shk.presenter.MainPresenter;
import com.nosmurf.shk.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends RootActivity implements MainPresenter.View {

    public static final String TAG = "MainActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    MainComponent mainComponent;

    @Inject
    MainPresenter mainPresenter;

    @Bind(R.id.camera)
    FloatingActionButton camera;

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

    }

    @OnClick(R.id.camera)
    void takeAPhoto() {
        mainPresenter.takeAPhoto(REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void showProgress(int messageId) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showError(int messageId) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mainPresenter.decodeImageAndShow();
        }
    }
}
