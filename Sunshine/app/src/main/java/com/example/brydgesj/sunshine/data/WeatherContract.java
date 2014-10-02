package com.example.brydgesj.sunshine.data;

import android.provider.BaseColumns;

/**
 * Created by jbrydges on 10/1/2014.
 */
public class WeatherContract {
    public static final class WeatherEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_LOC_KEY = "location_id";
        public static final String COLUMN_DATETEXT = "date";
        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        //float
        public static final String COLUMN_MIN_TEMP = "min";
        //float
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_WIND_SPEED = "wind";
        public static final String COLUMN_DEGREES = "degrees";
    }

    public static final class LocationEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "location";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_LOCATION_SETTING = "location_setting";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_CITY_NAME = "city_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }
}
