package com.nosmurf.shk.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nosmurf.shk.internal.di.component.AppComponent;
import com.nosmurf.shk.presenter.Presenter;
import com.nosmurf.shk.view.SecurityHomeKeyApplication;

import butterknife.ButterKnife;

public abstract class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        ButterKnife.bind(this);

        initializeInjector();
        initializeUI();
        initializePresenter();
        registerListeners();
    }

    public abstract int getLayoutId();

    public abstract Presenter getPresenter();

    protected abstract void initializeInjector();

    protected abstract void initializeUI();

    protected abstract void initializePresenter();

    protected abstract void registerListeners();

    protected AppComponent getAppComponent() {
        return ((SecurityHomeKeyApplication) getApplication()).getAppComponent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }
}
