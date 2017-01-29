package com.nosmurf.shk.presenter;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nosmurf.domain.model.Access;
import com.nosmurf.domain.usecase.GetAccessUserCase;
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
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private final UploadPhotoUseCase uploadPhotoUseCase;

    private final GetAccessUserCase getAccessUserCase;

    private String photoPath;

    private FingerprintAuthHelper fingerprintAuthHelper;

    @Inject
    public MainPresenter(@Named("uploadPhotoUseCase") UseCase uploadPhotoUseCase,
                         @Named("getAccessUserCase") UseCase getAccessUserCase) {
        this.uploadPhotoUseCase = (UploadPhotoUseCase) uploadPhotoUseCase;
        this.getAccessUserCase = (GetAccessUserCase) getAccessUserCase;
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
                            getAccess();
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
            getAccess();
            view.showNormalUI();
        }
    }

    private void getAccess() {
        view.showProgress(null);
        getAccessUserCase.execute(new GetAccessSubscriber());
    }

    @Override
    public void destroy() {
        getAccessUserCase.unsubscribe();
        uploadPhotoUseCase.unsubscribe();
    }

    public void takeAPhoto(int requestImageCapture) {
        Dexter.checkPermissions(
                new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                File file = FileUtils.createImageFile(view.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                                photoPath = file.getPath();
                                navigator.navigateToCameraActivity((RootActivity) view, file, requestImageCapture);
                            } catch (IOException e) {
                                view.showError(ExceptionManager.convert(e));
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        view.showError("You must accept the permissions");
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
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

        void showAccess(List<Access> access);
    }

    private class GetAccessSubscriber extends PresenterSubscriber<List<Access>> {

        @Override
        public void onCompleted() {
            view.hideProgress();
        }

        @Override
        public void onNext(List<Access> accesses) {
            view.showAccess(accesses);
            view.hideProgress();
        }

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
