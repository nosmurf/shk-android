package com.nosmurf.data.repository.network;

import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.UserRegisteredDtoResponse;

import rx.Observable;

public interface NetworkDataSource {

    Observable<UserRegisteredDtoResponse> createPersonOnMicrosoftFaceAPI(String personGroupId, String name);

    Observable<Void> addFaceOnMicrosoftFaceAPI(ImageReference imageReference);

}
