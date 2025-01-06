package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Contact;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContactList(List<Contact> ContactsList);

    @Query("select * from contacts where custid <> 1")
    List<Contact> getContacts();

    @Query("select * from contacts where contact_type='TowNotify'")
    List<Contact> getTowNotifyContacts();

    @Query("select * from contacts where contact_type='Support'")
    List<Contact> getSupportContacts();

    @Query("select phone from contacts where custid=1")
    String getSupportNumber();

    @Query("Delete from contacts")
    void removeAll();

    @Query("Delete from contacts where contact_id=:id")
    void removeById(int id);
}
