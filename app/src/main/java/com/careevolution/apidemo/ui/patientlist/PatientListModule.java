package com.careevolution.apidemo.ui.patientlist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class PatientListModule {

    @Provides
    static ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

}
