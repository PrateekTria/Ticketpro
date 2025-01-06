package com.ticketpro.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.util.version.Semver;

import org.json.JSONObject;

import java.util.Date;

public class VersionUpdate implements Parcelable {

    public static final Creator<VersionUpdate> CREATOR = new Creator<VersionUpdate>() {
        @Override
        public VersionUpdate createFromParcel(Parcel in) {
            return new VersionUpdate(in);
        }

        @Override
        public VersionUpdate[] newArray(int size) {
            return new VersionUpdate[size];
        }
    };
    private int versionId;
    private int custId;
    @SerializedName("module")
    @Expose
    private String module;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("force_install")
    @Expose
    private String forceInstall;
    @SerializedName("apk_url")
    @Expose
    private String apkUrl;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("db_changes")
    @Expose
    private String dbChanges;
    @SerializedName("clean_install")
    @Expose
    private String cleanInstall;
    private Date updateDate;
    private String isActive;
    @SerializedName("local_path")
    @Expose
    private String path;
    @SerializedName("notes_path")
    @Expose
    private String notes_path;

    public VersionUpdate() {
    }

    protected VersionUpdate(Parcel in) {
        versionId = in.readInt();
        custId = in.readInt();
        module = in.readString();
        version = in.readString();
        forceInstall = in.readString();
        apkUrl = in.readString();
        notes = in.readString();
        dbChanges = in.readString();
        cleanInstall = in.readString();
        isActive = in.readString();
        notes_path = in.readString();
        path = in.readString();
    }

    public VersionUpdate(JSONObject object) throws Exception {
        setVersion(object.getString("version"));
        setModule(object.getString("module"));
        setNotes(object.getString("notes"));
        setCleanInstall(object.getString("clean_install"));
        setDbChanges(object.getString("db_changes"));
        setApkUrl(object.getString("apk_url"));
        setForceInstall(object.getString("force_install"));
        setPath(object.optString("local_path"));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionId);
        dest.writeInt(custId);
        dest.writeString(module);
        dest.writeString(version);
        dest.writeString(forceInstall);
        dest.writeString(apkUrl);
        dest.writeString(notes);
        dest.writeString(dbChanges);
        dest.writeString(cleanInstall);
        dest.writeString(isActive);
        dest.writeString(notes_path);
        dest.writeString(path);
    }

    public String getNotes_path() {
        return notes_path;
    }

    public void setNotes_path(String notes_path) {
        this.notes_path = notes_path;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDbChanges() {
        return dbChanges;
    }

    public void setDbChanges(String dbChanges) {
        this.dbChanges = dbChanges;
    }

    public String getCleanInstall() {
        return cleanInstall;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getForceInstall() {
        return forceInstall;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public boolean isCleanInstall() {
        return "Y".equalsIgnoreCase(cleanInstall);
    }

    public void setCleanInstall(String cleanInstall) {
        this.cleanInstall = cleanInstall;
    }

    public boolean hasDbChanges() {
        return "Y".equalsIgnoreCase(dbChanges);
    }

    public boolean isForceInstall() {
        return "Y".equalsIgnoreCase(forceInstall);
    }

    public void setForceInstall(String forceInstall) {
        this.forceInstall = forceInstall;
    }

    public boolean isNewVersion(String currentVersion) {
        Semver sem = new Semver(currentVersion);
        if (sem.isLowerThan(version)) {
            return true;
        }

        return false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
