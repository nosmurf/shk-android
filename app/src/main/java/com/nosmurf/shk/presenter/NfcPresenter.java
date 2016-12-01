package com.nosmurf.shk.presenter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;

import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Inject;

/**
 * Created by Sergio on 01/12/2016.
 */

public class NfcPresenter extends Presenter<NfcPresenter.View> {

    private NfcAdapter adapter;

    private PendingIntent pendingIntent;

    private IntentFilter[] filters;

    private String[][] techListsArray;

    @Inject
    public NfcPresenter() {
        super();
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

    public interface View extends Presenter.View {

    }
}
