package com.nosmurf.shk.presenter;

import com.nosmurf.domain.usecase.HasCurrentUserUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Sergio on 02/12/2016.
 */

public class SplashPresenter extends Presenter<SplashPresenter.View> {

    private final HasCurrentUserUseCase hasCurrentUserUseCase;

    @Inject
    public SplashPresenter(@Named("hasCurrentUserUseCase") UseCase hasCurrentUserUseCase) {
        this.hasCurrentUserUseCase = (HasCurrentUserUseCase) hasCurrentUserUseCase;
    }

    @Override
    protected void initialize() {
        hasCurrentUserUseCase.execute(new PresenterSubscriber<Boolean>() {
            public Boolean hasCurrentUser;

            @Override
            public void onCompleted() {
                if (hasCurrentUser) {
                    navigator.navigateToMainActivity((RootActivity) view.getContext());
                } else {
                    navigator.navigateToLoginActivity((RootActivity) view.getContext());
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                this.hasCurrentUser = aBoolean;
            }
        });
    }

    @Override
    public void destroy() {

    }

    public interface View extends Presenter.View {

    }
}
