package com.careevolution.apidemo.di;

import com.careevolution.apidemo.ExampleApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        // Injection Framework Modules
        AndroidSupportInjectionModule.class,

        // Global Modules
        ApplicationModule.class,

        // Android Components
        ActivityModule.class
})
public interface ApplicationComponent extends AndroidInjector<ExampleApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<ExampleApplication> {
    }
}
