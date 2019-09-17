/*
 * Author: Hariharan Arunachalam, Sudeep Basnet
 * Date: May 31, 2016 (5:00:18 PM), Nov 11-2017
 * Explicit author permission required before this code is reused for any purpose - more like please let me know :)

 * This program will download the data from GDELT's repository and insert into the table "surge_event_data" in the SURGE's local host database
 * I advise not to use this database for actual research purpose, this is more for visualization
 * For research, you should modify this program to use the raw dataset from GDELT instead 
 */
package dataloadermysql;

import readgdelt.Aggregate;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import readgdelt.*;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import org.joda.time.*;
import org.joda.time.format.*;
import entities.ImportLog;
/**
 *
 * @author Hariharan, SudBasnet
 */
public class DataLoaderMySQL {
    public static String downloadlink = "";
    public static int downloadedFileRawCount = 0;
    public static int extractFileRawCount = 0;
    public static String aggregateFileName = "";
    public static int aggregatedFileRawCount = 0;
    
    private static int limitCount = 400;
    private static DateTimeFormatter ft_month = DateTimeFormat.forPattern("yyyyMM");
    private static DateTimeFormatter ft_day = DateTimeFormat.forPattern("yyyyMMdd");
    private static DateTimeFormatter ft_year = DateTimeFormat.forPattern("yyyy");
    public static DateTime currentDay;
    
    // give location of folders here: 
    private static String popDensityFileLocation = "/home/fac/surge/DataUploader/dist/popDensityFile/poplationdensity.xyz";
    private static String extractLocation = "/home/fac/surge/DataUploader/dist/importFiles/";
    private static String aggregateLocation = "/home/fac/surge/DataUploader/dist/aggregateFiles/";
    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    
//    We are going to pass arguments to the jar file built from the program
    public static void main(String[] args) throws ParseException, IOException {
        DateTime todaysDate = new DateTime();
        DateTime yesterdaysDate = todaysDate.minusDays(2);
        String yesterdaysDateString = yesterdaysDate.toString(ft_day);
        
//      by default, the start and enddate is yesterday's date'
        String startingDate = yesterdaysDateString;
        String endDate = yesterdaysDateString;
        
//      we can provide just the startdate or both start and enddate
        if(args.length == 1) {
            startingDate = args[0];
        } else if (args.length == 2){
            startingDate = args[0];
            endDate = args[1];
        }
        
        // if extract location does not exist, program assumes you are not on the CSE server
        // we create a new folder in the local drive
        File impLoc = new File(extractLocation);
        if(!impLoc.exists()){
            File imp = new File("./importLocation/");
            if(!imp.exists()) imp.mkdir();
            extractLocation = "./importLocation/";
        }
        
        // if aggregate location does not exist, program assumes you are not on the CSE server
        // we create a new folder in the local drive
        File aggLoc = new File(aggregateLocation);
        if(!aggLoc.exists()){
            File agg = new File("./aggregateLocation/");
            if(!agg.exists()) agg.mkdir();
            aggregateLocation = "./aggregateLocation/";
        }

        File popDenLoc = new File(popDensityFileLocation);
        if(!popDenLoc.exists()){
            File popDen = new File("./popDensityFileLocation/");
            File xyz = new File("./popDensityFileLocation/a.xyz");
            if(!popDen.exists()) popDen.mkdir();
            if(!xyz.exists()) xyz.createNewFile();
//            dummy file created 
            popDensityFileLocation = "./popDensityFileLocation/a.xyz";
        }
        
//      This calls the main import function with the start and enddates
        setTimeFrame(startingDate, endDate);
        
        System.out.println("downloadlink: "+ downloadlink);
        System.out.println("aggregateFileName: "+ aggregateFileName);
        System.out.println("downloadedFileRawCount: "+ downloadedFileRawCount);
        System.out.println("extractFileRawCount: "+ extractFileRawCount);
        System.out.println("aggregatedFileRawCount: "+ aggregatedFileRawCount);

        /*      
        * The entity manager is what puts the data into the database
        * The main entity manager is in the PopDensityWeightedDataLoader.java
        * We also use one to keep log of downloaded file here 
        */
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("DataLoaderMySQLPU");
        EntityManager entitymanager = emfactory.createEntityManager();
        entitymanager.getTransaction().begin();

        ImportLog logger = new ImportLog( ); 
        logger.setDownloadUrl(downloadlink);
        logger.setFileName(aggregateFileName);
        Date todate = new Date();
        logger.setImportDate(todate);
        logger.setDownloadedRows(downloadedFileRawCount);
        logger.setExtractRows(extractFileRawCount);
        logger.setAggregatedRows(aggregatedFileRawCount);
        logger.setFileDate(yesterdaysDateString);

        entitymanager.persist(logger);
        entitymanager.getTransaction().commit();
        entitymanager.close();
        emfactory.close();
    }
    
