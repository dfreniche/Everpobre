package io.keepcoding.everpobre.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import io.keepcoding.everpobre.EverpobreApp;
import io.keepcoding.everpobre.model.Note;
import io.keepcoding.everpobre.model.Notebook;
import io.keepcoding.everpobre.model.dao.NoteDAO;
import io.keepcoding.everpobre.model.dao.NotebookDAO;
import io.keepcoding.everpobre.model.db.DBConstants;
import io.keepcoding.everpobre.model.db.DBHelper;


public class EverpobreProvider extends ContentProvider {
	public static final String EVERPOBRE_PROVIDER = "io.keepcoding.everpobre.provider";

	// content://io.keepcoding.everpobre.provider/notebooks
	public static final Uri NOTEBOOKS_URI = Uri.parse("content://" + EVERPOBRE_PROVIDER + "/notebooks");

	// content://io.keepcoding.everpobre.provider/notes
	public static final Uri NOTES_URI = Uri.parse("content://" + EVERPOBRE_PROVIDER + "/notes");


	// Create the constants used to differentiate between the different URI requests.
	private static final int ALL_NOTEBOOKS = 1;

	private static final int SINGLE_NOTEBOOK = 2;
	private static final int ALL_NOTES = 3;
	private static final int SINGLE_NOTE = 4;
	private static final UriMatcher uriMatcher;

