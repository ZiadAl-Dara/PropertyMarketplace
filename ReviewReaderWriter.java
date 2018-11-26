import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;

/**
 * The ReviewReaderWriter class reads and writes to the reviews.csv file
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class ReviewReaderWriter
{
    public ArrayList<Review> read(String propertyID)
    {
        ArrayList<Review> reviews = new ArrayList<Review>();
        try{
            URL url = getClass().getResource("reviews.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String author = line[1];
                int rating = convertInt(line[2]);
                String comment = line[3];

                Review review = new Review(id, author, rating, comment);
                
                if(propertyID.equals("")) {
                    reviews.add(review);
                } else if(id.equals(propertyID)) {
                    reviews.add(review);
                }
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        return reviews;
    }
    
    /**
     * Writes to reviews.csv
     */
    public void write(String propertyID, String reviewerName, int rating, String review)
    {
        try {
            String filename= "reviews.csv";
            FileWriter fileWriter = new FileWriter(filename,true);
            fileWriter.write(System.getProperty("line.separator"));
            fileWriter.write(propertyID + "," + reviewerName + ","+ rating + "," + review);
            fileWriter.close();
        }
        catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
    
    /**
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return -1;
    }
}