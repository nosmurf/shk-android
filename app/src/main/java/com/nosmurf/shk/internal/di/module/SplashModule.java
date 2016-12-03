package com.nosmurf.shk.internal.di.module;

import com.nosmurf.domain.usecase.HasCurrentUserUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sergio on 02/12/2016.
 */
@Module
public class SplashModule {
    @Provides
    @PerActivity
    @Named("hasCurrentUserUseCase")
    UseCase provideHasCurrentUserUseCase(HasCurrentUserUseCase hasCurrentUserUseCase) {
        return hasCurrentUserUseCase;
    }
}
