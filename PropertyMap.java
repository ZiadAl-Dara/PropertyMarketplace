import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Map class represents a list of properties that are currently being displayed on the map, based on the users search parameters.
 * This class is also responsible for calculating various statistics based on the listings stored within the map.
 *  
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class PropertyMap
{
    private ArrayList<AirbnbListing> listings;
    private int lowerLimit, upperLimit;
    private AirbnbDataLoader airbnbLoader;
    private HashMap<String, Marker> markers;
    
    /**
     * Constructor for objects of class Map
     */
    public PropertyMap(int lowerLimit, int upperLimit)
    {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        listings = new AirbnbDataLoader().load();
        listings.removeIf(listing -> listing.getPrice() < lowerLimit || listing.getPrice() > upperLimit);
        markers = new MarkerDataLoader().load();
        calculateMarkerSizes();
    }
    
    /**
     * Removes all of the listings that are not within the specified neighbourhood. Used when the user clicks on an icon on the map.
     */
    public ArrayList<AirbnbListing> filterListingsByNeighbourhood(String neighbourhood)
    {
        ArrayList<AirbnbListing> filteredListings = new ArrayList<>(listings);
        filteredListings.removeIf(listing -> !listing.getNeighbourhood().equals(neighbourhood));
        return filteredListings;
    }
    
    /**
     * Returns the average number of reviews of the listings on the map.
     */
    public int getAverageNumberOfReviews()
    {
        if (listings.size() == 0)
            return 0;
        int total = 0;
        for(AirbnbListing listing : listings)
            total += listing.getNumberOfReviews();
        return total/listings.size();
    }
    
    /**
     * Returns the total number of listings on the map.
     */
    public int getTotalNumberOfListings()
    {
        return listings.size();
    }
    
    /**
     * Returns the total number of listings which have the type "Entire home/apt"
     */
    public int getNumberOfEntireHomesAndApartments()
    {
        int total = 0;
        for(AirbnbListing listing : listings) {
            if(listing.getRoom_type().equals("Entire home/apt"))
                total++;
        }
        return total;
    }
    
    /**
     * Calculates the priciest neighbourhood on the map.
     */
    public String getPriciestNeighbourhood()
    {
        // Creates a HashMap of neighbourhood to number of properties in the neighbourhood
        HashMap<String, Integer> numberPropertiesInNeighbourhood = new HashMap<>();
        for(AirbnbListing listing : listings) {
            String neighbourhood = listing.getNeighbourhood();
            if(numberPropertiesInNeighbourhood.containsKey(listing.getNeighbourhood())) {
                int newValue = numberPropertiesInNeighbourhood.get(neighbourhood) + 1;
                numberPropertiesInNeighbourhood.put(listing.getNeighbourhood(), newValue);
            } else {
                numberPropertiesInNeighbourhood.put(listing.getNeighbourhood(), 1);
            }
        }
        // Creates a hashmap of neighbourhood to the sum of the product of each listing in the neighbourhood's price and min nights
        HashMap<String, Integer> neighbourhoods = new HashMap<>();
        for(AirbnbListing listing : listings) {
            String neighbourhood = listing.getNeighbourhood();
            if(neighbourhoods.containsKey(neighbourhood)) {
                int newValue = neighbourhoods.get(neighbourhood) + (listing.getPrice() * listing.getMinimumNights());
                neighbourhoods.replace(neighbourhood, newValue);
            } else {
                neighbourhoods.put(neighbourhood, listing.getPrice() * listing.getMinimumNights());
            }
        }
        // Divide each neighbourhood's value in neighbourhoods by its value in numberPropertiesInNeighbourhood
        for(String neighbourhood : neighbourhoods.keySet()) {
            int newValue = neighbourhoods.get(neighbourhood) / numberPropertiesInNeighbourhood.get(neighbourhood);
            neighbourhoods.replace(neighbourhood, newValue);
        }
        // Find the highest value in the neighbourhoods HashMap
        int greatest = 0;
        String priciestNeighbourhood = "";
        for(String neighbourhood : neighbourhoods.keySet()) {
            if(neighbourhoods.get(neighbourhood) > greatest) {
                greatest = neighbourhoods.get(neighbourhood);
                priciestNeighbourhood = neighbourhood;
            }
        }
        return priciestNeighbourhood;
    }
    
    /**
     * Returns the standard deviation of all of the listings currently on the map.
     */
    public int getStandardDeviation()
    {
        int total = 0;
        if(listings.size() == 0)
            return 0;
        for(AirbnbListing listing : listings)
            total += listing.getPrice();
        double averagePrice = total/listings.size();
        double sum = 0;
        for(AirbnbListing listing : listings)
            sum += (listing.getPrice() - averagePrice) * (listing.getPrice() - averagePrice);
        return (int) Math.round(Math.sqrt(sum/listings.size()));
    }
    
    /**
     * Return an array of all listings.
     */
    public ArrayList<AirbnbListing> getListings()
    {
        return listings;
    }
    
    /**
     * Calculates the size of each marker that is displayed on the map, based on the number of listings in that neighbourhood.
     */
    private void calculateMarkerSizes()
    {
        // Count number of listings in each neighbourhood
        for(AirbnbListing listing : listings) {
            String neighbourhood = listing.getNeighbourhood();
            markers.get(neighbourhood).incrementListings();
        }
        // Calculate size of marker given the number of listings in the neighbourhood
        for(String neighbourhood : markers.keySet()) {
            markers.get(neighbourhood).calculateSize();
        }
    }
    
    /**
     * Returns all markers on the map.
     */
    public HashMap<String, Marker> getMarkers() {
        return markers;
    }
    
    /**
     * Sorts all of the current listings by the number of reviews.
     */
    public ArrayList<AirbnbListing> sortListingsByReviews(ArrayList<AirbnbListing> listings) {
        ArrayList<AirbnbListing> tempListings = new ArrayList<>(listings);
        ArrayList<AirbnbListing> orderedListings = new ArrayList<>();
        while(!tempListings.isEmpty()) {
            int highestNumberOfReviews = 0;
            AirbnbListing listingWithHighestReviews = null;
            for(AirbnbListing listing : tempListings) {
                if(listing.getNumberOfReviews() >= highestNumberOfReviews) {
                    highestNumberOfReviews = listing.getNumberOfReviews();
                    listingWithHighestReviews = listing;
                }
            }
            orderedListings.add(listingWithHighestReviews);
            tempListings.remove(listingWithHighestReviews);
        }
        return orderedListings;
    }
    
    /**
     * Sorts all of the current listings by current price.
     */
    public ArrayList<AirbnbListing> sortListingsByPrice(ArrayList<AirbnbListing> listings) {
        ArrayList<AirbnbListing> tempListings = new ArrayList<>(listings);
        ArrayList<AirbnbListing> orderedListings = new ArrayList<>();
        while(!tempListings.isEmpty()) {
            int highestPrice = 0;
            AirbnbListing listingWithHighestPrice = null;
            for(AirbnbListing listing : tempListings) {
                if(listing.getPrice() >= highestPrice) {
                    highestPrice = listing.getPrice();
                    listingWithHighestPrice = listing;
                }
            }
            orderedListings.add(listingWithHighestPrice);
            tempListings.remove(listingWithHighestPrice);
        }
        return orderedListings;
    }
    
    /**
     * Sorts all of the listings by the Host name.
     */
    public ArrayList<AirbnbListing> sortListingsByHost(ArrayList<AirbnbListing> listings) {
        ArrayList<AirbnbListing> tempListings = new ArrayList<>(listings);
        ArrayList<AirbnbListing> orderedListings = new ArrayList<>();
        while(!tempListings.isEmpty()) {
            String firstHost = tempListings.get(0).getHost_name();
            AirbnbListing listingWithFirstHost = null;
            for(AirbnbListing listing : tempListings) {
                String listingHostName = listing.getHost_name();
                if(listingHostName.compareToIgnoreCase(firstHost) <= 0) {
                    firstHost = listingHostName;
                    listingWithFirstHost = listing;
                }
            }
            orderedListings.add(listingWithFirstHost);
            tempListings.remove(listingWithFirstHost);
        }
        return orderedListings;
    }
    
    /**
     * Returns the average price of all the listings that are currently shown on the map.
     */
    public int getAveragePrice()
    {
        if (listings.size() == 0)
            return 0;
        int total = 0;
        for(AirbnbListing listing : listings)
            total += listing.getPrice();
        return total/listings.size();
    }
    
    /**
     * Returns the average price of all shared rooms on the map.
     */
    public int getAveragePriceSharedRoom()
    {
        int sum = 0;
        int numSharedRooms = 0;
        for(AirbnbListing listing : listings) {
            if(listing.getRoom_type().equals("Shared room")) {
                numSharedRooms++;
                sum += listing.getPrice();
            }
        }
        if (numSharedRooms == 0)
            return 0;
        return sum/numSharedRooms;
    }
    
    /**
     * Returns the average price of all private rooms on the map.
     */
    public int getAveragePricePrivateRoom()
    {
        int sum = 0;
        int numPrivateRooms = 0;
        for(AirbnbListing listing : listings) {
            if(listing.getRoom_type().equals("Private room")) {
                numPrivateRooms++;
                sum += listing.getPrice();
            }
        }
        if (numPrivateRooms == 0)
            return 0;
        return sum/numPrivateRooms;
    }
    
    /**
     * This method works out which sorting type to use.
     * @return the sorted array
     */
    public ArrayList<AirbnbListing> sort(String sortingType, String neighbourhood)
    {
        ArrayList<AirbnbListing> sorted;
        if(sortingType.equals("Number of Reviews"))
            sorted = new ArrayList<>(sortListingsByReviews(filterListingsByNeighbourhood(neighbourhood)));
        else if(sortingType.equals("Price"))
            sorted = new ArrayList<>(sortListingsByPrice(filterListingsByNeighbourhood(neighbourhood)));
        else
            sorted = new ArrayList<>(sortListingsByHost(filterListingsByNeighbourhood(neighbourhood)));
        return sorted;
    }
}