	// Populate the UriMatcher object, where a URI ending in elements will correspond to a request for all items, and elements/[rowID] represents a single row.
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(EVERPOBRE_PROVIDER, "notebooks", ALL_NOTEBOOKS);
		uriMatcher.addURI(EVERPOBRE_PROVIDER, "notebooks/#", SINGLE_NOTEBOOK);
		uriMatcher.addURI(EVERPOBRE_PROVIDER, "notes", ALL_NOTES);
		uriMatcher.addURI(EVERPOBRE_PROVIDER, "notes/#", SINGLE_NOTE);
	}

	private DBHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());

		return false;
	}

    // Implement this to handle requests for the MIME type of the data at the given URI.
	// single item: vnd.android.cursor.item/vnd.<companyname>.<contenttype>
	// all items: vnd.android.cursor.dir/vnd.<companyname>.<contenttype>
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case ALL_NOTEBOOKS:
				return "vnd.android.cursor.dir/vnd.keepcoding.notebook";
			case SINGLE_NOTEBOOK:
				return "vnd.android.cursor.item/vnd.keepcoding.notebook";
			case ALL_NOTES:
				return "vnd.android.cursor.dir/vnd.keepcoding.note";
			case SINGLE_NOTE:
				return "vnd.android.cursor.item/vnd.keepcoding.note";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Open a read / write database to support the transaction.

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String tableName = getTableName(uri);

		// To add empty rows to your database by passing in an empty Content Values object
		// you must use the null column hack parameter to specify the name of the column that can be set to null.
		String nullColumnHack = null;
		// Insert the values into the table
		long id = db.insert(tableName, nullColumnHack, values);
		// Construct and return the URI of the newly inserted row.
		if (id > -1) {
			// Construct and return the URI of the newly inserted row.
			Uri insertedUri = null;
			switch (uriMatcher.match(uri)) {
			case ALL_NOTEBOOKS:
				insertedUri = ContentUris.withAppendedId(NOTEBOOKS_URI, id);
				break;
			case SINGLE_NOTEBOOK :
				insertedUri = ContentUris.withAppendedId(NOTEBOOKS_URI, id);
				break;
			case ALL_NOTES:
				insertedUri = ContentUris.withAppendedId(NOTES_URI, id);
				break;
			case SINGLE_NOTE:
				insertedUri = ContentUris.withAppendedId(NOTES_URI, id);
				break;
			default: break;
			}

			// Notify any observers of the change in the data set.
			getContext().getContentResolver().notifyChange(uri, null);
			getContext().getContentResolver().notifyChange(insertedUri, null);

			return insertedUri;
		} else {
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Open a read / write database to support the transaction.

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String tableName = getTableName(uri);
		String rowID = null;

		// If this is a row URI, limit the deletion to the specified row.
		switch (uriMatcher.match(uri)) {
			case SINGLE_NOTEBOOK:
				rowID = uri.getPathSegments().get(1);
				selection = DBConstants.KEY_NOTEBOOK_ID + "=" + rowID;
				break;
			case SINGLE_NOTE:
				rowID = uri.getPathSegments().get(1);
				selection = DBConstants.KEY_NOTE_ID + "=" + rowID;
				break;
			default:
				break;
		}

		// Perform the deletion.
		int deleteCount = db.delete(tableName, selection, selectionArgs);
		// Notify any observers of the change in the data set.
		getContext().getContentResolver().notifyChange(uri, null);

		// Return the number of deleted items.
		return deleteCount;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// Open the database. 

		SQLiteDatabase db = getDB(); 

		// Replace these with valid SQL statements if necessary. 
		String groupBy = null;
		String having = null;
		// Use an SQLite Query Builder to simplify constructing the database query.

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(getTableName(uri));
		
		// If this is a row query, limit the result set to the passed in row. 
		String rowID = null;
		switch (uriMatcher.match(uri)) {
		case SINGLE_NOTEBOOK :
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(DBConstants.KEY_NOTEBOOK_ID + "=" + rowID);
			break;
		case SINGLE_NOTE:
			rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(DBConstants.KEY_NOTE_ID + "=" + rowID);
			break;
		default: break; 
		}

		// Specify the table on which to perform the query. This can // be a specific table or a join as required. queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);
		// Execute the query.
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
		// Return the result Cursor.
		cursor.setNotificationUri(getContext().getContentResolver(), uri);


		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = getDB(); 

		String rowID = null;
		// If this is a row URI, limit the deletion to the specified row. 
		switch (uriMatcher.match(uri)) {
		case SINGLE_NOTEBOOK :
			rowID = uri.getPathSegments().get(1);
			selection = DBConstants.KEY_NOTEBOOK_ID + "=" + rowID;
			break;
		case SINGLE_NOTE :
			rowID = uri.getPathSegments().get(1);
			selection = DBConstants.KEY_NOTE_ID + "=" + rowID;
			break;
		default: 
			break; 
		}
		
		if (rowID == null) {
			return -1;
		}
		
		int updateCount = db.update(getTableName(uri), values, selection, selectionArgs);

		// Notify any observers of the change in the data set. 
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

	private String getTableName(Uri uri) {
		String tableName = null;
		switch (uriMatcher.match(uri)) {
		case ALL_NOTEBOOKS:
			tableName = DBConstants.TABLE_NOTEBOOK;
			break;
		case SINGLE_NOTEBOOK :
			tableName = DBConstants.TABLE_NOTEBOOK;
			break;
		case ALL_NOTES:
			tableName = DBConstants.TABLE_NOTE;
			break;
		case SINGLE_NOTE:
			tableName = DBConstants.TABLE_NOTE;
			break;
		default: break; 
		}
		return tableName;
	}

	private SQLiteDatabase getDB() {
		SQLiteDatabase db; 
		try {
			db = dbHelper.getWritableDatabase(); 
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase(); 
		}
		return db;
	}
	
	// convenience methods: easy access to this content provider from within this project
	
	public static String getIdFromUri(Uri uri) {
		String rowID = uri.getPathSegments().get(1);
		return rowID;
	}
	
	public static Cursor getAllNotebooks() {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();

		Cursor cursor = cr.query(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.allColumns, null, null, null);

		return cursor;
	}
	
	public static Uri insertNotebook(Notebook notebook) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		if (notebook == null) {
			return null;
		}
		
		
		Uri uri = cr.insert(EverpobreProvider.NOTEBOOKS_URI, NotebookDAO.getContentValues(notebook));
		notebook.setId(Long.parseLong(getIdFromUri(uri)));
		return uri;
	}
	
	public static Uri insertNote(Notebook notebook, String text) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		
		Note n = new Note(notebook, text);
		Uri insertedUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(n));
		
		return insertedUri;
	}
	
	public static Uri insertNote(Notebook notebook, Note note) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		
		note.setNotebook(notebook);
		Uri insertedUri = cr.insert(EverpobreProvider.NOTES_URI, NoteDAO.getContentValues(note));
		
		return insertedUri;
	}
	
	public static void deleteNotebook(Notebook notebook) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		String sUri = NOTEBOOKS_URI.toString() + "/" + notebook.getId();
		Uri uri = Uri.parse(sUri);
		cr.delete(uri, null, null);
	}
	
	/**
	 * Deletes a notebook using its id
	 * @param id
	 */
	public static void deleteNotebook(long id) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		String sUri = NOTEBOOKS_URI.toString() + "/" + id;
		Uri uri = Uri.parse(sUri);
		cr.delete(uri, null, null);
	}
	
	/**
	 * 
	 * @param notebook
	 * @return all notes from a Notebook object
	 */
	public static Cursor getAllNotes(Notebook notebook) {	
		return getAllNotes(notebook.getId());
	}

	/**
	 * 
	 * @param notebookId id of the notebook whose notes we want
	 * @return
	 */
	public static Cursor getAllNotes(long notebookId) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		
		Cursor c = cr.query(EverpobreProvider.NOTES_URI, null, DBConstants.KEY_NOTE_NOTEBOOK + "=" + notebookId, null, null);
		
		return c;
	}
	
	public static void deleteNote(long id) {
		ContentResolver cr = EverpobreApp.getAppContext().getContentResolver();
		String sUri = NOTES_URI.toString() + "/" + id;
		Uri uri = Uri.parse(sUri);
		cr.delete(uri, null, null);
	}


}
