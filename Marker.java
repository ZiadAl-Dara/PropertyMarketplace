/**
 * The Marker class represents a single marker that is displayed on the map.
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class Marker
{
    private String neighbourhood;
    private int xposition;
    private int yposition;
    private Integer size;
    private int numberOfListings;

    /**
     * Constructor for objects of class MarkerPosition
     */
    public Marker(String neighbourhood, int xposition, int yposition)
    {
        this.neighbourhood = neighbourhood;
        this.xposition = xposition;
        this.yposition = yposition;
        numberOfListings = 0;
        size = null;
    }
    
    public String getNeighbourhood()
    {
        return neighbourhood;
    }
    
    public int getXPosition()
    {
        return xposition;
    }
    
    public int getYPosition()
    {
        return yposition;
    }
    
    public Integer getSize()
    {
        return size;
    }
    
    /**
     * Increase the number of listings by one.
     */
    public void incrementListings()
    {
        numberOfListings++;
    }
    
    public int getNumberOfListings()
    {
        return numberOfListings;
    }
    
    /**
     * Calculates size of marker. Every 500 listings is an extra 1 pixel.
     */
    public void calculateSize()
    {
        size = numberOfListings/500;
    }
}
