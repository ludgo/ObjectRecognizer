package sk.stuba.fiit.vava.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import sk.stuba.fiit.vava.android.db.entity.TemporaryUrl;

/**
 * ORMLite simple SQLite database class based on ORM docs sample
 *
 * https://github.com/j256/ormlite-examples/blob/master/android/HelloAndroid/src/com/example/helloandroid/DatabaseHelper.java
 *
 * Database helper class used to manage the creation and upgrading database. This class also provides
 * DAOs used by other classes
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "vava_client.db";
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database access object
    private Dao<TemporaryUrl, Integer> temporaryUrlDao = null;
    private RuntimeExceptionDao<TemporaryUrl, Integer> temporaryUrlRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Create table statements here
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TemporaryUrl.class);
            Log.i(DatabaseHelper.class.getName(), "Database successfully created: " + DATABASE_NAME);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Database create failed: " + DATABASE_NAME , e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when application is upgraded and it has a higher version number.
     * It allows to adjust various data to match the new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // !!! remove drop to prevent data loss
            TableUtils.dropTable(connectionSource, TemporaryUrl.class, true);
            // Recreate
            onCreate(db, connectionSource);
            Log.i(DatabaseHelper.class.getName(), "Database successfully upgraded");
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Database upgrade failed", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Return Database Access Object (DAO) for {@link TemporaryUrl}.
     * It will create it or just give the cached value
     * Dao does throw RuntimeException
     */
    public Dao<TemporaryUrl, Integer> getDao() throws SQLException {
        if (temporaryUrlDao == null) {
            temporaryUrlDao = getDao(TemporaryUrl.class);
        }
        return temporaryUrlDao;
    }

    /**
     * Return RuntimeExceptionDao (Database Access Object) version of a Dao.
     * It will create it or just give the cached value.
     * RuntimeExceptionDao does NOT throw RuntimeException!
     */
    public RuntimeExceptionDao<TemporaryUrl, Integer> getTemporaryUrlDao() {
        if (temporaryUrlRuntimeDao == null) {
            temporaryUrlRuntimeDao = getRuntimeExceptionDao(TemporaryUrl.class);
        }
        return temporaryUrlRuntimeDao;
    }

    /**
     * Close database connections and clear any cached DAOs
     */
    @Override
    public void close() {
        super.close();
        temporaryUrlDao = null;
        temporaryUrlRuntimeDao = null;
    }
}