    public static void setTimeFrame(String startdate, String enddate) throws ParseException, IOException{
        String filename = null;
        String downloadedFile = null;
        String convertedFile = null;
        String aggregateFile = null;
        String filename_extension = null;
        DateTimeFormatter ft = ft_day;
        String update = "daily";
        DateTime startdate_d = ft.parseDateTime(startdate);
        DateTime enddate_d = ft.parseDateTime(enddate);   
        currentDay = startdate_d;
        
        while (currentDay.isBefore(enddate_d.plusDays(1))){
            if(currentDay.isBefore(ft_day.parseDateTime("20060101"))){
                ft = ft_year; 
                update = "yearly";
                filename_extension = "";
            } else if (currentDay.isAfter(ft_day.parseDateTime("20051231")) && currentDay.isBefore(ft_day.parseDateTime("20130401"))){
                ft = ft_month;
                update = "monthly";
                filename_extension = "";
            } else {
                ft = ft_day;
                update = "daily";
                filename_extension = ".export.CSV";
            }
            
            // create the file's name
            filename = currentDay.toString(ft);
            
            // download the file
            try{
                getFile(filename + filename_extension, extractLocation);
            } catch (Exception e){
                System.out.println(filename + filename_extension + " not found.");
            } finally {
                downloadedFile = extractLocation + filename + filename_extension ;

                // convert the csv file
                convertedFile = ReadGDELT.readGdeltCsv(downloadedFile, extractLocation);
                ReadGDELT.deleteCSV(downloadedFile); // deleting the previous file to save space

                //aggregate the file
                File agg = new File(aggregateLocation);
                   if (!agg.exists() || !agg.isDirectory()){
                       new File(aggregateLocation).mkdir();
                   }
                aggregateFile = Aggregate.aggregateTXT(convertedFile, aggregateLocation);
                ReadGDELT.deleteCSV(convertedFile); // deleting the previous file to save space

                //process the aggregate-file
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("DataLoaderMySQLPU");
                PopDensityWeightedDataLoader pdwdl = new PopDensityWeightedDataLoader(popDensityFileLocation);
                pdwdl.PerformLoad(emf, aggregateFile);
            }
            //next file's name
            switch (update) {
                case "monthly":
                    currentDay = currentDay.plusMonths(1);
                    break;
                case "yearly":
                    currentDay = currentDay.plusYears(1);
                    break;
                case "daily":
                    currentDay = currentDay.plusDays(1);
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void getFile(String csvName, String targetDir){
        String filename = csvName + ".zip";
        String downloadDir = targetDir; //the directory where downloaded files are kept
        //Download Data from http://data.gdeltproject.org/events/index.html    
        downloadlink = ("http://data.gdeltproject.org/events/"+filename);
        DownloadZip.downloadZIP("http://data.gdeltproject.org/events/"+filename, downloadDir+filename);
        // extract the ZIP file
        try {
            UnzipUtility.unzip(downloadDir+filename, downloadDir);
            DownloadZip.deleteZip(downloadDir+filename); // delete the downloaded zip file   
        } catch (IOException ex) {
            Logger.getLogger(DataLoaderMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
