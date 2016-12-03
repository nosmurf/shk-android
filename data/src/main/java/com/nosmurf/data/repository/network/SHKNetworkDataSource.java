package com.nosmurf.data.repository.network;

import com.nosmurf.data.model.FaceDto;
import com.nosmurf.data.model.ImageReference;
import com.nosmurf.data.model.PersonGroupDto;
import com.nosmurf.data.model.PersonReference;
import com.nosmurf.data.model.UserDto;
import com.nosmurf.data.model.UserRegisteredDtoResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Singleton
public class SHKNetworkDataSource implements NetworkDataSource {

    private ApiService apiService;

    @Inject
    public SHKNetworkDataSource() {
        apiService = ApiClient.createRetrofitService(ApiService.class, ApiService.END_POINT);
    }

    @Override
    public Observable<String> createGroupOnMicrosoftFaceAPI(String groupId) {
        String groupIdLowerCase = groupId.toLowerCase();
        return apiService.createPersonGroup(groupIdLowerCase, new PersonGroupDto(groupId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        return Observable.just(groupIdLowerCase);
                    }
                });
    }

    @Override
    public Observable<String> createPersonOnMicrosoftFaceAPI(PersonReference personReference) {
        return apiService.createPerson(personReference.getGroupId(), new UserDto(personReference.getCurrentUser()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(UserRegisteredDtoResponse::getPersonId);
    }

    @Override
    public Observable<Void> addFaceOnMicrosoftFaceAPI(ImageReference imageReference) {
        return apiService.addFace(imageReference.getGroupId(), imageReference.getPersonId(),
                new FaceDto(imageReference.getImageUrl()))
                .flatMap(persistedFaceDtoResponse -> Observable.empty());
    }
}
