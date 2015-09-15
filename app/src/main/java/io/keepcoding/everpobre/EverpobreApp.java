package io.keepcoding.everpobre;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

import io.keepcoding.everpobre.util.Constants;

public class EverpobreApp extends Application {
    @Nullable private static WeakReference<Context> staticContext;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Constants.appName, getString(R.string.everpobre_app_starting));
		staticContext = new WeakReference<Context>(getApplicationContext());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Nullable public static Context getAppContext() {
		if (staticContext != null) {
			return staticContext.get();
		}
		return null;
	}
}
