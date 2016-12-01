package com.nosmurf.shk.presenter;

import android.util.Log;

import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.exception.ExceptionManager;
import com.nosmurf.shk.utils.FileUtils;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private final UseCase uploadPhotoUseCase;

    private String photoPath;


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

    public void takeAPhoto(int requestImageCapture) {
        try {
            File file = FileUtils.createImageFile();
            photoPath = file.getPath();
            navigator.navigateToCameraActivity((RootActivity) view, file, requestImageCapture);
        } catch (IOException e) {
            view.showError(ExceptionManager.convert(e));
        }
    }

    public void decodeImageAndShow() {
        Log.i("Photo", photoPath);
    }

    public interface View extends Presenter.View {

    }

}
