package com.nosmurf.shk.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nosmurf.shk.R;
import com.nosmurf.shk.utils.AnimationUtils;
import com.pro100svitlo.fingerprintAuthHelper.FahErrorType;
import com.pro100svitlo.fingerprintAuthHelper.FahListener;
import com.pro100svitlo.fingerprintAuthHelper.FingerprintAuthHelper;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sergio on 02/12/2016.
 */

public class FingerPrintDialog extends DialogFragment {

    @Bind(R.id.green)
    ImageView green;

    @Bind(R.id.grey)
    ImageView grey;

    @Bind(R.id.red)
    ImageView red;

    private FingerprintAuthHelper fingerprintAuthHelper;

    private OnFingerPrintDialogListener onFingerPrintDialogListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fingerprint_dialog, container, false);
        ButterKnife.bind(this, view);

        fingerprintAuthHelper = new FingerprintAuthHelper
                .Builder(getActivity(),
                new FahListener() {
                    @Override
                    public void onFingerprintStatus(boolean authSuccessful, int errorType, @NotNull CharSequence charSequence) {
                        if (authSuccessful) {
                            showFingerPrintSuccess();
                        } else if (fingerprintAuthHelper != null) {
                            // do some stuff here in case auth failed
                            switch (errorType) {
                                case FahErrorType.General.LOCK_SCREEN_DISABLED:
                                case FahErrorType.General.NO_FINGERPRINTS:
                                    fingerprintAuthHelper.showSecuritySettingsDialog();
                                    break;
                                case FahErrorType.Auth.AUTH_NOT_RECOGNIZED:
                                    showFingerPrintError();
                                    break;
                                case FahErrorType.Auth.AUTH_TO_MANY_TRIES:
                                    showFingerPrintError();
                                    onFingerPrintDialogListener.onFingerPrintFinalError(FingerPrintDialog.this);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFingerprintListening(boolean b, long l) {
                        // Nothing to do
                    }
                }).build();

        if (fingerprintAuthHelper.isHardwareEnable()) {
            fingerprintAuthHelper.startListening();
        } else {
            onFingerPrintDialogListener.onFingerPrintNotSupported(this);
        }

        return view;
    }

    private void showFingerPrintSuccess() {
        red.setVisibility(View.GONE);
        grey.setVisibility(View.GONE);
        AnimationUtils.enterReveal(green, () -> onFingerPrintDialogListener.onFingerPrintSuccess(this), 2);
    }

    private void showFingerPrintError() {
        grey.setVisibility(View.GONE);
        green.setVisibility(View.GONE);
        AnimationUtils.enterReveal(red, () -> onFingerPrintDialogListener.onFingerPrintError(this), 2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fingerprintAuthHelper.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        fingerprintAuthHelper.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fingerprintAuthHelper.onDestroy();
    }

    public void setParams(OnFingerPrintDialogListener listener) {
        this.onFingerPrintDialogListener = listener;
    }

    public interface OnFingerPrintDialogListener {
        void onFingerPrintSuccess(FingerPrintDialog dialog);

        void onFingerPrintError(FingerPrintDialog dialog);

        void onFingerPrintFinalError(FingerPrintDialog dialog);

        void onFingerPrintNotSupported(FingerPrintDialog dialog);
    }
}
