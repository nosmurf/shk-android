package com.nosmurf.shk.view.activity;

import com.nosmurf.shk.presenter.NfcPresenter;
import com.nosmurf.shk.presenter.Presenter;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcActivity extends RootActivity implements NfcPresenter.View {
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
        return 0;
    }

    @Override
    public Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initializeInjector() {

    }

    @Override
    protected void initializeUI() {

    }

    @Override
    protected void initializePresenter() {

    }

    @Override
    protected void registerListeners() {

    }
}
