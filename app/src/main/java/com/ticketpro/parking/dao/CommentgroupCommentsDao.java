package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.CommentGroupComment;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CommentgroupCommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroupComment(CommentGroupComment... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroupComment(CommentGroupComment data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroupCommentList(List<CommentGroupComment> CommentGroupCommentsList);

    @Query("select * from comment_group_comments")
    List<CommentGroupComment> getCommentGroupComments();

    @Query("Delete from comment_group_comments")
    void removeAll();

    @Query("Delete from comment_group_comments where comment_group_id= :id")
    void removeById(int id);
}
