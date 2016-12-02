package com.nosmurf.data.repository.network;

import com.nosmurf.data.model.FaceDto;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.data.model.UserDto;
import com.nosmurf.data.model.UserRegisteredDtoResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SHKNetworkDataSource implements NetworkDataSource {

    private ApiService apiService;

    @Inject
    public SHKNetworkDataSource() {
        apiService = ApiClient.createRetrofitService(ApiService.class, ApiService.END_POINT);
    }

    @Override
    public Observable<String> createPersonOnMicrosoftFaceAPI(PersonReference personReference) {
        return apiService.createPerson(personReference.getGroupId(), new UserDto(personReference.getCurrentUser()))
                .map(UserRegisteredDtoResponse::getPersonId);
    }

    @Override
    public Observable<Void> addFaceOnMicrosoftFaceAPI(ImageReference imageReference) {
        return apiService.addFace(imageReference.getGroupId(), imageReference.getPersonId(),
                new FaceDto(imageReference.getImageUrl()))
                .flatMap(persistedFaceDtoResponse -> Observable.empty());
    }
}
