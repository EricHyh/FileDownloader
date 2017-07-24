package com.eric.hyh.tools.download.internal.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.eric.hyh.tools.download.internal.db.bean.TaskDBInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TASK_DBINFO".
*/
public class TaskDBInfoDao extends AbstractDao<TaskDBInfo, Long> {

    public static final String TABLENAME = "TASK_DBINFO";

    /**
     * Properties of entity TaskDBInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ResKey = new Property(1, String.class, "resKey", false, "RES_KEY");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property CurrentStatus = new Property(3, Integer.class, "currentStatus", false, "CURRENT_STATUS");
        public final static Property Progress = new Property(4, Integer.class, "progress", false, "PROGRESS");
        public final static Property VersionCode = new Property(5, Integer.class, "versionCode", false, "VERSION_CODE");
        public final static Property ResponseCode = new Property(6, Integer.class, "responseCode", false, "RESPONSE_CODE");
        public final static Property RangeNum = new Property(7, Integer.class, "rangeNum", false, "RANGE_NUM");
        public final static Property TotalSize = new Property(8, Long.class, "totalSize", false, "TOTAL_SIZE");
        public final static Property CurrentSize = new Property(9, Long.class, "currentSize", false, "CURRENT_SIZE");
        public final static Property Time = new Property(10, Long.class, "time", false, "TIME");
        public final static Property PackageName = new Property(11, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property FilePath = new Property(12, String.class, "filePath", false, "FILE_PATH");
        public final static Property Expand = new Property(13, String.class, "expand", false, "EXPAND");
        public final static Property WifiAutoRetry = new Property(14, Boolean.class, "wifiAutoRetry", false, "WIFI_AUTO_RETRY");
        public final static Property TagJson = new Property(15, String.class, "tagJson", false, "TAG_JSON");
        public final static Property TagClassName = new Property(16, String.class, "tagClassName", false, "TAG_CLASS_NAME");
    }


    public TaskDBInfoDao(DaoConfig config) {
        super(config);
    }
    
    public TaskDBInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TASK_DBINFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"RES_KEY\" TEXT UNIQUE ," + // 1: resKey
                "\"URL\" TEXT," + // 2: url
                "\"CURRENT_STATUS\" INTEGER," + // 3: currentStatus
                "\"PROGRESS\" INTEGER," + // 4: progress
                "\"VERSION_CODE\" INTEGER," + // 5: versionCode
                "\"RESPONSE_CODE\" INTEGER," + // 6: responseCode
                "\"RANGE_NUM\" INTEGER," + // 7: rangeNum
                "\"TOTAL_SIZE\" INTEGER," + // 8: totalSize
                "\"CURRENT_SIZE\" INTEGER," + // 9: currentSize
                "\"TIME\" INTEGER," + // 10: time
                "\"PACKAGE_NAME\" TEXT," + // 11: packageName
                "\"FILE_PATH\" TEXT," + // 12: filePath
                "\"EXPAND\" TEXT," + // 13: expand
                "\"WIFI_AUTO_RETRY\" INTEGER," + // 14: wifiAutoRetry
                "\"TAG_JSON\" TEXT," + // 15: tagJson
                "\"TAG_CLASS_NAME\" TEXT);"); // 16: tagClassName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TASK_DBINFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TaskDBInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String resKey = entity.getResKey();
        if (resKey != null) {
            stmt.bindString(2, resKey);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        Integer currentStatus = entity.getCurrentStatus();
        if (currentStatus != null) {
            stmt.bindLong(4, currentStatus);
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(5, progress);
        }
 
        Integer versionCode = entity.getVersionCode();
        if (versionCode != null) {
            stmt.bindLong(6, versionCode);
        }
 
        Integer responseCode = entity.getResponseCode();
        if (responseCode != null) {
            stmt.bindLong(7, responseCode);
        }
 
        Integer rangeNum = entity.getRangeNum();
        if (rangeNum != null) {
            stmt.bindLong(8, rangeNum);
        }
 
        Long totalSize = entity.getTotalSize();
        if (totalSize != null) {
            stmt.bindLong(9, totalSize);
        }
 
        Long currentSize = entity.getCurrentSize();
        if (currentSize != null) {
            stmt.bindLong(10, currentSize);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(11, time);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(12, packageName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(13, filePath);
        }
 
        String expand = entity.getExpand();
        if (expand != null) {
            stmt.bindString(14, expand);
        }
 
        Boolean wifiAutoRetry = entity.getWifiAutoRetry();
        if (wifiAutoRetry != null) {
            stmt.bindLong(15, wifiAutoRetry ? 1L: 0L);
        }
 
        String tagJson = entity.getTagJson();
        if (tagJson != null) {
            stmt.bindString(16, tagJson);
        }
 
        String tagClassName = entity.getTagClassName();
        if (tagClassName != null) {
            stmt.bindString(17, tagClassName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TaskDBInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String resKey = entity.getResKey();
        if (resKey != null) {
            stmt.bindString(2, resKey);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        Integer currentStatus = entity.getCurrentStatus();
        if (currentStatus != null) {
            stmt.bindLong(4, currentStatus);
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(5, progress);
        }
 
        Integer versionCode = entity.getVersionCode();
        if (versionCode != null) {
            stmt.bindLong(6, versionCode);
        }
 
        Integer responseCode = entity.getResponseCode();
        if (responseCode != null) {
            stmt.bindLong(7, responseCode);
        }
 
        Integer rangeNum = entity.getRangeNum();
        if (rangeNum != null) {
            stmt.bindLong(8, rangeNum);
        }
 
        Long totalSize = entity.getTotalSize();
        if (totalSize != null) {
            stmt.bindLong(9, totalSize);
        }
 
        Long currentSize = entity.getCurrentSize();
        if (currentSize != null) {
            stmt.bindLong(10, currentSize);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(11, time);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(12, packageName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(13, filePath);
        }
 
        String expand = entity.getExpand();
        if (expand != null) {
            stmt.bindString(14, expand);
        }
 
        Boolean wifiAutoRetry = entity.getWifiAutoRetry();
        if (wifiAutoRetry != null) {
            stmt.bindLong(15, wifiAutoRetry ? 1L: 0L);
        }
 
        String tagJson = entity.getTagJson();
        if (tagJson != null) {
            stmt.bindString(16, tagJson);
        }
 
        String tagClassName = entity.getTagClassName();
        if (tagClassName != null) {
            stmt.bindString(17, tagClassName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TaskDBInfo readEntity(Cursor cursor, int offset) {
        TaskDBInfo entity = new TaskDBInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // resKey
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // currentStatus
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // progress
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // versionCode
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // responseCode
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // rangeNum
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // totalSize
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // currentSize
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // time
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // packageName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // filePath
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // expand
            cursor.isNull(offset + 14) ? null : cursor.getShort(offset + 14) != 0, // wifiAutoRetry
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // tagJson
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // tagClassName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TaskDBInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setResKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCurrentStatus(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setProgress(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setVersionCode(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setResponseCode(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setRangeNum(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setTotalSize(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setCurrentSize(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setTime(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setPackageName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFilePath(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setExpand(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setWifiAutoRetry(cursor.isNull(offset + 14) ? null : cursor.getShort(offset + 14) != 0);
        entity.setTagJson(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setTagClassName(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TaskDBInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TaskDBInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TaskDBInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}