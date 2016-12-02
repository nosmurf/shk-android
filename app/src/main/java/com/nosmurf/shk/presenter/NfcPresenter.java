package com.nosmurf.shk.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import com.nosmurf.domain.model.Key;
import com.nosmurf.domain.model.TokenHashed;
import com.nosmurf.domain.usecase.GetHashedAuthTokenUseCase;
import com.nosmurf.domain.usecase.GetKeyUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.R;
import com.nosmurf.shk.view.activity.NfcActivity;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcPresenter extends Presenter<NfcPresenter.View> {

    private final GetHashedAuthTokenUseCase getHashedAuthTokenUseCase;

    private final GetKeyUseCase getKeyUseCase;
    byte defaultKeyA[] = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techListsArray;
    private int sectorIndex;

    private MifareClassic mifareClassic;

    @Inject
    public NfcPresenter(@Named("getHashedTokenUseCase") UseCase getHashedAuthTokenUseCase,
                        @Named("getKeyUseCase") UseCase getKeyUseCase) {
        this.getHashedAuthTokenUseCase = (GetHashedAuthTokenUseCase) getHashedAuthTokenUseCase;
        this.getKeyUseCase = (GetKeyUseCase) getKeyUseCase;
        this.sectorIndex = 3; // FIXME: 01/12/2016 please, remove hardcoded value
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    protected void initialize() {
        adapter = NfcAdapter.getDefaultAdapter(view.getContext());

        RootActivity activity = (NfcActivity) view.getContext();

        pendingIntent = PendingIntent.getActivity(activity, 0,
                new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters = new IntentFilter[]{tagDetected};
        techListsArray = new String[][]{new String[]{MifareClassic.class.getName()}};
    }

    @Override
    public void destroy() {
        getHashedAuthTokenUseCase.unsubscribe();
    }

    public void onContinueClick() {
        // TODO: 01/12/2016
    }

    public void onResume() {
        adapter.enableForegroundDispatch(((NfcActivity) view.getContext()), pendingIntent, filters, techListsArray);
    }

    public void onNewTagDetected(Intent intent) {
        view.showProgress(R.string.tag_detected);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        mifareClassic = MifareClassic.get(tag);

        try {
            mifareClassic.connect();
            encryptNfcTag();

        } catch (IOException e) {
            view.hideProgress();
            view.showError(R.string.error_reading_tag);
        }

    }

    private void writeToken() {
        getHashedAuthTokenUseCase.execute(new PresenterSubscriber<TokenHashed>() {
            @Override
            public void onCompleted() {
                view.hideProgress();
                view.showCompletedUI();
            }

            @Override
            public void onNext(TokenHashed tokenHashed) {
                writeTag(tokenHashed);
            }
        });
    }

    private void encryptNfcTag() {
        getKeyUseCase.execute(new PresenterSubscriber<Key>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(Key key) {
                try {
                    if (mifareClassic.authenticateSectorWithKeyA(key.getSector(), hexStringToByteArray(key.getValue()))) {
                        writeToken();
                    } else if (mifareClassic.authenticateSectorWithKeyA(key.getSector(), defaultKeyA)) {
                        byte[] bytes = mifareClassic.readBlock(key.getBlock());
                        byte[] value = hexStringToByteArray(key.getValue());
                        System.arraycopy(value, 0, bytes, 0, value.length);

                        mifareClassic.writeBlock(key.getBlock(), bytes);
                        writeToken();
                    } else {
                        view.hideProgress();
                        view.showError(R.string.error_format_tag);
                    }
                } catch (IOException e) {
                    view.hideProgress();
                    view.showError(R.string.error_writing_tag_key);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void writeTag(TokenHashed tokenHashed) {
        try {
            mifareClassic.writeBlock(tokenHashed.getBlock(), tokenHashed.getHash());
        } catch (IOException e) {
            view.hideProgress();
            view.showError(R.string.error_writing_tag);
        }
    }

    public void onPause() {
        adapter.disableForegroundDispatch(((NfcActivity) view.getContext()));
    }

    public interface View extends Presenter.View {
        void showCompletedUI();
    }
}
