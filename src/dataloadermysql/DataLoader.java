/*
 * Author: Hariharan Arunachalam
 * Date: Jun 26, 2016 (10:10:24 PM)
 * Explicit author permission required before this code is reused for any purpose - more like please let me know :)
 */
package dataloadermysql;

import static dataloadermysql.DataLoaderMySQL.aggregateFileName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Hariharan
 */
public abstract class DataLoader<T> {

    private int updateEveryRowT = 100;

    protected abstract T extract(String line, EntityManager em, String filename);

    public void PerformLoad(EntityManagerFactory emf, String inputFileLocation) {
//        System.out.printf("Starting data loader [%s]\n", this.toString());
        // get all the txt files in the location
        File file1 = new File(inputFileLocation);
        FilenameFilter extensionFilter = new FilenameFilter() {
            private String filterString = "txt";

            public boolean accept(File f, String s) {
                boolean r = s.endsWith(filterString);
                return r;
            }
        };

        String filename = file1.getName();
        EntityManager em = emf.createEntityManager();
//        System.out.printf("Begin current file: %s\n", filename);
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileLocation))) {
            String line;
            long linesProcessed = 0;
            while ((line = br.readLine()) != null) {
                linesProcessed++;
                T entityLoaded = extract(line, em, filename);

                if (linesProcessed % updateEveryRowT == 0) {
                    System.out.printf("\t%d entries processed [%d]\n", updateEveryRowT, linesProcessed);
                }
            }
            DataLoaderMySQL.aggregatedFileRawCount = (int) linesProcessed;
            DataLoaderMySQL.aggregateFileName = filename;
        } catch (Exception ex) {
            System.out.printf("EXCEPTION: %s\n", ex.getMessage());
        }

        if (em != null && em.isOpen()) {
            em.close();
        };
    }

}
