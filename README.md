# CareEvolution FHIR Interface - Android Demo

This sample Android app demonstrates authenticating and connecting to the [CareEvolution HIEBus&trade; FHIR Interface](https://fhir.docs.careevolution.com).

## Configuration

To use the app, you need to configure it to connect to your HIEBus server or to the [CareEvolution public test server](https://fhir.docs.careevolution.com/overview/test_server.html).

### Configuring an OAuth Client

Before configuring the app, you'll need an OAuth client ID, supplied by your site administrator.  

If using the CareEvolution public test server, [contact us](https://fhir.docs.careevolution.com/help.html) to register your client.

If using your own HIEBus server, your site administrator will need to set up an OAuth client as explained in the [FHIR Administration Guide](https://fhir.docs.careevolution.com/config/authentication.html).  Here are some specific settings to use for the demo app:

* Use `ceapidemo://oauth` for the Redirect URI. See [Custom URI Schemes, Deep Links, and App Links](#custom-uri-schemes-deep-links-and-app-links) below for more details.
* Check the box for **SMART app**.
* Set the **OpenID Connect flow** to `AuthorizationCode`.
* Add the following values to **Allowed scopes**:
      * `offline_access` - This enables refreshTokens ad the ability to renew an accessToken after it expires. This allows the app to remain logged-in between sessions.
      * `user/*.read` - This grants read access to the FHIR APIs
      * `api` - This grants access to all of CareEvolutions APIs

### Configuring Your Server Connection Info

Next, configure your Connection Info to connect to your server or a demo server:

The `config.xml` file is used to specify the OAuth connection parameters.  Before launching the app, be sure to edit the file and fill-in the following values:

```
<string name="OAuth_discoveryDocument">https://{your-domain-name}/{your-discovery-document}/</string>
<string name="OAuth_clientId">{OAuth clientID configured on the server}</string>
<string name="FHIR_serverBase">https://{your-careevolution-url}/{path-to-STU3-endpoint}</string>
```

NOTE: If you're using GitHub to fork this project, you should run the following command before committing any changes to your fork.  This will make git ignore your local changes:

```
$ git update-index --skip-worktree app/src/main/res/values/config.xml
```

This will prevent your private server information from being stored on the public GitHub servers should you choose to push your changes.

## Running The App

One you've completed the sections above you can launch the app in an emulator or on a device from Android Studio. You'll be prompted to log in to your HIEBus server. Enter the credentials for a user that has access to a list of patients and log in. The app will log in and download the complete list patients your user has access to.

## Custom URI Schemes, Deep Links, and App Links

The demo app is configured to use a [Deep Link](https://developer.android.com/training/app-links/#app-links-vs-deep-links) with a custom URI scheme.  This mechanism was chosen because it won't trigger a chooser dialog and works without server-side configuration.  Consider implementing a more robust [App Link](https://developer.android.com/training/app-links/#add-app-links) instead.

The default configuration uses `ceapidemo:path?parameter=value` for its URI scheme.  To change this:

1. Open `app/src/main/res/values/config.xml`.
2. Change the `scheme` entity to the prefix of your choice e.g. `ceapidemo`.

Whatever you set as the `scheme` entity will become the protocol of your custom url, e.g. `ceapidemo:path?parameter=value`.  Whenever a URL with that protocol is opened, it will be forwarded to the `RedirectUriReceiverActivity` defined in `AndroidManifest.xml`.

## 3rd Party Dependencies

* [AppAuth Android](https://github.com/openid/AppAuth-Android) - AppAuth for Android is a client SDK for communicating with [OAuth 2.0](https://tools.ietf.org/html/rfc6749) and [OpenID Connect](http://openid.net/specs/openid-connect-core-1_0.html) providers.
* [HAPI FHIR](http://hapifhir.io/) - HAPI FHIR is an Open Source implementation of the [FHIR](https://www.hl7.org/fhir/) specification in Java.
* [Dagger 2](https://google.github.io/dagger/) - Dagger is a fully static, compile-time [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) framework for both Java and Android.
* [ButterKnife](http://jakewharton.github.io/butterknife/) - Field and method binding for Android views which uses annotation processing to generate boilerplate code for you.
