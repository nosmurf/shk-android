package com.nosmurf.shk.internal.di.component;

import com.nosmurf.shk.internal.di.PerActivity;
import com.nosmurf.shk.internal.di.module.MainModule;
import com.nosmurf.shk.view.activity.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
