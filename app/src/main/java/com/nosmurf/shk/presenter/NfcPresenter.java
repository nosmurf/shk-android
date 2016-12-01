package com.nosmurf.shk.presenter;

import javax.inject.Inject;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcPresenter extends Presenter<NfcPresenter.View> {

    @Inject
    public NfcPresenter() {
        super();
    }


    @Override
    protected void initialize() {

    }

    @Override
    public void destroy() {

    }

    public interface View extends Presenter.View {

    }
}
