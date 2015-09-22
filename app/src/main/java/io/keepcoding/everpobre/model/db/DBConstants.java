package io.keepcoding.everpobre.model.db;

public class DBConstants {
    public static final String[] CREATE_DATABASE = {
            SQL_CREATE_NOTEBOOK_TABLE,
            SQL_CREATE_NOTE_TABLE
    };

    public static final String DROP_DATABASE = "";

    public static final String TABLE_NOTEBOOK = "NOTEBOOK";
    public static final String TABLE_NOTE = "NOTE";


    // Table field constants
    public static final String KEY_NOTEBOOK_ID = "_id";
    public static final String KEY_NOTEBOOK_NAME = "name";
    public static final String KEY_NOTEBOOK_CREATION_DATE = "creationDate";
    public static final String KEY_NOTEBOOK_MODIFICATION_DATE = "modificationDate";

    // Table field constants
    public static final String KEY_NOTE_ID = "_id";
    public static final String KEY_NOTE_TEXT = "text";
    public static final String KEY_NOTE_CREATION_DATE = "creationDate";
    public static final String KEY_NOTE_MODIFICATION_DATE = "modificationDate";
    public static final String KEY_NOTE_PHOTO_URL = "photoUrl";
    public static final String KEY_NOTE_NOTEBOOK = "notebook";
    public static final String KEY_NOTE_LATITUDE = "latitude";
    public static final String KEY_NOTE_LONGITUDE = "longitude";
    public static final String KEY_NOTE_HAS_COORDINATES = "hasCoordinates";
    public static final String KEY_NOTE_ADDRESS = "address";


    public static final String SQL_CREATE_NOTEBOOK_TABLE =
            "create table "
                    + TABLE_NOTEBOOK + "( " + KEY_NOTEBOOK_ID
                    + " integer primary key autoincrement, "
                    + KEY_NOTEBOOK_NAME + " text not null,"
                    + KEY_NOTEBOOK_CREATION_DATE + " INTEGER, "
                    + KEY_NOTEBOOK_MODIFICATION_DATE + " INTEGER "
                    + ");";

    public static final String SQL_CREATE_NOTE_TABLE =
            "create table "
                    + TABLE_NOTE + "( " + KEY_NOTE_ID + " integer primary key autoincrement, "
                    + KEY_NOTE_TEXT + " text not null,"
                    + KEY_NOTE_CREATION_DATE + " INTEGER, "
                    + KEY_NOTE_MODIFICATION_DATE + " INTEGER, "
                    + KEY_NOTE_PHOTO_URL + " text,"
                    + KEY_NOTE_NOTEBOOK + " INTEGER,"
                    + KEY_NOTE_LATITUDE + " real,"
                    + KEY_NOTE_LONGITUDE + " real, "
                    + KEY_NOTE_HAS_COORDINATES + " INTEGER, "
                    + KEY_NOTE_ADDRESS + " text, "
                    + "FOREIGN KEY(" + KEY_NOTE_NOTEBOOK + ") REFERENCES " + TABLE_NOTEBOOK + "(" + KEY_NOTEBOOK_ID + ") ON DELETE CASCADE"
                    + ");";

}
