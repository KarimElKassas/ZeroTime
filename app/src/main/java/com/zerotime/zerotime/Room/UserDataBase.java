package com.zerotime.zerotime.Room;

import com.zerotime.zerotime.Room.Data.UserDao;
import com.zerotime.zerotime.Room.Model.Complaint;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Complaint.class},version = 1,exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
public abstract UserDao getUserDao();









}
