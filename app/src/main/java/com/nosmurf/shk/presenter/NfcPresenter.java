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
        // TODO: 01/12/2016
    }

    @Override
    public void destroy() {
        // TODO: 01/12/2016
    }

    public void onContinueClick() {
        // TODO: 01/12/2016
    }

    public interface View extends Presenter.View {

    }
}
