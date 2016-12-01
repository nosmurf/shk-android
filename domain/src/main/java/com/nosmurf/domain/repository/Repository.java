package com.nosmurf.domain.repository;

import rx.Observable;

public interface Repository {

    Observable<Boolean> uploadPhoto(String imagePath);

}
