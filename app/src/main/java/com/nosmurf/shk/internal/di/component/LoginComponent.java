package com.nosmurf.shk.internal.di.component;

import com.nosmurf.shk.internal.di.PerActivity;
import com.nosmurf.shk.internal.di.module.LoginModule;
import com.nosmurf.shk.view.activity.LoginActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = LoginModule.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
