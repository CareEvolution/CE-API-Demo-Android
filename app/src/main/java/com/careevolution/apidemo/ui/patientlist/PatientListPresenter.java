package com.careevolution.apidemo.ui.patientlist;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.util.Log;

import com.careevolution.apidemo.R;
import com.careevolution.apidemo.util.fhir.IGenericClientFactory;

import net.openid.appauth.AuthState;
import net.openid.appauthdemo.AuthStateManager;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class PatientListPresenter implements DefaultLifecycleObserver {

    private static final String TAG = PatientListPresenter.class.getName();

    private AuthStateManager authStateManager;
    private ExecutorService executorService;
    private IGenericClientFactory clientFactory;
    private PatientListActivity patientListActivity;

    private Bundle results;
    private AtomicBoolean working = new AtomicBoolean(false);

    @Inject
    public PatientListPresenter(AuthStateManager authStateManager, ExecutorService executorService, IGenericClientFactory clientFactory, PatientListActivity patientListActivity) {
        this.authStateManager = authStateManager;
        this.executorService = executorService;
        this.clientFactory = clientFactory;
        this.patientListActivity = patientListActivity;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        onQuery();
    }

    void onRefresh() {
        onQuery();
    }

    private void onQuery() {
        if (working.getAndSet(true)) {
            return;
        }

        patientListActivity.clearPatients();
        patientListActivity.showLoadingIndicator();
        executorService.submit(() -> {
            try {
                IGenericClient client = clientFactory.createClientWithFreshTokens().get();
                results = client.search()
                        .forResource(Patient.class)
                        .returnBundle(Bundle.class)
                        .execute();
                processResultsBundle(results);
            } catch (Exception e) {
                handleRefreshTokenError(e);
            }
        });
    }

    void onPaginate() {
        if (working.getAndSet(true) || results.getLink(Bundle.LINK_NEXT) == null) {
            return;
        }

        patientListActivity.showLoadingIndicator();
        executorService.submit(() -> {
            try {
                IGenericClient client = clientFactory.createClientWithFreshTokens().get();
                results = client.loadPage()
                        .next(results)
                        .execute();
                processResultsBundle(results);
            } catch (Exception e) {
                handleRefreshTokenError(e);
            }
        });
    }

    private void handleRefreshTokenError(Exception e) {
        Log.d(TAG, "Error refreshing tokens.", e);
        patientListActivity.showMessage(R.string.PatientListActivity_tokenRefreshError);
        onLogout();
    }

    private void processResultsBundle(Bundle b) {
        List<Patient> patients = new ArrayList<>(b.getEntry().size());
        for (Bundle.BundleEntryComponent bundleEntryComponent : b.getEntry()) {
            patients.add((Patient) bundleEntryComponent.getResource());
        }
        patientListActivity.addPatients(patients);
        patientListActivity.hideLoadingIndicator();
        working.set(false);
    }

    void onLogout() {
        authStateManager.replace(new AuthState());
        patientListActivity.navigateToAuthorizationActivity();
    }

}
