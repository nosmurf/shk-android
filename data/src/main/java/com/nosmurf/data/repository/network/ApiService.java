package com.nosmurf.data.repository.network;

import com.nosmurf.data.model.FaceDto;
import com.nosmurf.data.model.PersistedFaceDtoResponse;
import com.nosmurf.data.model.PersonGroupDto;
import com.nosmurf.data.model.UserDto;
import com.nosmurf.data.model.UserRegisteredDtoResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    String END_POINT = "https://api.projectoxford.ai/face/v1.0/";

    String API_KEY = "efd5a218d049401dacd34f047bcbe485";

    @Headers({
            "Content-type: application/json",
            "Ocp-Apim-Subscription-Key: " + API_KEY
    })
    @PUT("persongroups/{personGroupId}")
    Observable<Void> createPersonGroup(@Path("personGroupId") String personGroupId, @Body PersonGroupDto name);

    @Headers({
            "Content-type: application/json",
            "Ocp-Apim-Subscription-Key: " + API_KEY
    })
    @POST("persongroups/{personGroupId}/persons")
    Observable<UserRegisteredDtoResponse> createPerson(@Path("personGroupId") String personGroupId, @Body UserDto name);

    @Headers({
            "Content-type: application/json",
            "Ocp-Apim-Subscription-Key: " + API_KEY
    })
    @POST("persongroups/{personGroupId}/persons/{personId}/persistedFaces")
    Observable<PersistedFaceDtoResponse> addFace(@Path("personGroupId") String personGroupId, @Path("personId") String personId, @Body FaceDto faceDto);


    @Headers({
            "Ocp-Apim-Subscription-Key: " + API_KEY
    })
    @POST("persongroups/{personGroupId}/train")
    Observable<Void> trainPersonGroup(@Path("personGroupId") String personGroupId);


}
