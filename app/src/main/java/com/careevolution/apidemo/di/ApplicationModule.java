package com.careevolution.apidemo.di;

import android.content.Context;
import android.content.res.Resources;

import com.careevolution.apidemo.ExampleApplication;

import net.openid.appauth.AuthorizationService;
import net.openid.appauthdemo.AuthStateManager;

import javax.inject.Singleton;

import ca.uhn.fhir.context.FhirContext;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
abstract class ApplicationModule {

    @Binds
    abstract Context provideContext(ExampleApplication exampleApplication);

    @Provides
    static Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    static AuthorizationService provideAuthorizationService(Context context) {
        return new AuthorizationService(context);
    }

    @Provides
    @Reusable
    static FhirContext provideFhirContext() {
        return FhirContext.forDstu3();
    }

    @Provides
    @Singleton
    static AuthStateManager provideAuthStateManager(Context context) {
        return AuthStateManager.getInstance(context);
    }

}
