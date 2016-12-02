package com.nosmurf.data.repository.network;

import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonReference;

import rx.Observable;

public interface NetworkDataSource {

    Observable<String> createPersonOnMicrosoftFaceAPI(PersonReference personReference);

    Observable<Void> addFaceOnMicrosoftFaceAPI(ImageReference imageReference);

}
