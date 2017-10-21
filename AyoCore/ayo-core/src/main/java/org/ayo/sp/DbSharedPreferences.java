
package org.ayo.sp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.ayo.log.Trace;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DbSharedPreferences implements SharedPreferences {
    private static final String TAG = "DbSharedPreferences";

    private Context mContext;
    private DbHelper mOpenHelper;

    private static DbSharedPreferences instance;

    public static DbSharedPreferences getInstance(@NonNull Context context) {
        if (instance == null)
            instance = new DbSharedPreferences(context);
        return instance;
    }

    private DbSharedPreferences(@NonNull Context context) {
        if (context != null){
            mContext = context.getApplicationContext();
            mOpenHelper = new DbHelper(mContext);
        }
        if (mContext == null) {
            throw new RuntimeException("Context can't be null");
        }
    }

    private class DBEditor implements Editor {
        private final static String TAG = "DBEditor";

        private void insert(String key, String value) {
            ContentValues values = new ContentValues();
            values.put(DbHelper.COLUMNS.SP_KEY, key);
            values.put(DbHelper.COLUMNS.SP_VALUE, value);
//            Uri uri = mContext.getContentResolver()
//                    .insert(SPContentProvider.SP.CONTENT_URI, values);

            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            long rowId = db.replace(DbHelper.TABLE_NAME, null, values);

            Trace.e(TAG, "insert key:" + key);
            Trace.e(TAG, "insert value:" + value);
        }

        @Override
        public Editor putString(String key, String value) {
            insert(key, value);
            Trace.e(TAG, "putString:" + key + "==>" + value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> v) {
            return null;
        }

        @Override
        public Editor putInt(String key, int value) {
            insert(key, value + "");
            Trace.e(TAG, "putString:" + key + "==>" + value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            insert(key, value + "");
            Trace.e(TAG, "putString:" + key + "==>" + value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            insert(key, value + "");
            Trace.e(TAG, "putString:" + key + "==>" + value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            insert(key, value + "");
            Trace.e(TAG, "putString:" + key + "==>" + value);
            return this;
        }

        @Override
        public Editor remove(String key) {
//            int count = mContext.getContentResolver().delete(SPContentProvider.SP.CONTENT_URI,
//                    SPContentProvider.SP.COLUMNS.SP_KEY + " = ? ", new String[] {
//                        key
//                    });
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int count = db.delete(DbHelper.TABLE_NAME, DbHelper.COLUMNS.SP_KEY, new String[] {key});
            Trace.e(TAG, "remove key:" + key);
            Trace.e(TAG, "remove count:" + count);
            return this;
        }

        @Override
        public Editor clear() { // TODO: 16/4/21 未实现功能
            return null;
        }

        @Override
        public boolean commit() { // TODO: 16/4/21 未实现功能
            return false;
        }

        @Override
        public void apply() { // TODO: 16/4/21 未实现功能
        }
    }

    private String query(String key) {
        Trace.e(TAG, "query:" + key);
        Cursor cursor = null;
        try {
//            cursor = mContext.getContentResolver().query(SPContentProvider.SP.CONTENT_URI,
//                    new String[] {
//                        SPContentProvider.SP.COLUMNS.SP_VALUE
//                    },
// SPContentProvider.SP.COLUMNS.SP_KEY + " = ? ",
// new String[] {
//                        key
//                    },
// null);
            cursor =  mOpenHelper.getReadableDatabase().query(DbHelper.TABLE_NAME,
                    new String[] {DbHelper.COLUMNS.SP_VALUE},
                    DbHelper.COLUMNS.SP_KEY + " = ? ",
                    new String[] {key}, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                Trace.e(TAG, "query: null");
                return null;
            }
            String value = cursor.getString(0);
            Trace.e(TAG, "query:" + value);
            return value;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    @Override
    public Map<String, ?> getAll() {
        Map<String, String> map = new HashMap();
        Cursor cursor = null;
        try {

            cursor =  mOpenHelper.getReadableDatabase().query(DbHelper.TABLE_NAME,
                    new String[] {
                            DbHelper.COLUMNS.SP_KEY,
                            DbHelper.COLUMNS.SP_VALUE,
                    },
                    null, null, null, null, null);

//            cursor = mContext.getContentResolver().query(
//                    SPContentProvider.SP.CONTENT_URI,
//                    new String[] {
//                            SPContentProvider.SP.COLUMNS.SP_KEY,
//                            SPContentProvider.SP.COLUMNS.SP_VALUE,
//                    }, null, null, null);
            if (cursor == null) {
                return null;
            }
            boolean hasNext = cursor.moveToFirst();
            while (hasNext) {
                String key = cursor.getString(0);
                String value = cursor.getString(1);
                map.put(key, value);
                hasNext = cursor.moveToNext();
                // Trace.e(TAG, "key:" + key+"  value:"+value);
            }

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return map;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        String value = query(key);

        if (value == null)
            return defValue;

        return value;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) { // TODO:
                                                                         // 16/4/21
                                                                         // 未实现功能
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = query(key);
        if (TextUtils.isEmpty(value))
            return defValue;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = query(key);
        if (TextUtils.isEmpty(value))
            return defValue;
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    @Override
    public float getFloat(String key, float defValue) {
        String value = query(key);
        if (TextUtils.isEmpty(value))
            return defValue;
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = query(key);
        if (TextUtils.isEmpty(value))
            return defValue;
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    @Override
    public boolean contains(String key) {
//        Cursor cursor = mContext.getContentResolver().query(SPContentProvider.SP.CONTENT_URI,
//                new String[] {
//                    SPContentProvider.SP.COLUMNS.SP_VALUE
//                }, SPContentProvider.SP.COLUMNS.SP_KEY + " = ? ", new String[] {
//                    key
//                }, null);

        Cursor cursor =  mOpenHelper.getReadableDatabase().query(DbHelper.TABLE_NAME,
                new String[] {DbHelper.COLUMNS.SP_VALUE},
                DbHelper.COLUMNS.SP_KEY + " = ? ",
                new String[] {key}, null, null, null);
        if (cursor == null || cursor.moveToFirst()) {
            Trace.e(TAG, "contains:false");
            return false;
        }
        Trace.e(TAG, "contains:true");
        return true;
    }

    @Override
    public Editor edit() {
        return new DBEditor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        // TODO: 16/4/21 未实现功能
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        // TODO: 16/4/21 未实现功能
    }

    private static class DbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "sp.db";
        private static final int DATABASE_VERSION = 1;

        private static String TABLE_NAME = "sp";

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMNS.ID
                + " INTEGER  PRIMARY KEY," + COLUMNS.SP_KEY + " TEXT UNIQUE," + COLUMNS.SP_VALUE
                + " TEXT); ";

        interface COLUMNS {
            String ID = "_id";

            String SP_KEY = "sp_key";

            String SP_VALUE = "sp_value";
        }

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
