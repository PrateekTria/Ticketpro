package com.ticketpro.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;
import com.ticketpro.util.StringUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Maybe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.ticketpro.util.TPConstant.TAG;

@Entity(tableName = "users")
public class User implements Serializable {
    @Ignore
    private static final long serialVersionUID = 1L;
    @PrimaryKey
    @ColumnInfo(name = "userid")
    @SerializedName("userid")
    @Expose
    private int userId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "username")
    @SerializedName("username")
    @Expose
    private String username;
    @ColumnInfo(name = "password")
    @SerializedName("password")
    @Expose
    private String password;
    @ColumnInfo(name = "user_type")
    @SerializedName("user_type")
    @Expose
    private String userType;
    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @ColumnInfo(name = "badge")
    @SerializedName("badge")
    @Expose
    private String badge;
    @ColumnInfo(name = "department")
    @SerializedName("department")
    @Expose
    private String department;
    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    @Expose
    private String isAtive;
    @ColumnInfo(name = "email_address")
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @ColumnInfo(name = "modules")
    @SerializedName("modules")
    @Expose
    private String modules;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("print_name")
    @Expose
    @ColumnInfo(name = "print_name")
    private String print_name;

    @SerializedName("rmsid")
    @Expose
    @ColumnInfo(name = "rmsid")
    private String rmsid;

    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;

    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public User() {
    }

    public User(JSONObject object) throws Exception {
        this.setUserId(object.getInt("userid"));
        this.setCustId(object.getInt("custid"));
        this.setUsername(object.getString("username"));
        this.setPassword(object.getString("password"));
        this.setUserType(object.getString("user_type"));
        this.setFirstName(object.getString("first_name"));
        this.setLastName(object.getString("last_name"));
        this.setBadge(object.getString("badge"));
        this.setDepartment(object.getString("department"));
        this.setIsAtive(object.getString("is_active"));
        this.setEmailAddress(object.getString("email_address"));
        this.setModules(object.getString("modules"));
        this.setTitle(object.getString("title"));
        this.setPrint_name(object.getString("print_name"));
        this.setRmsid(object.getString("rmsid"));

    }

    public static ArrayList<User> getUsers(String moduleName) throws Exception {
        ArrayList<User> list;

        if (!"ALL".equalsIgnoreCase(moduleName) && moduleName != null) {
            list = (ArrayList<User>) ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().getUsers(moduleName);
        } else {
            list = (ArrayList<User>) ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().getAllUsers();
        }
        return list;
    }

    public int getSyncDataId() {
        return syncDataId;
    }

    public void setSyncDataId(int syncDataId) {
        this.syncDataId = syncDataId;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public static int getUserId(String password) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().getUserId(password);
    }

    public static User getUserInfo(int userId) {
        User object = null;
        object = ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().getUserInfo(userId);
        return object;
    }

    public static User getUserByRmsid(String rmsid) {
        return ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().getUserByRmsidInfo(rmsid);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().removeById(id);
    }

    public static void insertUser(final List<User> users) {
        final ParkingDatabase instance = ParkingDatabase.getInstance(TPApplication.getInstance());
        final long l = System.currentTimeMillis();
        Log.i(TAG, "insertUser: " + l);
        instance.usersDao().insertUsersList(users).subscribeOn(Schedulers.io()).subscribeWith(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + System.currentTimeMillis());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: " + (System.currentTimeMillis() - l));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: ", e);
            }
        });
    }

    public static void insertUser(User user) {
        ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().insertUsers(user).subscribeOn(Schedulers.io()).subscribe();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrint_name() {
        return print_name;
    }

    public void setPrint_name(String print_name) {
        this.print_name = print_name;
    }

    public ContentValues getContentValues() throws Exception {
        ContentValues values = new ContentValues();
        values.put("userid", this.userId);
        values.put("custid", this.custId);
        values.put("username", this.username);
        values.put("password", this.password);
        values.put("user_type", this.userType);
        values.put("first_name", this.firstName);
        values.put("last_name", this.lastName);
        values.put("badge", this.badge);
        values.put("department", this.department);
        values.put("is_active", this.isAtive);
        values.put("email_address", this.emailAddress);
        values.put("modules", this.modules);
        values.put("title", this.title);
        values.put("print_name", this.print_name);

        return values;
    }

    public String getRmsid() {
        return rmsid;
    }

    public void setRmsid(String rmsid) {
        this.rmsid = rmsid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIsAtive() {
        return isAtive;
    }

    public void setIsAtive(String isAtive) {
        this.isAtive = isAtive;
    }

    public boolean isAdmin() {
        if ("Admin".equalsIgnoreCase(this.userType)) {
            return true;
        }

        return false;
    }

    public boolean isOfficer() {
        if ("Officer".equalsIgnoreCase(this.userType)) {
            return true;
        }

        return false;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        if (this.firstName != null && this.lastName != null) {
            return this.firstName + " " + this.lastName;
        }

        return StringUtil.getDisplayString(this.firstName);
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    @SuppressLint("StaticFieldLeak")
    static class InsertUsers extends AsyncTask<Void, Void, Void> {
        List<User> list;

        public InsertUsers(List<User> users) {
            list = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().insertUsersList(list);
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    static class InsertUser extends AsyncTask<Void, Void, Void> {
        User user;

        public InsertUser(User user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).usersDao().insertUsers(user);
            return null;
        }
    }
}
