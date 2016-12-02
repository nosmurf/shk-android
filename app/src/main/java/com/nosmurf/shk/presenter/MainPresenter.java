package com.nosmurf.shk.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    public void showAndUploadPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        view.showImage(bitmap);
        uploadPhotoUseCase.execute(photoPath, new UploadPhotoSubscriber());
    }

    public interface View extends Presenter.View {
        void showImage(Bitmap bitmap);
    }

    private class UploadPhotoSubscriber extends Subscriber<Uri> {

        Uri photoDownloadUrl;

        @Override
        public void onCompleted() {
            // TODO: 02/12/2016 Save Uri on database
            view.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            view.showError(ExceptionManager.convert((Exception) e));
        }

        @Override
        public void onNext(Uri uri) {
            photoDownloadUrl = uri;
        }

    }

}
