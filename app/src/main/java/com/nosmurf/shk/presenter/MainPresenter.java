package com.nosmurf.shk.presenter;

import com.nosmurf.shk.exception.ExceptionManager;
import com.nosmurf.shk.utils.FileUtils;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MainPresenter extends Presenter<MainPresenter.View> {

    private String photoPath;

    @Inject
    public MainPresenter() {
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
        
    }


    public interface View extends Presenter.View {

    }

}
