package sk.stuba.fiit.vava.android.db.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Database entity representing temporary url entry
 */
@NoArgsConstructor
@DatabaseTable(tableName = TemporaryUrl.TABLE_NAME)
public class TemporaryUrl {

    public static final String TABLE_NAME = "temporary_url";
    // PK
    public static final String COLUMN_ID = "id";
    // Image url
    public static final String COLUMN_URL = "url";
    // Firebase user ID
    public static final String COLUMN_UID = "uid";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    int id;

    @DatabaseField(columnName = COLUMN_URL, canBeNull = false, uniqueCombo = true)
    @Getter
    String url;

    @DatabaseField(columnName = COLUMN_UID, canBeNull = false, uniqueCombo = true, index = true)
    String uid;

    public TemporaryUrl(String url, String uid) {
        this.url = url;
        this.uid = uid;
    }
}
