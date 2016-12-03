package com.nosmurf.shk.internal.di.component;

import com.nosmurf.shk.internal.di.PerActivity;
import com.nosmurf.shk.internal.di.module.NfcModule;
import com.nosmurf.shk.view.activity.NfcActivity;

import dagger.Component;

/**
 * Created by Sergio on 30/11/2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = NfcModule.class)
public interface NfcComponent {
    void inject(NfcActivity nfcActivity);
}
