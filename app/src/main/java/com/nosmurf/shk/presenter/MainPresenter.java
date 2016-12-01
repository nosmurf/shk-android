package com.nosmurf.shk.presenter;

import com.nosmurf.domain.usecase.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private final UseCase uploadPhotoUseCase;

    @Inject
    public MainPresenter(@Named("uploadPhotoUseCase") UseCase uploadPhotoUseCase) {
        this.uploadPhotoUseCase = uploadPhotoUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }

    public void takeAPhoto() {

    }

    public interface View extends Presenter.View {

    }



}
