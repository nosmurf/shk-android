package com.nosmurf.shk.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import com.nosmurf.domain.model.TokenHashed;
import com.nosmurf.domain.usecase.GetHashedAuthTokenUseCase;
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

    private NfcAdapter adapter;

    private PendingIntent pendingIntent;

    private IntentFilter[] filters;

    private String[][] techListsArray;

    private int sectorIndex;

    private MifareClassic mifareClassic;

    @Inject
    public NfcPresenter(@Named("getHashedTokenUseCase") UseCase getHashedAuthTokenUseCase) {
        this.getHashedAuthTokenUseCase = (GetHashedAuthTokenUseCase) getHashedAuthTokenUseCase;
        this.sectorIndex = 3; // FIXME: 01/12/2016 please, remove hardcoded value
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
        // TODO: 01/12/2016
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
            // 19 72 5d 85 51 31 // FIXME: 01/12/2016 please, remove hardcoded key
            byte keyA[] = {(byte) 0x19, (byte) 0x72, (byte) 0x5d, (byte) 0x85, (byte) 0x51, (byte) 0x31};

            if (mifareClassic.authenticateSectorWithKeyA(sectorIndex, keyA)) {
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
            } else {
                view.hideProgress();
                view.showError(R.string.error_auth);
            }
        } catch (IOException e) {
            view.hideProgress();
            view.showError(R.string.error_reading_tag);
        }

    }

    private void writeTag(TokenHashed tokenHashed) {
        try {
            mifareClassic.writeBlock(tokenHashed.getBlock(), tokenHashed.getHash());
        } catch (IOException e) {
            view.hideProgress();
            view.showError(R.string.error_writing_tag);
        }
    }

    public interface View extends Presenter.View {
        void showCompletedUI();
    }
}
