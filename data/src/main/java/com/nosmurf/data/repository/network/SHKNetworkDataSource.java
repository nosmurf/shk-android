package com.nosmurf.data.repository.network;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SHKNetworkDataSource implements NetworkDataSource {

    private ApiService apiService;

    @Inject
    public SHKNetworkDataSource() {
        apiService = ApiClient.createRetrofitService(ApiService.class, ApiService.END_POINT);
    }
}
