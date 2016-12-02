package com.nosmurf.shk.navigation;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nosmurf.shk.view.activity.MainActivity;
import com.nosmurf.shk.view.activity.NfcActivity;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.File;

import javax.inject.Inject;

import static com.nosmurf.shk.view.activity.LoginActivity.RC_SIGN_IN;

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

    public void navigateToSignIngActivity(GoogleApiClient apiClient, RootActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void navigateToNfcActivity(RootActivity activity) {
        Intent intent = NfcActivity.getCallingIntent(activity);
        activity.startActivity(intent);
        activity.finish();
    }

    public void navigateToMainActivity(RootActivity activity) {
        Intent intent = MainActivity.getCallingIntent(activity);
        activity.startActivity(intent);
        activity.finish();
    }
}
