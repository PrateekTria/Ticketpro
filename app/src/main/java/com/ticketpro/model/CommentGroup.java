package com.ticketpro.model;

import android.content.ContentValues;
import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ticketpro.parking.activity.TPApplication;
import com.ticketpro.parking.dao.ParkingDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName = "comment_groups")
public class CommentGroup {
    @PrimaryKey
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    @Expose
    private int groupId;
    @ColumnInfo(name = "custid")
    @SerializedName("custid")
    @Expose
    private int custId;
    @ColumnInfo(name = "group_code")
    @SerializedName("group_code")
    @Expose
    private String groupCode;
    @ColumnInfo(name = "group_name")
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @Ignore
    @SerializedName("sync_data_id")
    @Expose
    private int syncDataId;
    @Ignore
    @SerializedName("primary_key")
    @Expose
    private int primaryKey;

    public CommentGroup() {

    }

    public CommentGroup(JSONObject object) throws Exception {
        this.setGroupId(object.getInt("group_id"));
        this.setCustId(object.getInt("custid"));
        this.setGroupCode(object.getString("group_code"));
        this.setGroupName(object.getString("group_name"));
    }

    public static ArrayList<CommentGroup> getCommentGroups() throws Exception {
        return (ArrayList<CommentGroup>) ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupsDao().getCommentGroups();
    }

    public static ArrayList<Comment> getCommentsByGroup(String group) throws Exception {
        return (ArrayList<Comment>) ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupsDao().getCommentsByGroup(group);
    }

    public static void removeAll() throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupsDao().removeAll();
    }

    public static void removeById(int id) throws Exception {
        ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupsDao().removeById(id);
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

    public ContentValues getContentValues() throws Exception {

        ContentValues values = new ContentValues();
        values.put("group_id", this.groupId);
        values.put("custid", this.custId);
        values.put("group_code", this.groupCode);
        values.put("group_name", this.groupName);

        return values;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public static void insertCommentGroup(CommentGroup CommentGroup){
        new CommentGroup.InsertCommentGroup(CommentGroup).execute();
    }

    private static class InsertCommentGroup extends AsyncTask<Void,Void,Void> {
        private CommentGroup CommentGroup;

        public InsertCommentGroup(CommentGroup CommentGroup) {
            this.CommentGroup = CommentGroup;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParkingDatabase.getInstance(TPApplication.getInstance()).commentgroupsDao().insertCommentGroup(CommentGroup);
            return null;
        }
    }

}
