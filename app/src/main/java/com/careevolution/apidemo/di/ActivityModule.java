package com.careevolution.apidemo.di;

import com.careevolution.apidemo.di.scopes.ActivityScope;
import com.careevolution.apidemo.ui.authorization.AuthorizationActivity;
import com.careevolution.apidemo.ui.authorization.AuthorizationModule;
import com.careevolution.apidemo.ui.patientlist.PatientListActivity;
import com.careevolution.apidemo.ui.patientlist.PatientListModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = AuthorizationModule.class)
    abstract AuthorizationActivity contributesAuthorizationActivityInjector();

    @ActivityScope
    @ContributesAndroidInjector(modules = PatientListModule.class)
    abstract PatientListActivity contributesQueryActivityInjector();

}
