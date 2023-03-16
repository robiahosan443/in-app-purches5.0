package com.unity3d.services.ads;

import android.app.Activity;
import android.content.Context;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.IUnityAdsTokenListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsLoadOptions;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.UnityServices;
import com.unity3d.services.ads.operation.load.LoadOperationState;
import com.unity3d.services.ads.operation.load.LoadModule;
import com.unity3d.services.ads.operation.show.ShowOperationState;
import com.unity3d.services.ads.operation.show.ShowModule;
import com.unity3d.services.ads.token.AsyncTokenStorage;
import com.unity3d.services.ads.token.TokenStorage;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.bridge.WebViewBridgeInvoker;

public final class UnityAdsImplementation {
	private static Configuration configuration = null;
	private static WebViewBridgeInvoker webViewBridgeInvoker = new WebViewBridgeInvoker();

	/**
	 * Initializes Unity Ads. Unity Ads should be initialized when app starts.
	 *
	 * @param context Current Android application context of calling app
	 * @param gameId Unique identifier for a game, given by Unity Ads admin tools or Unity editor
	 * @param initializationListener Listener for IUnityAdsInitializationListener callbacks
	 */
	public static void initialize(final Context context, final String gameId, final IUnityAdsInitializationListener initializationListener) {
		boolean testMode = false;
		initialize(context, gameId, testMode, initializationListener);
	}

	/**
	 * Initializes Unity Ads. Unity Ads should be initialized when app starts.
	 *
	 * @param context Current Android application context of calling app
	 * @param gameId Unique identifier for a game, given by Unity Ads admin tools or Unity editor
	 * @param testMode If true, only test ads are shown
	 */
	public static void initialize(final Context context, final String gameId, final boolean testMode) {
		initialize(context, gameId, testMode, null);
	}

	/**
	 * Initializes Unity Ads. Unity Ads should be initialized when app starts.
	 *  @param context Current Android application context of calling app
	 * @param gameId Unique identifier for a game, given by Unity Ads admin tools or Unity editor
	 * @param testMode If true, only test ads are shown
	 * @param initializationListener Listener for IUnityAdsInitializationListener callbacks
	 */
	public static void initialize(final Context context, final String gameId, final boolean testMode, final IUnityAdsInitializationListener initializationListener) {
		DeviceLog.entered();

		UnityServices.initialize(context, gameId, testMode, initializationListener);
	}

	/**
	 * Checks if Unity Ads has been initialized. This might be useful for debugging initialization problems.
	 *
	 * @return If true, Unity Ads has been successfully initialized
	 */
	public static boolean isInitialized() {
		return UnityServices.isInitialized();
	}

	/**
	 * Checks if current device supports running Unity Ads
	 *
	 * @return If true, device supports Unity Ads. If false, device can't initialize or show Unity Ads.
	 */
	public static boolean isSupported() {
		return UnityServices.isSupported();
	}

	/**
	 * Get current SDK version
	 *
	 * @return Current SDK version name
	 */
	public static String getVersion() {
		return UnityServices.getVersion();
	}

	/**
	 * Show one advertisement with custom placement.
	 *
	 * @param activity Current Android activity of calling app
	 * @param placementId Placement, as defined in Unity Ads admin tools
	 */
	public static void show(final Activity activity, final String placementId) {
		show(activity, placementId, new UnityAdsShowOptions(), null);
	}

	/**
	 * Show one advertisement with custom placement.
	 *
	 * @param activity Current Android activity of calling app
	 * @param placementId Placement, as defined in Unity Ads admin tools
	 * @param showListener Listener for IUnityAdsShowListener callbacks
	 */
	public static void show(final Activity activity, final String placementId, final IUnityAdsShowListener showListener) {
		show(activity, placementId, new UnityAdsShowOptions(), showListener);
	}

	/**
	 * Show one advertisement with custom placement and custom options.
	 *
	 * @param activity Current Android activity of calling app
	 * @param placementId Placement, as defined in Unity Ads admin tools
	 * @param showOptions Custom options
	 * @param showListener Listener for IUnityAdsShowListener callbacks
	 */
	public static void show(final Activity activity, final String placementId, final UnityAdsShowOptions showOptions, final IUnityAdsShowListener showListener) {
		if (!isSupported()) {
			String showErrorMessage = "Unity Ads is not supported for this device";
			handleShowError(showListener, placementId, UnityAds.UnityAdsShowError.NOT_INITIALIZED, showErrorMessage);
			return;
		}
		if(!isInitialized()) {
			String showErrorMessage = "Unity Ads is not initialized";
			handleShowError(showListener, placementId, UnityAds.UnityAdsShowError.NOT_INITIALIZED, showErrorMessage);
			return;
		}
		if(activity == null) {
			String showErrorMessage = "Activity must not be null";
			handleShowError(showListener, placementId, UnityAds.UnityAdsShowError.INVALID_ARGUMENT, showErrorMessage);
			return;
		}
		Configuration config = configuration == null ? new Configuration() : configuration;
		ClientProperties.setActivity(activity);
		ShowModule.getInstance().executeAdOperation(WebViewApp.getCurrentApp(), new ShowOperationState(placementId, showListener, activity, showOptions, config));
	}

	private static void handleShowError(IUnityAdsShowListener showListener, String placementId, UnityAds.UnityAdsShowError error, String message) {
		if (showListener == null) return;
		showListener.onUnityAdsShowFailure(placementId, error, message);
	}

	/**
	 * Toggles debug mode on/off
	 *
	 * @param debugMode If true, debug mode is on and there will be lots of debug output from Unity Ads. If false, there will be only some short log messages from Unity Ads.
	 */
	public static void setDebugMode(boolean debugMode) {
		UnityServices.setDebugMode(debugMode);
	}

	/**
	 * Get current debug mode status
	 *
	 * @return If true, debug mode is on. If false, debug mode is off.
	 */
	public static boolean getDebugMode() {
		return UnityServices.getDebugMode();
	}

	public static void load(final String placementId, final UnityAdsLoadOptions loadOptions, final IUnityAdsLoadListener listener) {
		Configuration config = configuration == null ? new Configuration() : configuration;
		LoadModule.getInstance().executeAdOperation(webViewBridgeInvoker, new LoadOperationState(placementId, listener, loadOptions, config));
	}

	public static String getToken() {
		return TokenStorage.getToken();
	}

	public static void getToken(IUnityAdsTokenListener listener) {
		if (listener == null) {
			DeviceLog.info("Please provide non-null listener to UnityAds.GetToken method");
			return;
		}
		if (ClientProperties.getApplicationContext() == null) {
			listener.onUnityAdsTokenReady(null);
			return;
		}
		AsyncTokenStorage.getInstance().getToken(listener);
	}

	public static void setConfiguration(Configuration configuration) {
		UnityAdsImplementation.configuration = configuration;
	}
}
