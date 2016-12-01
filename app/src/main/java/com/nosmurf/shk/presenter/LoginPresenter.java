package com.nosmurf.shk.presenter;

import com.nosmurf.shk.presenter.Presenter.View;

import javax.inject.Inject;

public class LoginPresenter extends Presenter<View> {

    @Inject
    public LoginPresenter() {
        super();
    }

    @Override
    public void initialize() {
        view.showProgress(null);
    }

    @Override
    public void destroy() {

    }
}
