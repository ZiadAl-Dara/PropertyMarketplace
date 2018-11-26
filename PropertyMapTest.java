import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The test class PropertyMapTest.
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class PropertyMapTest
{
    private PropertyMap propertyMap1, propertyMap2, propertyMap3;
    
    /**
     * Default constructor for test class PropertyMapTest
     */
    public PropertyMapTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        propertyMap1 = new PropertyMap(0, 10);
        propertyMap2 = new PropertyMap(20, 50);
        propertyMap3 = new PropertyMap(100, 200);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void TestFilterListingsByNeighbourhood()
    {
        AirbnbListing listing1 = propertyMap1.filterListingsByNeighbourhood("Barnet").get(0);
        assertNotNull(listing1.getId());
        AirbnbListing listing2 = propertyMap2.filterListingsByNeighbourhood("Barnet").get(0);
        assertNotNull(listing2.getId());
        AirbnbListing listing3 = propertyMap3.filterListingsByNeighbourhood("Barnet").get(0);
        assertNotNull(listing3.getId());
    }
    
    @Test
    public void TestGetAverageNumberOfReviews()
    {
        assertEquals(3, propertyMap1.getAverageNumberOfReviews());
        assertEquals(11, propertyMap2.getAverageNumberOfReviews());
        assertEquals(12, propertyMap3.getAverageNumberOfReviews());
    }
    
    @Test
    public void TestGetAverageNumberOfReviewsNegative()
    {
        assertFalse(propertyMap1.getAverageNumberOfReviews() == 5);
        assertFalse(propertyMap2.getAverageNumberOfReviews() == 10);
        assertFalse(propertyMap3.getAverageNumberOfReviews() == 43);
    }
    
    @Test
    public void TestGetTotalNumberOfListings()
    {
        assertEquals(17, propertyMap1.getTotalNumberOfListings());
        assertEquals(18472, propertyMap2.getTotalNumberOfListings());
        assertEquals(14029, propertyMap3.getTotalNumberOfListings());
    }
    
    @Test
    public void TestGetTotalNumberOfListingsNegative()
    {
        assertFalse(propertyMap1.getTotalNumberOfListings() == 10);
        assertFalse(propertyMap2.getTotalNumberOfListings() == 50);
        assertFalse(propertyMap3.getTotalNumberOfListings() == 27);
    }
    
    @Test
    public void TestGetNumberOfEntireHomesAndApartments()
    {
        assertEquals(4, propertyMap1.getNumberOfEntireHomesAndApartments());
        assertEquals(868, propertyMap2.getNumberOfEntireHomesAndApartments());
        assertEquals(12849, propertyMap3.getNumberOfEntireHomesAndApartments());
    }
    
    @Test
    public void TestGetNumberOfEntireHomesAndApartmentsNegative()
    {
        assertFalse(propertyMap1.getNumberOfEntireHomesAndApartments() == 10);
        assertFalse(propertyMap2.getNumberOfEntireHomesAndApartments() == 700);
        assertFalse(propertyMap3.getNumberOfEntireHomesAndApartments() == 500);
    }
    
    @Test
    public void TestGetPriciestNeighbourhood()
    {
        assertEquals("Hackney", propertyMap1.getPriciestNeighbourhood());
        assertEquals("Kensington and Chelsea", propertyMap2.getPriciestNeighbourhood());
        assertEquals("Merton", propertyMap3.getPriciestNeighbourhood());
    }
    
    @Test
    public void TestGetPriciestNeighbourhoodNegative()
    {
        assertFalse(propertyMap1.getPriciestNeighbourhood().equals("Merton"));
        assertFalse(propertyMap2.getPriciestNeighbourhood().equals("Merton"));
        assertFalse(propertyMap3.getPriciestNeighbourhood().equals("Hackney"));
    }
    
    @Test
    public void TestGetStandardDeviation()
    {
        assertEquals(1, propertyMap1.getStandardDeviation());
        assertEquals(9, propertyMap2.getStandardDeviation());
        assertEquals(30, propertyMap3.getStandardDeviation());
    }
    
    @Test
    public void TestGetStandardDeviationNegative()
    {
        assertFalse(propertyMap1.getStandardDeviation() == 10);
        assertFalse(propertyMap2.getStandardDeviation() == 10);
        assertFalse(propertyMap3.getStandardDeviation() == 50);
    }
    
    @Test
    public void TestGetListings()
    {
        AirbnbListing listing1 = propertyMap1.getListings().get(0);
        assertNotNull(listing1.getId());
        AirbnbListing listing2 = propertyMap2.getListings().get(0);
        assertNotNull(listing2.getId());
        AirbnbListing listing3 = propertyMap2.getListings().get(0);
        assertNotNull(listing3.getId());
    }
    
    @Test
    public void TestGetMarkers()
    {
        Marker marker1 = propertyMap1.getMarkers().get("Barnet");
        assertNotNull(marker1.getNeighbourhood());
        Marker marker2 = propertyMap2.getMarkers().get("Barnet");
        assertNotNull(marker2.getNeighbourhood());
        Marker marker3 = propertyMap3.getMarkers().get("Barnet");
        assertNotNull(marker3.getNeighbourhood());
    }
    
    @Test
    public void TestCalculateMarkerSizes()
    {
        // calculateMarkerSizes() is called in the PropertyMap() constructor so it is not explicitly called here.
        HashMap<String, Marker> markers1 = new HashMap<>(propertyMap1.getMarkers());
        for(Marker marker : markers1.values()) {
            assertNotNull(marker.getSize());
        }
        
        HashMap<String, Marker> markers2 = new HashMap<>(propertyMap2.getMarkers());
        for(Marker marker : markers2.values()) {
            assertNotNull(marker.getSize());
        }
        
        HashMap<String, Marker> markers3 = new HashMap<>(propertyMap3.getMarkers());
        for(Marker marker : markers3.values()) {
            assertNotNull(marker.getSize());
        }
    }
    
    @Test
    public void TestSortListingsByReviews()
    {
        ArrayList<AirbnbListing> sorted1 = new ArrayList<>(propertyMap1.sortListingsByReviews(propertyMap1.getListings()));
        for(int i = 0; i < sorted1.size() - 1; ++i) {
            int currentReviews = sorted1.get(i).getNumberOfReviews();
            int nextReviews = sorted1.get(i+1).getNumberOfReviews();
            assertTrue(currentReviews >= nextReviews);
        }
        
        ArrayList<AirbnbListing> sorted2 = new ArrayList<>(propertyMap2.sortListingsByReviews(propertyMap2.getListings()));
        for(int i = 0; i < sorted2.size() - 1; ++i) {
            int currentReviews = sorted2.get(i).getNumberOfReviews();
            int nextReviews = sorted2.get(i+1).getNumberOfReviews();
            assertTrue(currentReviews >= nextReviews);
        }
        
        ArrayList<AirbnbListing> sorted3 = new ArrayList<>(propertyMap3.sortListingsByReviews(propertyMap3.getListings()));
        for(int i = 0; i < sorted3.size() - 1; ++i) {
            int currentReviews = sorted3.get(i).getNumberOfReviews();
            int nextReviews = sorted3.get(i+1).getNumberOfReviews();
            assertTrue(currentReviews >= nextReviews);
        }
    }
    
    @Test
    public void TestSortListingsByPrice()
    {
        ArrayList<AirbnbListing> sorted1 = new ArrayList<>(propertyMap1.sortListingsByPrice(propertyMap1.getListings()));
        for(int i = 0; i < sorted1.size() - 1; ++i) {
            int currentPrice = sorted1.get(i).getPrice();
            int nextPrice = sorted1.get(i+1).getPrice();
            assertTrue(currentPrice >= nextPrice);
        }
        
        ArrayList<AirbnbListing> sorted2 = new ArrayList<>(propertyMap2.sortListingsByPrice(propertyMap2.getListings()));
        for(int i = 0; i < sorted2.size() - 1; ++i) {
            int currentPrice = sorted2.get(i).getPrice();
            int nextPrice = sorted2.get(i+1).getPrice();
            assertTrue(currentPrice >= nextPrice);
        }
        
        ArrayList<AirbnbListing> sorted3 = new ArrayList<>(propertyMap3.sortListingsByPrice(propertyMap3.getListings()));
        for(int i = 0; i < sorted3.size() - 1; ++i) {
            int currentPrice = sorted3.get(i).getPrice();
            int nextPrice = sorted3.get(i+1).getPrice();
            assertTrue(currentPrice >= nextPrice);
        }
    }
    
    @Test
    public void TestSortListingsByHost()
    {
        ArrayList<AirbnbListing> sorted1 = new ArrayList<>(propertyMap1.sortListingsByHost(propertyMap1.getListings()));
        for(int i = 0; i < sorted1.size() - 1; ++i) {
            String currentHost = sorted1.get(i).getHost_name();
            String nextHost = sorted1.get(i+1).getHost_name();
            assertTrue(currentHost.compareToIgnoreCase(nextHost) <= 0);
        }
        
        ArrayList<AirbnbListing> sorted2 = new ArrayList<>(propertyMap2.sortListingsByHost(propertyMap2.getListings()));
        for(int i = 0; i < sorted2.size() - 1; ++i) {
            String currentHost = sorted2.get(i).getHost_name();
            String nextHost = sorted2.get(i+1).getHost_name();
            assertTrue(currentHost.compareToIgnoreCase(nextHost) <= 0);
        }
        
        ArrayList<AirbnbListing> sorted3 = new ArrayList<>(propertyMap3.sortListingsByHost(propertyMap3.getListings()));
        for(int i = 0; i < sorted3.size() - 1; ++i) {
            String currentHost = sorted3.get(i).getHost_name();
            String nextHost = sorted3.get(i+1).getHost_name();
            assertTrue(currentHost.compareToIgnoreCase(nextHost) <= 0);
        }
    }
    
    @Test
    public void TestGetAveragePrice()
    {
        assertEquals(9, propertyMap1.getAveragePrice());
        assertEquals(37, propertyMap2.getAveragePrice());
        assertEquals(137, propertyMap3.getAveragePrice());
    }
    
    @Test
    public void TestGetAveragePriceNegative()
    {
        assertFalse(propertyMap1.getAveragePrice() == 30);
        assertFalse(propertyMap2.getAveragePrice() == 10);
        assertFalse(propertyMap3.getAveragePrice() == 100);
    }
    
    @Test
    public void TestGetAveragePriceSharedRoom()
    {
        assertEquals(9, propertyMap1.getAveragePriceSharedRoom());
        assertEquals(30, propertyMap2.getAveragePriceSharedRoom());
        assertEquals(132, propertyMap3.getAveragePriceSharedRoom());
    }
    
    @Test
    public void TestGetAveragePriceSharedRoomNegative()
    {
        assertFalse(propertyMap1.getAveragePriceSharedRoom() == 50);
        assertFalse(propertyMap2.getAveragePriceSharedRoom() == 20);
        assertFalse(propertyMap3.getAveragePriceSharedRoom() == 100);
    }
    
    @Test
    public void TestGetAveragePricePrivateRoom()
    {
        assertEquals(9, propertyMap1.getAveragePricePrivateRoom());
        assertEquals(36, propertyMap2.getAveragePricePrivateRoom());
        assertEquals(124, propertyMap3.getAveragePricePrivateRoom());
    }
    
    @Test
    public void TestGetAveragePricePrivateRoomNegative()
    {
        assertFalse(propertyMap1.getAveragePricePrivateRoom() == 30);
        assertFalse(propertyMap2.getAveragePricePrivateRoom() == 26);
        assertFalse(propertyMap3.getAveragePricePrivateRoom() == 100);
    }
}