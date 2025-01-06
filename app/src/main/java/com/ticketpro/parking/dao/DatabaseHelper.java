package com.ticketpro.parking.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ticketpro.exception.TPException;
import com.ticketpro.util.TPUtility;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ticketpro.db";
    private static final int DATABASE_VERSION = 15;
    private static String DATABASE_PATH = "";
    private static Context context;
    private static DatabaseHelper mInstance;
    private SQLiteDatabase dbSqlite;
    private Logger log = Logger.getLogger("DatabaseHelper");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;

        DATABASE_PATH = DatabaseHelper.context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
    }

    public static void init(Context ctx) {
        mInstance = new DatabaseHelper(ctx.getApplicationContext());
    }

    public static synchronized DatabaseHelper getInstance() {
        if (mInstance == null) {
            if (context != null)
                mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            if (context != null)
                mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DatabaseHelper", "onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String packageName = context.getPackageName();
        SharedPreferences mPreferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE);
        int currentVersion = mPreferences.getInt("DATABASE_VERSION", 0);
        if (currentVersion < newVersion) {
            return;
        }

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("DATABASE_VERSION", newVersion);
        editor.apply();

        log.info("Upgrading database from " + oldVersion + " to " + newVersion);

        try {
            db.execSQL("ALTER TABLE devices ADD COLUMN os_version TEXT");
            db.execSQL("CREATE TABLE \"device_groups\" (\n" +
                    "\t\"group_id\"\tINTEGER,\n" +
                    "\t\"group_name\"\tTEXT,\n" +
                    "\t\"device_ids\"\tTEXT,\n" +
                    "\t\"custid\"\tINTEGER\n" +
                    ");");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDatabase() throws IOException {
        createDB();
    }

    public void createDB() throws IOException {
        boolean dbExist = dbExists();
        if (!dbExist) {
            log.info("Creating database...");

            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean dbExists() {
        SQLiteDatabase checkDB = null;
        try {
            File database = context.getDatabasePath(DATABASE_NAME);
            if (database.exists()) {
                String databasePath = database.getAbsolutePath();
                checkDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
            } else {
                log.error("Database does not exits");
            }
        } catch (SQLiteException e) {
            log.error("Database does not exits");
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null;
    }

    public void copyDataBase() throws IOException {
        InputStream iStream = null;
        OutputStream oStream = null;
        String outFilePath = DATABASE_PATH;
        try {
            iStream = context.getAssets().open(DATABASE_NAME);
            oStream = new FileOutputStream(outFilePath);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = iStream.read(buffer)) > 0) {
                oStream.write(buffer, 0, length);
            }

            oStream.flush();
            oStream.close();
            iStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }

        log.info("Created Database successfully.");
    }

    public void backupDatabase() {
        try {
            if (DATABASE_PATH == null || DATABASE_PATH.equals("")) {
                DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
            }

            File dbFile = new File(DATABASE_PATH);
            FileInputStream fis;

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String dateStr = sdf.format(cal.getTime());

            fis = new FileInputStream(dbFile);
            String outFileName = TPUtility.getBackupFolder() + "ticketPRO_" + dateStr + ".sqlite";
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();

            log.info("Done database backup..." + outFileName);

        } catch (IOException e) {
            log.error(TPUtility.getPrintStackTrace(e));
        }
    }

    public SQLiteDatabase openReadableDatabase() throws SQLException {
        SQLiteDatabase db = getReadableDatabase();
        if (!db.isOpen()) {
            Log.e("DatabaseHelper", "Readonly Database is closed");
            db = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
        }

        return db;
    }

    public void openWritableDatabase() throws SQLException {
        dbSqlite = getWritableDatabase();

        if (!dbSqlite.isOpen()) {
            Log.e("DatabaseHelper", "Writable Database is closed");
            dbSqlite = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    public synchronized void closeWritableDb() {
        try {
            if (dbSqlite != null && dbSqlite.isOpen()) {
                dbSqlite.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {
        try {
            super.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkRecordExist(String tableName, String[] keys, String[] values) throws TPException {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keys.length; i++) {
                sb.append(keys[i]).append("=\"").append(values[i]).append("\" ");
                if (i < keys.length - 1)
                    sb.append("AND ");
            }

            Cursor cursor = dbSqlite.query(tableName, null, sb.toString(), null, null, null, null);
            boolean exists = (cursor.getCount() > 0);
            cursor.close();

            return exists;

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            TPException appEx = new TPException();
            appEx.setErrorMessage("Unable to check record exist in database.");
            throw appEx;
        }
    }

    public boolean insertOrReplace(ContentValues values, String tableName) throws SQLiteException {
        try {
            final String COMMA_SPACE = ", ";
            StringBuilder columnsBuilder = new StringBuilder();
            StringBuilder placeholdersBuilder = new StringBuilder();
            List<Object> pureValues = new ArrayList<Object>(values.size());
            Iterator<Entry<String, Object>> iterator = values.valueSet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> pair = iterator.next();
                String column = pair.getKey();
                columnsBuilder.append(column).append(COMMA_SPACE);
                placeholdersBuilder.append("?").append(COMMA_SPACE);
                Object value = pair.getValue();
                pureValues.add(value);
            }
            final String columns = columnsBuilder.substring(0, columnsBuilder.length() - COMMA_SPACE.length());
            final String placeholders = placeholdersBuilder.substring(0, placeholdersBuilder.length() - COMMA_SPACE.length());
            if (dbSqlite == null || !dbSqlite.isOpen()) {
                openWritableDatabase();
            }
            dbSqlite.execSQL("INSERT OR REPLACE INTO " + tableName + "(" + columns + ") VALUES (" + placeholders + ")", pureValues.toArray());
            //closeWritableDb();
        } catch (SQLiteException e) {
            log.error(TPUtility.getPrintStackTrace(e));
            //closeWritableDb();
            throw e;
        }
        return true;
    }

    public boolean insertOrReplaceByJSON(JSONObject jsonObject, ContentValues propertiesMap, String tableName)
            throws Exception {

        try {
            final String COMMA_SPACE = ", ";
            StringBuilder columnsBuilder = new StringBuilder();
            StringBuilder placeholdersBuilder = new StringBuilder();
            List<Object> pureValues = new ArrayList<Object>();
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String column = (String) iterator.next();
                if (!propertiesMap.containsKey(column)) {
                    continue;
                }

                columnsBuilder.append(column).append(COMMA_SPACE);
                placeholdersBuilder.append("?").append(COMMA_SPACE);
                Object value = jsonObject.get(column);
                pureValues.add(value);
            }

            final String columns = columnsBuilder.substring(0, columnsBuilder.length() - COMMA_SPACE.length());
            final String placeholders = placeholdersBuilder.substring(0, placeholdersBuilder.length() - COMMA_SPACE.length());

            if (dbSqlite == null || !dbSqlite.isOpen()) {
                openWritableDatabase();
            }

            dbSqlite.execSQL("INSERT OR REPLACE INTO " + tableName + "(" + columns + ") VALUES (" + placeholders + ")", pureValues.toArray());

        } catch (Exception e) {
            log.error(TPUtility.getPrintStackTrace(e));
            throw e;
        }

        return true;
    }
}
