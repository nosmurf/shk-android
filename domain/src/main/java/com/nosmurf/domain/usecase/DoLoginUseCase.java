package com.nosmurf.domain.usecase;

import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Sergio on 01/12/2016.
 */

public class DoLoginUseCase extends UseCase {

    private final Repository repository;

    @Inject
    public DoLoginUseCase(Repository repository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.repository = repository;
    }

    public void execute(){

    }

    @Override
    protected Observable getObservable() {
        return null;
    }
}
