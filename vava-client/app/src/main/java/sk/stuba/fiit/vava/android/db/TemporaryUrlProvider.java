package sk.stuba.fiit.vava.android.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sk.stuba.fiit.vava.android.db.entity.TemporaryUrl;

/**
 * Provider for required {@link TemporaryUrl} database operations
 */
public class TemporaryUrlProvider {

    private Context context;

    /**
     * Constructor
     * @param context Context using database for {@link TemporaryUrl}
     */
    public TemporaryUrlProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Initialize database access via helper and DAO
     */
    private RuntimeExceptionDao<TemporaryUrl, Integer> accessDatabase() {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        return databaseHelper.getTemporaryUrlDao();
    }

    /**
     * Release helper before end of Context lifecycle
     */
    public void release() {
        OpenHelperManager.releaseHelper();
    }

    /**
     * Save single row to database
     * @param url Image url
     * @param uid User ID
     */
    public void save(@NonNull String url, @NonNull String uid) {
        RuntimeExceptionDao<TemporaryUrl, Integer> temporaryUrlDao = accessDatabase();
        temporaryUrlDao.create(new TemporaryUrl(url, uid));
    }

    /**
     * Delete urls in list for particular user
     * @param urls Image urls
     * @param uid User ID
     */
    public void delete(@NonNull @Size(min=1) Set<String> urls, @NonNull String uid) {
        RuntimeExceptionDao<TemporaryUrl, Integer> temporaryUrlDao = accessDatabase();
        try {
            DeleteBuilder deleteBuilder = temporaryUrlDao.deleteBuilder();
            deleteBuilder.where().eq(TemporaryUrl.COLUMN_UID, uid)
                    .and().in(TemporaryUrl.COLUMN_URL, urls);
            temporaryUrlDao.delete(deleteBuilder.prepare());
        }
        catch (SQLException e) {
            Log.e(TemporaryUrlProvider.class.getName(), "Error deleting urls from database", e);
        }
    }

    /**
     * Get all urls for user
     * @param uid User ID
     * @return List of urls
     */
    public List<TemporaryUrl> find(@NonNull String uid) {
        RuntimeExceptionDao<TemporaryUrl, Integer> temporaryUrlDao = accessDatabase();
        List<TemporaryUrl> temporaryUrlList;
        try {
            temporaryUrlList = temporaryUrlDao.queryBuilder().where().eq(TemporaryUrl.COLUMN_UID, uid).query();
        }
        catch (SQLException e) {
            Log.e(TemporaryUrlProvider.class.getName(), "Error fetching urls from database", e);
            temporaryUrlList = new ArrayList<>();
        }
        return temporaryUrlList;
    }
}
