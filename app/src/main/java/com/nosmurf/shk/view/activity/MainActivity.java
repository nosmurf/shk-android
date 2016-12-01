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

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    MainComponent mainComponent;

    @Inject
    MainPresenter mainPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_layout;
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
}
