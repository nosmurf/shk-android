package com.nosmurf.shk.navigation;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.nosmurf.shk.view.activity.RootActivity;

import java.io.File;

import javax.inject.Inject;

public class Navigator {

    @Inject
    public Navigator() {

    }

    public void navigateToCameraActivity(RootActivity activity, File photoFile, int requestImageCapture) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null && photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            activity.startActivityForResult(takePictureIntent, requestImageCapture);
        }
    }

}
