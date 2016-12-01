package com.nosmurf.shk.view.activity;

import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerNfcComponent;
import com.nosmurf.shk.internal.di.component.NfcComponent;
import com.nosmurf.shk.internal.di.module.NfcModule;
import com.nosmurf.shk.presenter.NfcPresenter;
import com.nosmurf.shk.presenter.Presenter;

import javax.inject.Inject;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcActivity extends RootActivity implements NfcPresenter.View {
    @Inject
    NfcPresenter nfcPresenter;

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
    public int getLayoutId() {
        return R.layout.activity_nfc;
    }

    @Override
    public Presenter getPresenter() {
        return nfcPresenter;
    }

    @Override
    protected void initializeInjector() {
        NfcComponent nfcComponent = DaggerNfcComponent
                .builder()
                .appComponent(getAppComponent())
                .nfcModule(new NfcModule())
                .build();

        nfcComponent.inject(this);
    }

    @Override
    protected void initializeUI() {

    }

    @Override
    protected void initializePresenter() {
        nfcPresenter.start(this);
    }

    @Override
    protected void registerListeners() {

    }
}
