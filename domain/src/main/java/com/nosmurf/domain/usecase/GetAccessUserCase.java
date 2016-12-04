package com.nosmurf.domain.usecase;

import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Daniel on 03/12/2016.
 */

public class GetAccessUserCase extends UseCase {

    Repository repository;

    @Inject
    public GetAccessUserCase(Repository repository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable getObservable() {
        return repository.getAccess();
    }

}
