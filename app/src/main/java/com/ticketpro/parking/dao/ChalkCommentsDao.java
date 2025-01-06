package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.ChalkComment;

import java.util.List;

import io.reactivex.Completable;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ChalkCommentsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkComment(ChalkComment... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertChalkComment(ChalkComment data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChalkCommentList(List<ChalkComment> chalkCommentsList);

    @Query("select * from chalk_comments where chalk_id=:chalkId")
    List<ChalkComment> getChalkComments(long chalkId);

    @Query("select * from chalk_comments where chalk_id=:commentId")
    ChalkComment getChalkCommentByPrimaryKey(long commentId);

    @Query("select max(chalk_comment_id) as max_chalk_comment_id from chalk_comments")
    int getNextPrimaryId();

    @Query("Delete from chalk_comments")
    void removeAll();

    @Query("Delete from chalk_comments where chalk_id=:chalkId")
    void removeChalkCommentById(long chalkId);
}
