package com.nosmurf.shk.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import com.nosmurf.shk.R;
import com.nosmurf.shk.view.activity.RootActivity;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcPresenter extends Presenter<NfcPresenter.View> {

    private NfcAdapter adapter;

    private PendingIntent pendingIntent;

    private IntentFilter[] filters;

    private String[][] techListsArray;
    private int sectorIndex;

    @Inject
    public NfcPresenter() {
        super();
        this.sectorIndex = 3; // FIXME: 01/12/2016 put it random please
    }


    @Override
    protected void initialize() {
        adapter = NfcAdapter.getDefaultAdapter(view.getContext());

        RootActivity activity = (RootActivity) view.getContext();

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
        adapter.enableForegroundDispatch(((RootActivity) view.getContext()), pendingIntent, filters, techListsArray);
    }

    public void onNewTagDetected(Intent intent) {
        view.showProgress(R.string.tag_detected);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mifareClassic = MifareClassic.get(tag);

        try {
            mifareClassic.connect();
            // 19 72 5d 85 51 31
            byte keyA[] = {(byte) 0x19, (byte) 0x72, (byte) 0x5d, (byte) 0x85, (byte) 0x51, (byte) 0x31};

            sectorIndex = 1;
            if (mifareClassic.authenticateSectorWithKeyA(sectorIndex, keyA)) {
                view.hideProgress();
            } else {
                view.hideProgress();
                view.showError(R.string.error_auth);
            }
        } catch (IOException e) {
            view.hideProgress();
            view.showError(R.string.error_reading_tag);
        }

    }

    public interface View extends Presenter.View {

    }
}
