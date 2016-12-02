package com.nosmurf.shk.view.activity;

import android.content.Context;

import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerSplashComponent;
import com.nosmurf.shk.internal.di.component.SplashComponent;
import com.nosmurf.shk.internal.di.module.SplashModule;
import com.nosmurf.shk.presenter.Presenter;
import com.nosmurf.shk.presenter.SplashPresenter;

import javax.inject.Inject;

/**
 * Created by Sergio on 02/12/2016.
 */

public class SplashActivity extends RootActivity implements SplashPresenter.View {

    @Inject
    SplashPresenter splashPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Presenter getPresenter() {
        return splashPresenter;
    }

    @Override
    protected void initializeInjector() {
        SplashComponent splashComponent = DaggerSplashComponent
                .builder()
                .appComponent(getAppComponent())
                .splashModule(new SplashModule())
                .build();

        splashComponent.inject(this);
    }

    @Override
    protected void initializeUI() {
        // Nothing to do
    }

    @Override
    protected void initializePresenter() {
        splashPresenter.start(this);
    }

    @Override
    protected void registerListeners() {
        // Nothing to do
    }

    @Override
    public void showProgress(String message) {
        // Nothing to do
    }

    @Override
    public void showProgress(int messageId) {
        // Nothing to do
    }

    @Override
    public void hideProgress() {
        // Nothing to do
    }

    @Override
    public void showError(String message) {
        // Nothing to do
    }

    @Override
    public void showError(int messageId) {
        // Nothing to do
    }

    @Override
    public Context getContext() {
        return this;
    }
}
