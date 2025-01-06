package com.ticketpro.parking.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ticketpro.model.Color;

import java.util.List;

/**
 * Created by Rohit on 23-07-2020.
 */
@Dao
public interface ColorsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColor(Color... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColor(Color data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertColorList(List<Color> ColorsList);

    @Query("select * from colors order by order_number,color")
    List<Color> getColors();

    @Query("select * from colors where color_id=:colorId")
    Color getColorById(int colorId);

    @Query("select * from colors where UPPER(code)=:colorCode")
    Color getColorByCode(String colorCode);

    @Query("Select * from colors where UPPER(color)=:colorTitle")
    Color getColorByTitle(String colorTitle);

    @Query("select color_id from colors where color=:name")
    int getColorIdByName(String name);

    @Query("select color_id from colors where code=:code")
    int getColorIdByCode(String code);

    @Query("select code from colors WHERE UPPER(color) = UPPER(:name)")
    String getColorCodeByName(String name);

    @Query("select code from colors where color_id=:colorId")
    String getColorCodeById(int colorId);

    @Query("Delete from colors")
    void removeAll();

    @Query("Delete from colors where color_id=:id")
    void removeById(int id);
}
