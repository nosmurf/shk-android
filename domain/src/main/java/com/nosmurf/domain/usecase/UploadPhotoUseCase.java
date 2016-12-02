package com.nosmurf.domain.usecase;

import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Daniel on 01/12/2016.
 */

public class UploadPhotoUseCase extends UseCase {

    Repository repository;

    private String imagePath;

    @Inject
    public UploadPhotoUseCase(Repository repository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.repository = repository;
    }

    public void execute(String imagePath, Subscriber<Void> subscriber) {
        this.imagePath = imagePath;

        super.execute(subscriber);
    }

    @Override
    protected Observable getObservable() {
        return repository.uploadPhoto(imagePath);
    }
}
