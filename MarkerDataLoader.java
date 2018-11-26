import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;

/**
 * The MarkerDataLoader class loads the positions of each marker on the map from marker-positions.csv
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class MarkerDataLoader
{
    /**
     * Constructor for objects of class MarkerPositionsLoader
     */
    public HashMap<String, Marker> load()
    {
        //System.out.print("Begin loading Airbnb london dataset...");
        HashMap<String, Marker> markerPositions = new HashMap<String, Marker>();
        try{
            URL url = getClass().getResource("marker-positions.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String neighbourhood = line[0];
                int xposition = convertInt(line[1]);
                int yposition = convertInt(line[2]);

                Marker marker = new Marker(neighbourhood, xposition, yposition);
                markerPositions.put(neighbourhood, marker);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        //System.out.println("Success! Number of loaded records: " + listings.size());
        return markerPositions;
    }
    
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
}
