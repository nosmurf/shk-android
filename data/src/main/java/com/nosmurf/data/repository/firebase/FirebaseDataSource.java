package com.nosmurf.data.repository.firebase;

import rx.Observable;

public interface FirebaseDataSource {

    Observable<Boolean> uploadPhoto(String imagePath);

}
