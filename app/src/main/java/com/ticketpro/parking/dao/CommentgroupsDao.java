package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Comment;
import com.ticketpro.model.CommentGroup;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface CommentgroupsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroup(CommentGroup... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroup(CommentGroup data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCommentGroupList(List<CommentGroup> CommentGroupsList);

    @Query("select * from comment_groups")
    List<CommentGroup> getCommentGroups();

    @Query("select * from comments where comments.code like '%NCMMT%' or comments.comment_id in (select comment_id from comment_group_comments where group_id=(select group_id from comment_groups where group_code=:group)) order by order_number, comment_id DESC")
    List<Comment> getCommentsByGroup(String group);

   @Query("select * from comments order by order_number")
    List<Comment> getALLComments();

    @Query("Delete from comment_groups")
    void removeAll();

    @Query("Delete from comment_groups where group_id=:id")
    void removeById(int id);
}
