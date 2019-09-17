/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgdelt;

import dataloadermysql.DataLoaderMySQL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author sudbasnet
 */
public class ReadGDELT {
//    We will only import data with the following country codes
    public static Map<String, String> COUNTRIES = new HashMap<String, String>(){{
        put("IN", "India");
        put("PK", "Pakistan");
        put("BG", "Bangladesh");
        put("AF", "Afganistan");
        put("IR", "Iran");
        put("IZ", "Iraz");
        put("SY", "Syria");
        put("TU", "Turkey");
        put("SA", "Saudi Arabia");
        put("ET", "Ethiopia");
        put("OD", "South Sudan");
        put("CT", "Central African Republic");
        put("CM", "Cameroon");
        put("NI", "Nigeria");
        put("GH", "Ghana");
        put("VM", "Vietnam");
        put("LA", "Laos");
        put("CB", "Cambodia");
        put("MY", "Malaysia");
    }};
    
    public static String readGdeltCsv(String readDir, String writeDir) throws FileNotFoundException, IOException {
        int rawCount = 0;
        int extractCount = 0;
        String sourceUrl = null;
        String returnFilePath = null;
        BufferedReader br = null;				
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter out = null;
        DateTimeFormatter ft = DateTimeFormat.forPattern("yyyyMMdd");
        try {
            String sCurrentLine;
            File file1 = new File(readDir);
            String name = file1.getName();
            int filenameLength = name.length();
            // only read .CSV file
            if (".CSV".equals(name.substring(filenameLength-4).toUpperCase())){
                br = new BufferedReader(new FileReader(file1));
                // create the output filename
                char[] chr = name.toCharArray();
                StringBuffer n = new StringBuffer();
                for(int c = 0; c < chr.length; c++){
                    if(chr[c] != '.'){
                        n.append(Character.toString(chr[c]));
                    } else
                        break;
                }
                // put the filename into the write directory
                returnFilePath = writeDir + n + ".txt";
                fw = new FileWriter(returnFilePath, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                    
                while ((sCurrentLine = br.readLine()) != null) {
                    rawCount++;
                    String[] attr = sCurrentLine.replaceAll("\t\"",",").replaceAll("\"\t",",").replaceAll("\"",",").split("\t");
                    
//                  Only explore the data for countries that are in the countries HashMap
                    if(COUNTRIES.containsKey(attr[51])){
                        extractCount++;
                        if (DataLoaderMySQL.currentDay.isBefore(ft.parseDateTime("20130401"))){
                            // there is no sourceURL in data before 20130401
                            sourceUrl = "no url";
                        } else {
                            sourceUrl = attr[57];
                        }
                            out.write(attr[0]  + "\t" + 
                                    attr[1].substring(6, 8)  + "\t" + 
                                    attr[1].substring(4, 6)  + "\t" + 
                                    attr[3]  + "\t" +
                                    attr[26] + "\t" +
                                    attr[34] + "\t" +
                                    attr[51] + "\t" + 
                                    attr[49] + "\t" + 
                                    attr[50] + "\t" +
                                    attr[53] + "\t" + 
                                    attr[54] + "\t" + 
                                    sourceUrl + "\n");
                    }
                } //end while
                DataLoaderMySQL.downloadedFileRawCount = rawCount;
                DataLoaderMySQL.extractFileRawCount = extractCount;
                out.close();
            }//end if CSV 
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
                if(bw != null) bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return returnFilePath;
    }
    public static void deleteCSV(String csvDir){
        File f = new File(csvDir);
        f.delete();
    }
}