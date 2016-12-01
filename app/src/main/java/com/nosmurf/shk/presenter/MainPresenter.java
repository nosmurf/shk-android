package com.nosmurf.shk.presenter;

import android.util.Log;

import com.nosmurf.domain.usecase.UploadPhotoUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.exception.ExceptionManager;
import com.nosmurf.shk.utils.FileUtils;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private final UploadPhotoUseCase uploadPhotoUseCase;

    private String photoPath;

    @Inject
    public MainPresenter(@Named("uploadPhotoUseCase") UseCase uploadPhotoUseCase) {
        this.uploadPhotoUseCase = (UploadPhotoUseCase) uploadPhotoUseCase;
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
        uploadPhotoUseCase.execute(photoPath, new UploadPhotoSubscriber());
    }

    public interface View extends Presenter.View {

    }

    private class UploadPhotoSubscriber extends Subscriber<Boolean> {

        boolean result;

        @Override
        public void onCompleted() {
            view.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            view.showError(ExceptionManager.convert((Exception) e));
        }

        @Override
        public void onNext(Boolean aBoolean) {
            result = aBoolean;
        }
    }

}
