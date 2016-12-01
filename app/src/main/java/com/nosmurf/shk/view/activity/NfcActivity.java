package com.nosmurf.shk.view.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerNfcComponent;
import com.nosmurf.shk.internal.di.component.NfcComponent;
import com.nosmurf.shk.internal.di.module.NfcModule;
import com.nosmurf.shk.presenter.NfcPresenter;
import com.nosmurf.shk.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcActivity extends RootActivity implements NfcPresenter.View {

    @Bind(R.id.container)
    RelativeLayout container;

    @Bind(R.id.progress)
    ContentLoadingProgressBar progress;

    @Bind(R.id.done)
    ImageView done;

    @Bind(R.id.continue_button)
    Button continueButton;

    @Inject
    NfcPresenter nfcPresenter;

    @Override
    public void showProgress(String message) {
        progress.setVisibility(View.VISIBLE);
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(int messageId) {
        progress.setVisibility(View.VISIBLE);
        Snackbar.make(container, messageId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showError(int messageId) {
        Snackbar.make(container, messageId, Snackbar.LENGTH_SHORT).show();
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
