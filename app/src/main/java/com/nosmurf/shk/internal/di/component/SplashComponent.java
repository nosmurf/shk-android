package com.nosmurf.shk.internal.di.component;

import com.nosmurf.shk.internal.di.PerActivity;
import com.nosmurf.shk.internal.di.module.SplashModule;
import com.nosmurf.shk.view.activity.SplashActivity;

import dagger.Component;

/**
 * Created by Sergio on 02/12/2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = SplashModule.class)
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
