package com.nosmurf.shk.internal.di.component;

import android.content.Context;

import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;
import com.nosmurf.shk.internal.di.module.AppModule;
import com.nosmurf.shk.view.activity.RootActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    void inject(RootActivity rootActivity);

    Context context();

    PostExecutionThread postExecutionThread();

    Repository repository();
}
