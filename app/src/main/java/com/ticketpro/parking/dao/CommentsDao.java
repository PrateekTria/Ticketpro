package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Comment;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */

@Dao
public interface CommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentList(List<Comment> CommentsList);

    @Query("select * from comments order by order_number,comment")
    List<Comment> getComments();

    @Query("select * from comments where comment_id=:commentId")
    Comment getCommentById(int commentId);

    @Query("select * from comments where comment=:commentText")
    Comment getCommentByText(String commentText);

    @Query("select * from comments where comment=:commentText")
    Comment getCommentsByText(String commentText);

    @Query("select max(comment_id) as max_comment_id from comments")
    int getLastInsertId();

    @Query("Delete from comments")
    void removeAll();

    @Query("Delete from comments where code like 'NCMMT%'")
    void removeAddedComments();

    @Query("Delete from comments where comment_id=:id")
    void removeById(int id);

    @Query("select * from comments where code like 'NCMMT%'")
    List<Comment> getCustomeComments();


}
