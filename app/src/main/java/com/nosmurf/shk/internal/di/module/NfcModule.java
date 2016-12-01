package com.nosmurf.shk.internal.di.module;

import com.nosmurf.domain.usecase.GetHashedAuthTokenUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Sergio on 30/11/2016.
 */
@Module
public class NfcModule {
    @Provides
    @PerActivity
    @Named("getHashedTokenUseCase")
    UseCase provideGetHashedTokenUseCase(GetHashedAuthTokenUseCase getHashedAuthTokenUseCase) {
        return getHashedAuthTokenUseCase;
    }
}
