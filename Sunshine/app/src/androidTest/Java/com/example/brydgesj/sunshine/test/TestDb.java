package com.example.brydgesj.sunshine.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.brydgesj.sunshine.MainActivity;
import com.example.brydgesj.sunshine.data.WeatherContract;
import com.example.brydgesj.sunshine.data.WeatherContract.LocationEntry;
import com.example.brydgesj.sunshine.data.WeatherContract.WeatherEntry;
import com.example.brydgesj.sunshine.data.WeatherDbHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by jbrydges on 10/1/2014.
 */
public class TestDb extends AndroidTestCase {

    private final String LOG_TAG = TestDb.class.getSimpleName();

    /*
    Tests that the database does stuff
     */
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());
        db.close();
    }

    public void assertRowMatch(ContentValues expected, Cursor actual)
    {
        Iterator<String> keyiter = expected.keySet().iterator();

        while (keyiter.hasNext()) {
            String key = keyiter.next();
            String actual_value = actual.getString(actual.getColumnIndex(key));
            assertEquals("Comparing " + key, expected.getAsString(key), actual_value);
        }
    }

    // Inserts values into tableName in database db and asserts values were inserted correctly in
    // all columns
    public long insertValuesAssert(SQLiteDatabase db, String tableName, ContentValues values){
        long locationRowId;
        locationRowId = db.insert(tableName, null, values);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        // Make string array of columns from the keys in ContentValues
        ArrayList<String> columns = new ArrayList<String>();
        Set<Map.Entry<String, Object>> keyValuePairs = values.valueSet();

        for (Map.Entry<String, Object> entry : keyValuePairs) {
            columns.add(entry.getKey());
        }

        String[] columnsArray=new String[columns.size()];
        columns.toArray(columnsArray);

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                tableName,  // Table to Query
                columnsArray,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            assertRowMatch(values, cursor);
        }
        else {
            fail("No values returned :(");
            locationRowId = -1;
        }
        cursor.close();

        return locationRowId;
    }

    // Tests that data is inserted into the weatherEntry and locationEntry tables correctly
    public void testInsertReadDb() {
        long locationRowId;

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // LOCATION TABLE TEST //

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, "99705");
        values.put(LocationEntry.COLUMN_CITY_NAME, "North Pole");
        values.put(LocationEntry.COLUMN_COORD_LAT, 64.7488);
        values.put(LocationEntry.COLUMN_COORD_LONG, -147.353);

        // Insert values and check if inserted properly
        locationRowId = insertValuesAssert( db, LocationEntry.TABLE_NAME,  values);

        // If we inserted correctly in the previous test
        if (locationRowId != -1){
            // WEATHER TABLE TEST //

            // Fantastic.  Now that we have a location, add some weather!
            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
            weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
            weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
            weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
            weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
            weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
            weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
            weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
            weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
            weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

            // Insert values and check if inserted properly
            locationRowId = insertValuesAssert( db, WeatherEntry.TABLE_NAME,  weatherValues);

        } else {
            // That's weird, it works on MY machine...
            fail("Something went wrong inserting into LocationEntry...");
        }

        dbHelper.close();
    }
}
