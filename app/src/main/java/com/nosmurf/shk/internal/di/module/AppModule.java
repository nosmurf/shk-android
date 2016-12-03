package com.nosmurf.shk.internal.di.module;

import android.content.Context;

import com.nosmurf.data.repository.DataRepository;
import com.nosmurf.data.repository.firebase.FirebaseDataSource;
import com.nosmurf.data.repository.firebase.SHKFirebaseDataSource;
import com.nosmurf.data.repository.network.NetworkDataSource;
import com.nosmurf.data.repository.network.SHKNetworkDataSource;
import com.nosmurf.data.repository.persistence.PersistenceDataSource;
import com.nosmurf.data.repository.persistence.SHKPersistenceDataSource;
import com.nosmurf.domain.executor.PostExecutionThread;
import com.nosmurf.domain.repository.Repository;
import com.nosmurf.shk.UIThread;
import com.nosmurf.shk.view.SecurityHomeKeyApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private SecurityHomeKeyApplication securityHomeKeyApplication;

    public AppModule(SecurityHomeKeyApplication securityHomeKeyApplication) {
        this.securityHomeKeyApplication = securityHomeKeyApplication;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return this.securityHomeKeyApplication;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    Repository provideRepository(DataRepository dataRepository) {
        return dataRepository;
    }

    @Provides
    @Singleton
    FirebaseDataSource provideFirebaseDataSource(SHKFirebaseDataSource shkFirebaseDataSource) {
        return shkFirebaseDataSource;
    }

    @Provides
    @Singleton
    NetworkDataSource provideNetworkDataSource(SHKNetworkDataSource shkNetworkDataSource) {
        return shkNetworkDataSource;
    }

    @Provides
    @Singleton
    PersistenceDataSource providePersistenceDataSource(SHKPersistenceDataSource shkPersistenceDataSource) {
        return shkPersistenceDataSource;
    }

}
