### Download GDELT data for selective countries and CAMEO codes and insert into established database.

This program was first created by Dr. Deepti Joshi, Hariharan Arunachalam. The program was later put together in one place and modified by Sudeep Basnet and Sanat Bhandari. This program does the following:

1. Checks the GDELT's website "http://data.gdeltproject.org/events/\<filename.zip\>" for new dataset on social unrest events.
2. Downloads the **.zip** file.
3. Extrats the **.csv\_** file from the zip.
4. Deletes the .zip file, and extracts only the required fields from the csv file and puts them into **.txt** files. Then deletes the .csv files as well.
5. Aggregates the data by putting events of same _*event_category*_ occuring on the same _*event_date*_ at the exact same location (_*latitude, longitude*_) as one record. The source data initially contained one URL explaining each of these events, but the aggregate just picks one of the URLs assuming all the events occuring at the same place, same time and same category are closely related. This URL is not used anywhere in the application yet. After the data is aggregated, this data is thown into new **.txt** files.
6. Next, the data is imported into the **surge_event_data** table of the surge@cse.unl.edu mysql database. While the data is imported, each coordinate of the data is checked against a Population Density file. This file contains the population density of the countries India, Pakistan, and Bangladesh. The data in each row is in the format _*longitude, latitude, population density*_. The GDELT's data for only India, Pakistan and Bangladesh is checked against this population density file and the population density numbers are pulled into the surge@cse.unl.edu's mysql database table along with the original data as an added column.

### Population Density data file

Please see the following instructions to get the Population Density. These are the initial instructions shared when the project was started.

Main page with the data information
-  https://daac.ornl.gov/ISLSCP_II/guides/global_population_xdeg.html
ISLSCP II Global Population of the World - ORNL DAAC daac.ornl.gov
Summary: Global Population of the World (GPW) translates census population data to a latitude-longitude grid so that population data may be used in cross-disciplinary ...

1.  If you click GetData on the page in step 1, we get to  http://daac.ornl.gov/cgi-bin/dsviewer.pl?ds_id=975. On this page, we go through the Spatial Data Access Tool link to reach http://webmap.ornl.gov/wcsdown/wcsdown.jsp?dg_id=975_3.

    ISLSCP II Global Population of the World doi:10.3334 ... daac.ornl.gov

    Description. Global Population of the World (GPW) translates census population data to a latitude-longitude grid so that population data may be used in cross ...  
    \*\*Note: The Spatial Data Access Tool link leads to a page with multiple different links to maps that encompass multiple different variables. I assume that it is one of the population based maps, but I am not sure which one.

2.  From the page we end up in after step 2, we have a section named "Data Customization and Download". On the map, I use the Spatial Extent tool (\[select_inactive.PNG\] ) to select the area around India, Pakistan, Bangladesh with a little buffer zone. And then I downloaded the data.

    \*\*Note: I use a population based map to try and at least download the data to get an idea of what the data looks like.


    **Note: When I use the "Spatial Extent tool" that is in the maps and designate the area (with a buffer). I get the file that is attached to this e-mail. The output that I get is some type of map-graphic but doesn't provide any latitudinal-longitudinal data. 

3. Results in a file that has the population density for every coordinate where each coordinate is at a 0.25 degree resolution. This is then used to calculate the population density for each event data point we have in the database by lookingHowToGetTheData.gif for the closest point with the population density for out coordinate of interest.

### Important:

If you want to run this program you need to edit the **persistance.xml** file and fill up the password value with the correct one. Members of the SURGE project should have access to this information.

```xml
    <property name="javax.persistence.jdbc.user" value="surge"/>
    <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
    <property name="javax.persistence.jdbc.password" value="******"/>
```

There are many things that can be improved in the program and perhaps can be replaced by any existing APIs from GDELT project. Please feel free to make changes to the program, this program was built as a quick way of downloading the GDELT data and visualizing the data on the web-application https://cse.unl.edu/~surge. So there are many improvements that can be made. The command is:

```sh
java -jar "DataLoaderMySQL.jar" "<startdate YYYYMMDD>" "<enddate YYYYMMDD> (optional)" "</path/to/populationDensity/file> (optional)"
```

**Note:** This program is built into a _*.jar*_ file, it runs every night at 00:00 am through cronjob in the cse.unl.edu server.

### Other Details

The repo is a NetBeans project.
The `DataLoaderMySQL.java` has the `main` method.
