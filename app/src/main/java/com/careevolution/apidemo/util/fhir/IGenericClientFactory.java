package com.careevolution.apidemo.util.fhir;

import android.content.res.Resources;

import com.careevolution.apidemo.R;

import net.openid.appauth.AuthorizationService;
import net.openid.appauthdemo.AuthStateManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.inject.Inject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

public class IGenericClientFactory {

    private AuthStateManager authStateManager;
    private AuthorizationService authorizationService;
    private FhirContext fhirContext;
    private Resources resources;

    @Inject
    public IGenericClientFactory(AuthStateManager authStateManager, AuthorizationService authorizationService, FhirContext fhirContext, Resources resources) {
        this.authStateManager = authStateManager;
        this.authorizationService = authorizationService;
        this.fhirContext = fhirContext;
        this.resources = resources;
    }

    public Future<IGenericClient> createClientWithFreshTokens() {
        CompletableFuture<IGenericClient> future = new CompletableFuture<>();

        authStateManager.getCurrent().performActionWithFreshTokens(authorizationService, (accessToken, idToken, ex) -> {
            if (ex == null) {
                String serverBase = resources.getString(R.string.FHIR_serverBase);
                IGenericClient client = fhirContext.newRestfulGenericClient(serverBase);
                client.registerInterceptor(new LoggingInterceptor(true));
                client.registerInterceptor(new BearerTokenAuthInterceptor(accessToken));
                future.complete(client);
            } else {
                future.completeExceptionally(ex);
            }
        });

        return future;
    }

    @Override
    protected void finalize() throws Throwable {
        authorizationService.dispose();
        super.finalize();
    }
}
