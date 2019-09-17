/*
 * Author: Hariharan Arunachalam
 * Date: Jul 12, 2016 (4:15:19 PM)
 * Explicit author permission required before this code is reused for any purpose - more like please let me know :)
 */
package dataloadermysql;

import entities.SurgeEventData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Hariharan
 */
public class PopDensityWeightedDataLoader extends DataLoader<SurgeEventData> {
    private int EventIdColumnIndex = 0;
    private int DayColumnIndex = 1;
    private int MonthColumnIndex = 2;
    private int YearColumnIndex = 3;

    private int CountryCodeColumnIndex = 4;
//    private int GeoCodeColumnIndex = 5;
    private int URLColumnIndex = 5;
    private int EventCategoryColumnIndex = 6;
    private int LatitudeColumnIndex = 7;
    private int LongitudeColumnIndex = 8;
    private int EventCountColumnIndex = 9;
    private int ColumnCount = 10;

    List<double[]> popDensities = new ArrayList<>();

    public PopDensityWeightedDataLoader(String popDensityDir) {
        super();
//      load the lat long pop density
        String latLongPopDensityFilename = popDensityDir;
        loadPopDensityFile(latLongPopDensityFilename);
    }
    private void loadPopDensityFile(String popDensityFilename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(popDensityFilename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] ls = line.split(",");
                double lat = Double.parseDouble(ls[0]);
                double lng = Double.parseDouble(ls[1]);
                double pd = Double.parseDouble(ls[2]);
                popDensities.add(new double[]{lat, lng, pd});
            }

        } catch (Exception ex) {
            System.out.printf("EXCEPTION: %s\n", ex.getMessage());
            Logger.getLogger(PopDensityWeightedDataLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        int k = 1;
    }

    @Override
    protected SurgeEventData extract(String line, EntityManager em, String filename) {
        String[] lineSplit = line.split("\t");
        if (lineSplit.length != ColumnCount) {
            System.out.println(lineSplit.length + ", ColumnCount:"+ ColumnCount);
            System.out.printf("Skipping line [%d] - (%s) in flle [%s]", lineSplit.length, line);
            return null;
        }
        long eventId = Long.parseLong(lineSplit[EventIdColumnIndex]);
        int day = Integer.parseInt(lineSplit[DayColumnIndex]);
        int month = Integer.parseInt(lineSplit[MonthColumnIndex]);
        int year = Integer.parseInt(lineSplit[YearColumnIndex]);
        Date currTime = new Date();

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, 9, 0, 0);
        Date date = c.getTime();
        String eventCategory = lineSplit[EventCategoryColumnIndex];

        BigDecimal lat = new BigDecimal(lineSplit[LatitudeColumnIndex].trim());
        BigDecimal lng = new BigDecimal(lineSplit[LongitudeColumnIndex].trim());

        String url = lineSplit[URLColumnIndex];
        String countryCode = lineSplit[CountryCodeColumnIndex];
   
//        String geoCode = lineSplit[GeoCodeColumnIndex];
        // attempt to split the geo code - 1 is only country, 2 would be state and country, 3 would be city, state country
//        String[] geoSplit = geoCode.split(",");

        String country = "";
        String state = "";
        String city = "";
        String countryName = readgdelt.ReadGDELT.COUNTRIES.get(countryCode);
                    
//      find out the closest population density. Currently, it is only available for IN, PK and BG, 
//      needs to be updated for all the countries
        double populationDensityAtPoint = -1.0 ;
        if(countryCode.equals("IN") || countryCode.equals("PK") || countryCode.equals("BG")){
            populationDensityAtPoint = getPopDensityAt(lat.doubleValue(), lng.doubleValue());
        }
        int eventsCount = Integer.parseInt(lineSplit[EventCountColumnIndex]);

        SurgeEventData sced = new SurgeEventData();

//      We need to catch the primary key violation in the surge_event_data table
//      might need to change this to:
//      com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
        EntityTransaction etx = em.getTransaction();
        etx.begin();
        try{
        EntityManagerHelpers.createEntity(sced, em);
        sced.setEventId(eventId);
        sced.setYear(year);
        sced.setMonth(month);
        sced.setDay(day);
        sced.setEventDate(date);
        sced.setEventCategory(eventCategory);
        sced.setGeoCity(city);
        sced.setGeoCountry(country);
        sced.setGeoState(state);
        sced.setPopulationDensity(populationDensityAtPoint);
        sced.setLatitude(lat);
        sced.setLongitude(lng);
        sced.setUrl(url);
        sced.setDataLoaded(currTime);
        sced.setCountryCode(countryCode);
        sced.setGeoCountry(countryName);
        sced.setDataSource("GDELT");
        sced.setEventCount(eventsCount);
        sced.setFilename(filename);        
        em.flush();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        etx.commit();
        return sced;
    }
    private final double EARTH_RADIUS = 6371e3;

    private double getPopDensityAt(double lat, double lng) {
        // iterate through the points till we hit something less than or equal to 0.25
        for (double[] popDensity : popDensities) {
            double dLat = Math.toRadians(popDensity[1] - lat);
            double dLng = Math.toRadians(popDensity[0] - lng);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(popDensity[1]))
                    * Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = EARTH_RADIUS * c;
            if (dist <= 500) {
                return popDensity[2];
            }
        }
        return -1.0;
    }

}
