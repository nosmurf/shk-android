package com.nosmurf.domain.usecase;

import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Sergio on 30/11/2016.
 */

public class GetHashedAuthTokenUseCase extends UseCase {

    private final Repository repository;

    @Inject
    public GetHashedAuthTokenUseCase(Repository repository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable getObservable() {
        return repository.getHashedToken();
    }
}
