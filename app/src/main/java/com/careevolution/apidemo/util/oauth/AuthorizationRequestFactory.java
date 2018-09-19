package com.careevolution.apidemo.util.oauth;

import android.content.res.Resources;
import android.net.Uri;

import com.careevolution.apidemo.R;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import javax.inject.Inject;

public class AuthorizationRequestFactory {

    private Resources resources;

    @Inject
    public AuthorizationRequestFactory(Resources resources) {
        this.resources = resources;
    }

    public AuthorizationRequest create(AuthorizationServiceConfiguration authorizationServiceConfiguration) {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                authorizationServiceConfiguration,
                resources.getString(R.string.OAuth_clientId),
                ResponseTypeValues.CODE,
                Uri.parse(resources.getString(R.string.OAuth_redirectUri))
        );

        return builder
                .setScope(resources.getString(R.string.OAuth_scope))
                .setCodeVerifier(null)
                .build();
    }

}
