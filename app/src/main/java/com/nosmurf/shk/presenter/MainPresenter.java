package com.nosmurf.shk.presenter;

import javax.inject.Inject;

public class MainPresenter extends Presenter<MainPresenter.View> {

    @Inject
    public MainPresenter() {
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }

    public interface View  extends Presenter.View{

    }
}
