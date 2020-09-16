package com.zerotime.zerotime.Room.Data;

import com.zerotime.zerotime.Room.Model.Complaint;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Dao
public
interface UserDao {



    @Insert
    void insertComplaint(Complaint complaint);

    @Delete
    void delete(Complaint complaint);

    @Delete
    void deleteAll(List<Complaint> complaint);

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("Drop TABLE Complaint");
        }
    };

    @Query("SELECT * FROM Complaint")
    List<Complaint> getComplaints();

    @Query("SELECT COUNT(date) FROM Complaint")
    int getCount();

    @Query("DELETE FROM Complaint")
    public void nukeTable();


}
