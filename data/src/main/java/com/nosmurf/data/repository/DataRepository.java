package com.nosmurf.data.repository;

import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.domain.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DataRepository implements Repository {

    private FirebaseDataSource firebaseDataSource;

    @Inject
    public DataRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }
}
