package com.careevolution.apidemo.ui.authorization;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.careevolution.apidemo.R;
import com.careevolution.apidemo.util.oauth.AuthorizationRequestFactory;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;
import net.openid.appauthdemo.AuthStateManager;

import javax.inject.Inject;

public class AuthorizationPresenter implements DefaultLifecycleObserver {

    private static final String TAG = AuthorizationPresenter.class.getName();
    private static final int RC_AUTHORIZATION_REQUEST = 0;

    private AuthorizationActivity authorizationActivity;
    private AuthorizationService authorizationService;
    private AuthorizationRequestFactory authorizationRequestFactory;
    private AuthStateManager authStateManager;
    private Resources resources;

    @Inject
    public AuthorizationPresenter(AuthorizationActivity authorizationActivity, AuthorizationService authorizationService, AuthorizationRequestFactory authorizationRequestFactory, AuthStateManager authStateManager, Resources resources) {
        this.authorizationActivity = authorizationActivity;
        this.authorizationService = authorizationService;
        this.authorizationRequestFactory = authorizationRequestFactory;
        this.authStateManager = authStateManager;
        this.resources = resources;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        if (authStateManager.getCurrent().isAuthorized()) {
            authorizationActivity.navigateToQueryActivity();
        }
    }

    void onConnectButtonClick() {
        authorizationActivity.showLoadingIndicator();
        AuthorizationServiceConfiguration.fetchFromUrl(
                Uri.parse(resources.getString(R.string.OAuth_discoveryDocument)),
                this::onFetchConfigurationComplete
        );
    }

    private void onFetchConfigurationComplete(@Nullable AuthorizationServiceConfiguration serviceConfiguration, @Nullable AuthorizationException ex) {
        if (serviceConfiguration != null) {
            Intent intent = authorizationService.getAuthorizationRequestIntent(authorizationRequestFactory.create(serviceConfiguration));
            authorizationActivity.startActivityForResult(intent, RC_AUTHORIZATION_REQUEST);
        } else {
            authorizationActivity.hideLoadingIndicator();
            if (ex != null) {
                Log.e(TAG, "Error fetching service configuration: code=" + ex.code + ", error=" + ex.errorDescription, ex);
            } else {
                Log.e(TAG, "Error fetching service configuration.");
            }
            authorizationActivity.showMessage(R.string.AuthorizationActivity_configurationError);
        }
    }

    void onActivityResult(int requestCode, Intent data) {
        if (requestCode != RC_AUTHORIZATION_REQUEST) {
            return;
        }

        AuthorizationResponse authorizationResponse = AuthorizationResponse.fromIntent(data);
        AuthorizationException authorizationException = AuthorizationException.fromIntent(data);
        authStateManager.updateAfterAuthorization(authorizationResponse, authorizationException);

        if (authorizationResponse != null) {
            authorizationService.performTokenRequest(
                    authorizationResponse.createTokenExchangeRequest(),
                    this::onTokenRequestComplete
            );
        } else {
            authorizationActivity.hideLoadingIndicator();
            if (authorizationException != null) {
                Log.e(TAG, "Auth code request failed: code=" + authorizationException.code + ", error=" + authorizationException.errorDescription, authorizationException);
            } else {
                Log.e(TAG, "Auth code request failed.");
            }
            authorizationActivity.showMessage(R.string.AuthorizationActivity_authorizationError);
        }
    }

    private void onTokenRequestComplete(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException tokenException) {
        authStateManager.updateAfterTokenResponse(tokenResponse, tokenException);

        if (authStateManager.getCurrent().isAuthorized()) {
            authorizationActivity.navigateToQueryActivity();
        } else {
            authorizationActivity.hideLoadingIndicator();
            if (tokenException != null) {
                Log.e(TAG, "Token exchange failed: error=" + tokenException.error + ", description=" + tokenException.errorDescription, tokenException);
            } else {
                Log.e(TAG, "Token exchange failed.");
            }
            authorizationActivity.showMessage(R.string.AuthorizationActivity_tokenError);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        authorizationService.dispose();
    }
}
