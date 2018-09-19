# CE-API-Demo-Android

Example app for authenticating and connecting to CareEvolution's REST APIs

### Configuring a Client ID

Before running this application you need to be able to connect to an instance of HIEBus, to do this you need a Client ID configured on the server:

1. Log into the web application as an Admin user.
2. Select **Administration > Miscellaneous** from the top menu.
3. Select **Client Configuration** from the Miscellaneous Administration sub menu.
4. Choose **Login > OAuth2Clients* from the drop-down.
5. Press the **Add** button to add a new client.
6. Give yourself a unique **Id**, this is the Client ID, note this for configuring Connection Info later.
7. Set the **Client/Product name** You can use `CE-API-Demo-Android` or choose something else.
8. If desired, set a **Description**.
9. Set your **Redirect URIs** to match the configuration of your app. For this default app, use `ceapidemo://oauth`. See [Custom URI Schemes, Deep Links, and App Links](#custom-uri-schemes-deep-links-and-app-links) below for more details.
10. Set **Token expiration (minutes)** to a reasonable number, `30` is a good choice. This is the amount of time you can use an accessToken before it expires.
11. Set **Refresh token expiration (days)** to a reasonable number, `8` is a good choice. The refreshToken is used to generate a new accessToken after it expires. When the refreshToken is used a new one is returned with a new expiration. This number is essentially the number of days between sessions your app can go before require the user to login again. With a value of 7, if you use it every day it will remain logged-in forever; if you use it every week on Monday it will remain logged-in forever, or until you go on vacation and skip a week.
12. Check the box for **SMART app**.
13. Set the **OpenID Connect flow** to `AuthorizationCode`.
14. Add the following values to **Allowed scopes**:
      * `offline_access` - This enables refreshTokens ad the ability to renew an accessToken after it expires. This allows the app to remain logged-in between sessions.
      * `user/*.read` - This grants read access to the FHIR APIs
      * `api` - This grants access to all of CareEvolutions APIs
15. Press the **Save** button to save your changes and create the new Client ID

### Configuring Your Server Connection Info

Next, configure your Connection Info to connect to your server or a demo server:

The `config.xml` file is used to specify the OAuth connection parameters, before launching the app be sure to edit the file and fill-in the following values:

```
<string name="OAuth_discoveryDocument">https://{your-domain-name}/{your-discovery-document}/</string>
<string name="OAuth_clientId">{OAuth clientID configured on the server}</string>
<string name="FHIR_serverBase">https://{your-domain-name}/{path-to-STU3-endpoint}</string>
```

NOTE: Before commiting any changes to your fork, you should run the following command to have git ignore your local changes:

```
$ git update-index --skip-worktree app/src/main/res/values/config.xml
```

This will prevent your private server information from being stored on the public GitHub servers should you choose to push your changes.

### Running The App

One you've completed the sections above you can launch the app in an emulator or on a device from Android Studio. You'll be prompted to log in to your HIEBus server. Enter the credentials for a user that has access to a list of patients and log in. If all goes well, the app will log in and download the complete list patients your user has access to.

## Custom URI Schemes, Deep Links, and App Links
This application is configured to use a [Deep Link](https://developer.android.com/training/app-links/#app-links-vs-deep-links) with a custom URI scheme because it won't trigger a chooser and works without server-side configuration.  However, [App Links](https://developer.android.com/training/app-links/#add-app-links) are considered more secure. If the app you are developing will be running inside a secure network then a Deep Link with a custom URI scheme is probably fine. If your app is running on a public server or being distributed through Google Play it should probably use an App Link instead.

### Using Deep Links with a custom URI string
(When you check out this project it is already configured this way.)

1. Open `app/src/main/res/values/config.xml`.
2. Change the `scheme` entity to the prefix of your choice e.g. `ceapidemo`.

Whatever you set as the `scheme` entity will become the protocol of your custom url, e.g. `ceapidemo:path?parameter=value`, whenever a URL with that protocol is opened it will be forwarded to the `RedirectUriReceiverActivity` defined in `AndroidManifest.xml`.

### Using Deep Links

1. TBD...

### Using App Links

To use an App Link you must be able to serve a [Digital Asset Links](https://github.com/google/digitalassetlinks/blob/master/well-known/details.md) file from the domain used in the link.  The file must be served over HTTPS, with a 200 status code, and the correct `Content-Type` header.

```
https://domain.name/.well-known/assetlinks.json
```

The developer documentation contains information on [verifying app links](https://developer.android.com/training/app-links/verify-site-associations) and [creating a statement list](https://developers.google.com/digital-asset-links/v1/create-statement) for the asset links file.

1. TBD...

## 3rd Party Dependencies
* [AppAuth Android](https://github.com/openid/AppAuth-Android) - AppAuth for Android is a client SDK for communicating with [OAuth 2.0](https://tools.ietf.org/html/rfc6749) and [OpenID Connect](http://openid.net/specs/openid-connect-core-1_0.html) providers.
* [HAPI FHIR](http://hapifhir.io/) - HAPI FHIR is an Open Source implementation of the [FHIR](https://www.hl7.org/fhir/) specification in Java.
* [Dagger 2](https://google.github.io/dagger/) - Dagger is a fully static, compile-time [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) framework for both Java and Android.
* [ButterKnife](http://jakewharton.github.io/butterknife/) - Field and method binding for Android views which uses annotation processing to generate boilerplate code for you.
