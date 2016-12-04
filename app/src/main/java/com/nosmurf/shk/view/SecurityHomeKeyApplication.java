package com.nosmurf.shk.view;

import android.app.Application;

import com.karumi.dexter.Dexter;
import com.nosmurf.shk.internal.di.component.AppComponent;
import com.nosmurf.shk.internal.di.component.DaggerAppComponent;
import com.nosmurf.shk.internal.di.module.AppModule;

public class SecurityHomeKeyApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Dexter.initialize(this);

        initializeInjector();
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
