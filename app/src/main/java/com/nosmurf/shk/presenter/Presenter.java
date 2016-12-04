package com.nosmurf.shk.presenter;

import android.content.Context;

import com.nosmurf.shk.exception.ExceptionManager;
import com.nosmurf.shk.navigation.Navigator;

import javax.inject.Inject;

import rx.Subscriber;

public abstract class Presenter<T extends Presenter.View> {

    T view;

    @Inject
    Navigator navigator;

    public void start(T v) {
        this.view = v;
        if (v == null) {
            throw new RuntimeException();
        }

        this.initialize();
    }

    protected abstract void initialize();

    public abstract void destroy();

    public interface View {
        void showProgress(String message);

        void showProgress(int messageId);

        void hideProgress();

        void showError(String message);

        void showError(int messageId);

        Context getContext();
    }


    protected abstract class PresenterSubscriber<T> extends Subscriber<T> {

        @Override
        public void onError(Throwable e) {
            view.showError(ExceptionManager.convert((Exception) e));
            view.hideProgress();
        }
        
    }

}
