package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.VendorItem;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface VendorItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorItem(VendorItem... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorItem(VendorItem data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVendorItemList(List<VendorItem> VendorItemsList);

    @Query("select * from vendor_items order by order_number")
    List<VendorItem> getVendorItems();

    @Query("select * from vendor_items where item_type='zone' and vendor_id=:vendorId")
    List<VendorItem> getVendorZones(int vendorId);

    @Query("select * from vendor_items where vendor_id=:vendorId")
    List<VendorItem> getVendorSamtrans(int vendorId);

    @Query("select * from vendor_items where item_type=:itemType and vendor_id=:vendorId")
    List<VendorItem> getVendorItems(int vendorId, String itemType);

    @Query("select * from vendor_items where item_code=:zoneString")
    VendorItem getVendorZone(String zoneString);

    @Query("select * from vendor_items where item_name=:itemName")
    VendorItem getVendorItemByName(String itemName);

    @Query("select * from vendor_items where item_type=:itemType")
    VendorItem getVendorZoneByType(String itemType);

    @Query("Delete from vendor_items")
    void removeAll();

    @Query("Delete from vendor_items where item_id=:id")
    void removeById(int id);
}
