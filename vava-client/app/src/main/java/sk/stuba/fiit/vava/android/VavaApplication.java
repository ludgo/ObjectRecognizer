package sk.stuba.fiit.vava.android;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import lombok.Getter;
import sk.stuba.fiit.vava.android.db.TemporaryUrlProvider;

/**
 * Android application
 */
public class VavaApplication extends MultiDexApplication {

    @Getter
    private static VavaApplication instance;
    @Getter
    private TemporaryUrlProvider temporaryUrlProvider;

    public void onCreate() {
        super.onCreate();
        // Keep reference to application instance
        instance = this;

        initDatabase();
    }

    /**
     * Reference application context within non-component classes
     * @return Application context
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    /**
     * Initialize auto-managed database providers here
     */
    private void initDatabase() {
        temporaryUrlProvider = new TemporaryUrlProvider(this);
    }

    /**
     * Release cached database providers
     */
    private void closeDatabase() {
        temporaryUrlProvider.release();
        temporaryUrlProvider = null;
    }

    @Override
    public void onTerminate() {
        // Bind database access to application lifecycle
        closeDatabase();
        super.onTerminate();
    }
}
