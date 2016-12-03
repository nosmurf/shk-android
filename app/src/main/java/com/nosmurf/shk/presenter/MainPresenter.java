package com.nosmurf.shk.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nosmurf.domain.usecase.UploadPhotoUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.exception.ExceptionManager;
import com.nosmurf.shk.utils.FileUtils;
import com.nosmurf.shk.view.activity.RootActivity;
import com.pro100svitlo.fingerprintAuthHelper.FahErrorType;
import com.pro100svitlo.fingerprintAuthHelper.FahListener;
import com.pro100svitlo.fingerprintAuthHelper.FingerprintAuthHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private final UploadPhotoUseCase uploadPhotoUseCase;

    private String photoPath;

    private FingerprintAuthHelper fingerprintAuthHelper;

    @Inject
    public MainPresenter(@Named("uploadPhotoUseCase") UseCase uploadPhotoUseCase) {
        this.uploadPhotoUseCase = (UploadPhotoUseCase) uploadPhotoUseCase;
    }

    @Override
    public void initialize() {
        fingerprintAuthHelper = new FingerprintAuthHelper
                .Builder(view.getContext(),
                new FahListener() {
                    @Override
                    public void onFingerprintStatus(boolean authSuccessful, int errorType, @NotNull CharSequence charSequence) {
                        if (authSuccessful) {
                            view.showFingerPrintSuccess();
                        } else if (fingerprintAuthHelper != null) {
                            // do some stuff here in case auth failed
                            switch (errorType) {
                                case FahErrorType.General.LOCK_SCREEN_DISABLED:
                                case FahErrorType.General.NO_FINGERPRINTS:
                                    fingerprintAuthHelper.showSecuritySettingsDialog();
                                    break;
                                case FahErrorType.Auth.AUTH_NOT_RECOGNIZED:
                                    view.showFingerPrintError();
                                    break;
                                case FahErrorType.Auth.AUTH_TO_MANY_TRIES:
                                    view.showFingerPrintError();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFingerprintListening(boolean b, long l) {
                        // Nothing to do
                    }
                }).build();

        if (fingerprintAuthHelper.isFingerprintEnrolled()) {
            fingerprintAuthHelper.startListening();
        } else {
            view.showNormalUI();
        }
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
        view.showProgress(null);
        uploadPhotoUseCase.execute(photoPath, new UploadPhotoSubscriber());
    }

    public interface View extends Presenter.View {
        void showImage(Bitmap bitmap);

        void showNormalUI();

        void showFingerPrintSuccess();

        void showFingerPrintError();
    }

    private class UploadPhotoSubscriber extends PresenterSubscriber<Void> {

        @Override
        public void onCompleted() {
            view.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            view.showError(ExceptionManager.convert((Exception) e));
        }

        @Override
        public void onNext(Void aVoid) {

        }

    }

}
