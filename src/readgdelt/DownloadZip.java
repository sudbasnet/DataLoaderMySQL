/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readgdelt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.File.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 *
 * @author sudbasnet
 */
public class DownloadZip {
    
    public static void downloadZIP(String url, String writeDir){
        try{
        downloadUsingStream(url, writeDir);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private static void downloadUsingStream(String urlStr, String writeDir) throws IOException{
        URL url = new URL(urlStr);
        try (BufferedInputStream bis = new BufferedInputStream(url.openStream()); 
                FileOutputStream fis = new FileOutputStream(writeDir)) {
            byte[] buffer = new byte[1024];
            int count=0;
            while((count = bis.read(buffer,0,1024)) != -1)
            {
                fis.write(buffer, 0, count);
            }
        }
    }
    
    public static void deleteZip(String zipDir) throws FileNotFoundException{
        File f = new File(zipDir);
        f.delete();   
    }
}

