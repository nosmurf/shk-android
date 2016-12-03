package com.nosmurf.shk.internal.di.module;

import com.nosmurf.domain.usecase.UploadPhotoUseCase;
import com.nosmurf.domain.usecase.UseCase;
import com.nosmurf.shk.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    @PerActivity
    @Named("uploadPhotoUseCase")
    UseCase provideUploadPhotoUseCase(UploadPhotoUseCase uploadPhotoUseCase) {
        return uploadPhotoUseCase;
    }

}
