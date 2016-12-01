package com.nosmurf.shk.internal.di.module;

import com.nosmurf.domain.usecase.DoLoginUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @PerActivity
    @Named("doLoginUseCase")
    public UseCase provideDoLoginUseCase(DoLoginUseCase doLoginUseCase) {
        return doLoginUseCase;
    }

}
