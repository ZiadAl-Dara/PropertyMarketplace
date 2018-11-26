import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

/**
 * The graphical user interface for the application.
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class GUI extends JFrame implements ActionListener
{
    // The application frame.
    private JFrame frame;
    // The container of the window in which all the panels are placed.
    private Container contentPane;
    // Array of prices for the user to choose a range.
    private Integer[] prices = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 8000};
    // Buttons in the application.
    private JButton leftButton, rightButton, submit;
    // Panels in the application.
    private JPanel topBar, mainPanel, statsPanel, reviewPanel, navigationBar, markersPanel, instructions;
    // Layered panels in the application.
    private JLayeredPane welcomePanel, mapPanel;
    // Drop down menus to select price range.
    private JComboBox<Integer> from, to;
    // Labels in the application.
    private JLabel fromLabel, toLabel, neighbourhoodName;
    // Keeps track of the panel number. 1 = Welcome, 2 = Map, 3 = Statistics.
    private Integer panelNumber = 1;
    // The initial price range.
    private Integer fromPrice = 0, toPrice = 20;
    // The logic/backend of the map.
    private PropertyMap map;
    // There is no selected property initially.
    private String selectedProperty = "";
    // The review reader/writer.
    private ReviewReaderWriter reviewReaderWriter;

    /**
     * Constructor of GUI class.
     */
    public GUI()
    {
        frame = new JFrame("London Property Marketplace");
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(900, 791));
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        reviewReaderWriter = new ReviewReaderWriter();

        createTopBar(); // Create top bar
        createMainPanel(); // Create the main panel
        createNavigationBar(); // Create navigation bar

        frame.pack();
        frame.setVisible(true);
        play("welcome_sound");
    }
    
    /**
     * Create the top bar of the application window. This contains the upper and lower bound price
     * drop-down menus and a submit button for confirming price range selection.
     */
    private void createTopBar()
    {
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        JPanel selection = new JPanel();
        topBar.add(selection, BorderLayout.EAST);
        selection.setLayout(new BoxLayout(selection, BoxLayout.X_AXIS));

        from = new JComboBox(prices); // lower price bound drop-down
        to = new JComboBox(prices); // upper price bound drop-down
        to.setSelectedItem(20); // sets initial display value in upper bound box to 20
        submit = new JButton("Submit");
        submit.addActionListener(this);
        
        selection.add(new JLabel("From:"));
        selection.add(from);
        selection.add(new JLabel("To:"));
        selection.add(to);
        selection.add(submit);
        
        contentPane.add(topBar, BorderLayout.NORTH);
    }
    
    /**
     * Creates the main panel where all the main application features are implemented. 
     * This uses a card layout which fits four distinct panels: welcome, map, statistics 
     * and review read/write.
     */
    private void createMainPanel()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());

        welcomePanel = new JLayeredPane();
        mapPanel = new JLayeredPane();
        statsPanel = new JPanel();
        reviewPanel = new JPanel();
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(mapPanel, "Map");
        mainPanel.add(statsPanel, "Statistics");
        mainPanel.add(reviewPanel, "Fourth Panel");

        createWelcomePanel();
        createMapPanel();
        createStatsPanel();
        createReviewPanel();
        
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the welcome panel. This is what the user sees when they load the application.
     * It contains instructions for how to use the application, as well as labels to show
     * the current specified price range.
     */
    private void createWelcomePanel()
    {
        BackgroundPanel bgpanel = new BackgroundPanel(new ImageIcon("welcome.png").getImage());
        instructions = new JPanel();
        instructions.setLayout(new BoxLayout(instructions, BoxLayout.Y_AXIS));
        bgpanel.setBounds(0, 0, 900, 711);
        instructions.setBounds(0, 0, 900, 711);
        instructions.setOpaque(false);

        JLabel welcomeMessage1 = new JLabel(" Welcome to the London Property Marketplace!");
        welcomeMessage1.setFont(new Font("Papyrus", Font.BOLD, 38));
        JLabel welcomeMessage2 = new JLabel("To continue, please select a valid price range.");
        welcomeMessage2.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 23));
        instructions.add(welcomeMessage1);
        instructions.add(welcomeMessage2);
        fromLabel = new JLabel("From: " + fromPrice);
        fromLabel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 23));
        instructions.add(fromLabel);
        toLabel = new JLabel("To: " + toPrice);
        toLabel.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 23));
        instructions.add(toLabel);

        welcomePanel.add(bgpanel, new Integer(0), 0);
        welcomePanel.add(instructions, new Integer(1), 0);
    }
    
    /**
     * Creates the map panel. Markers appear on the map.
     */
    private void createMapPanel()
    {
        BackgroundPanel bgpanel = new BackgroundPanel(new ImageIcon("map.png").getImage());
        markersPanel = new JPanel();
        markersPanel.setLayout(null);
        bgpanel.setBounds(0, 0, 900, 711);
        markersPanel.setBounds(0, 0, 900, 711);
        markersPanel.setOpaque(false);

        map = new PropertyMap(fromPrice, toPrice);
        HashMap<String, Marker> markers = map.getMarkers();

        for(String neighbourhood : markers.keySet()) {
            if(markers.get(neighbourhood).getNumberOfListings() > 0) {
                int xpos = markers.get(neighbourhood).getXPosition();
                int ypos = markers.get(neighbourhood).getYPosition();
                int size = markers.get(neighbourhood).getSize() + 15; // markers have a min size of 15
                createMarker(neighbourhood, xpos, ypos, size);
            }
        }
        // The label that shows the neighbourhood over which the mouse is hovering.
        neighbourhoodName = new JLabel("");
        neighbourhoodName.setOpaque(true);
        neighbourhoodName.setBackground(Color.WHITE);
        markersPanel.add(neighbourhoodName);
        neighbourhoodName.setBounds(10, 10, 170, 20);

        mapPanel.add(bgpanel, new Integer(0), 0);
        mapPanel.add(markersPanel, new Integer(1), 0);
    }
    
    /**
     * Creates a marker. When the user hovers over the marker, a label in the top
     * left displays the name of the neighbourhood.
     * @param x the x-position of the marker on the map
     * @param y the y-position of the marker on the map
     * @param size the size of the marker
     */
    private void createMarker(String neighbourhood, int x, int y, int size)
    {
        ImageIcon icon = new ImageIcon("house.png");
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(size, size,  java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);

        JButton marker = new JButton(icon);
        marker.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                neighbourhoodName.setText(neighbourhood);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                neighbourhoodName.setText("");
            }
        });
        marker.setOpaque(true);
        marker.addActionListener(e -> {createPopup(neighbourhood);});
        markersPanel.add(marker);
        marker.setBounds(x, y, size, size);
        marker.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * Creates the statistics panel. The panel shows four statistics boxes, each with arrows.
     * The user can cycle through the statistics using these arrows. No two boxes show the 
     * same statistic at any given time. The statistics displayed are: avg. no. reviews per
     * property, total no. properties, no. entire homes/apartments, priciest neighbourhood, 
     * standard deviation of prices, avg. price per night, avg. price for a shared room, 
     * avg. price for a private room.
     */
    private void createStatsPanel()
    {
        statsPanel.setLayout(new GridLayout(2,2));
        statsPanel.setBounds(0,0,900,711);
        ArrayList<String> statistics = new ArrayList<>();
        statistics.add("<html><center><b>Average Number of Reviews Per Property</b><br><br>" + Integer.toString(map.getAverageNumberOfReviews()));
        statistics.add("<html><center><b>Total Number of Properties</b><br><br>" + Integer.toString(map.getTotalNumberOfListings()));
        statistics.add("<html><center><b>Number of Entire Homes/Apartments</b><br><br>" + Integer.toString(map.getNumberOfEntireHomesAndApartments()));
        statistics.add("<html><center><b>Priciest Neighbourhood</b><br><br>" + map.getPriciestNeighbourhood());
        statistics.add("<html><center><b>Standard deviation of prices</b><br><br>" + Integer.toString(map.getStandardDeviation()));
        statistics.add("<html><center><b>Average price per night</b><br><br>" + Integer.toString(map.getAveragePrice()));
        statistics.add("<html><center><b>Average price (shared room)</b><br><br>" + Integer.toString(map.getAveragePriceSharedRoom()));
        statistics.add("<html><center><b>Average price (private room)</b><br><br>" + Integer.toString(map.getAveragePricePrivateRoom()));
        
        for(int i = 0; i < 4; ++i) {
            JPanel statBox = new JPanel();
            statBox.setLayout(new BorderLayout());
            JLabel stat = new JLabel(statistics.remove(0));
            
            JButton statLeftButton = new JButton("<");
            statBox.add(statLeftButton, BorderLayout.WEST);
            statLeftButton.addActionListener(e -> {
                String currentStat = stat.getText();
                String newStat = statistics.remove(statistics.size() - 1);
                stat.setText(newStat);
                statistics.add(0, currentStat);
            });
            
            JButton statRightButton = new JButton(">");
            statBox.add(statRightButton, BorderLayout.EAST);
            statRightButton.addActionListener(e -> {
                String currentStat = stat.getText();
                String newStat = statistics.remove(0);
                stat.setText(newStat);
                statistics.add(currentStat);
            });
            
            stat.setHorizontalAlignment(SwingConstants.CENTER);
            statBox.add(stat, BorderLayout.CENTER);
            statsPanel.add(statBox);
        }
    }
    
    /**
     * Creates the review panel. This is split in two: write a review and read reviews of the 
     * selected property. In order to write a review, a property must have been selected in 
     * the popup window on the map panel. Initially, all reviews are listed. When a property 
     * has been selected, only reviews for that property are displayed.
     */
    private void createReviewPanel()
    {
        reviewPanel.setLayout(new GridLayout(0, 2));
        reviewPanel.setBounds(0, 0, 900, 711);
        
        JPanel writeReview = new JPanel();
        writeReview.setLayout(new BorderLayout());
        writeReview.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        
        JLabel property = new JLabel("Selected Property ID: " + selectedProperty);
        writeReview.add(property, BorderLayout.NORTH);
        
        JPanel input = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(35);
        JTextArea reviewField = new JTextArea(20, 35);
        reviewField.setLineWrap(true);
        Integer[] ratings = {1, 2, 3, 4, 5};
        JComboBox chooseRating = new JComboBox(ratings);
        input.add(new JLabel("Name: "));
        input.add(nameField);
        input.add(new JLabel("Rating: "));
        input.add(chooseRating);
        input.add(new JLabel("Write review: "));
        input.add(reviewField);
        writeReview.add(input, BorderLayout.CENTER);
        
        JButton submitReview = new JButton("Submit Review");
        writeReview.add(submitReview, BorderLayout.SOUTH);
        
        submitReview.addActionListener(e -> {
            if(selectedProperty.equals("")) {
                JOptionPane.showMessageDialog(frame, "You must first select a property.");
                return;
            }
            reviewReaderWriter.write(selectedProperty, nameField.getText(), (Integer)chooseRating.getSelectedItem(), reviewField.getText());
            reviewPanel.removeAll();
            createReviewPanel();
            frame.pack();
        });
        
        JPanel readReview = new JPanel();
        JScrollPane scroll = new JScrollPane(readReview);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // increase vertical scroll speed
        scroll.getHorizontalScrollBar().setUnitIncrement(16); // increase horizontal scroll speed
        readReview.setLayout(new BoxLayout(readReview,BoxLayout.Y_AXIS));
        readReview.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY));
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY));
        JButton deselectButton = new JButton("Deselect Property");
        readReview.add(deselectButton);
        deselectButton.addActionListener(e -> {
            if (selectedProperty.equals(""))
                return;
            selectedProperty = "";
            reviewPanel.removeAll();
            createReviewPanel();
            frame.pack();
        });
        
        for(Review review : reviewReaderWriter.read(selectedProperty)) {
            readReview.add(new JLabel("<html><b>Property: </b>#" + review.getId()));
            readReview.add(new JLabel("<html><b>Author: </b>" + review.getAuthor()));
            readReview.add(new JLabel("<html><b>Rating: </b>" + Integer.toString(review.getRating())));
            readReview.add(new JLabel("<html><b>Review: </b>" + review.getComment()));
            readReview.add(new JLabel(" "));
        }
        reviewPanel.add(writeReview);
        reviewPanel.add(scroll);
    }
    
    /**
     * Creates the navigation bar at the bottom of the application. This contains two buttons for 
     * the user to move back and forth between the panels.
     */
    private void createNavigationBar()
    {
        JPanel navigationBar = new JPanel();
        navigationBar.setLayout(new BorderLayout());
        navigationBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        leftButton = new JButton("<");
        leftButton.setEnabled(false); // left button is disabled initially
        rightButton = new JButton(">");

        leftButton.addActionListener(e -> {
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.previous(mainPanel);
            --panelNumber;
            // When on the welcome panel, the left button is disabled
            if(panelNumber == 1)
                leftButton.setEnabled(false);
        });
        
        rightButton.addActionListener(e -> {
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
            cardLayout.next(mainPanel);
            ++panelNumber;
            // When on the last panel, the right button is disabled
            if(panelNumber == 4)
                rightButton.setEnabled(false);
        });

        navigationBar.add(leftButton, BorderLayout.WEST);
        navigationBar.add(rightButton, BorderLayout.EAST);
        
        contentPane.add(navigationBar, BorderLayout.SOUTH);
    }
    
    /**
      * This method is used to create a popup window to list all properties in a particular neighbourhood.
      */
    private void createPopup(String neighbourhood)
    {
        JFrame popup = new JFrame("Properties In " + neighbourhood);
        Container popupContentPane = popup.getContentPane();
        popupContentPane.setLayout(new BorderLayout());
        
        JPanel popupTopBar = new JPanel();
        popupTopBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        String sortingTypes[] = {"Number of Reviews", "Price", "Host Name"};
        JComboBox sorting = new JComboBox(sortingTypes);
        popupTopBar.add(sorting);
        
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel,BoxLayout.Y_AXIS));
        popupPanel.add(new JLabel("Host, Price, No. Reviews, Min. Nights"));
        JScrollPane scroll = new JScrollPane(popupPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        sorting.addActionListener(e -> {
            popupPanel.removeAll();
            popup.pack();
            popupPanel.setLayout(new BoxLayout(popupPanel,BoxLayout.Y_AXIS));
            popupPanel.add(new JLabel("Host, Price, No. Reviews, Min. Nights"));
            ArrayList<AirbnbListing> sorted = map.sort((String)sorting.getSelectedItem(), neighbourhood);
            for(AirbnbListing listing : sorted) {
                String hostName = listing.getHost_name();
                String price = "£" + Integer.toString(listing.getPrice());
                String numberOfReviews = Integer.toString(listing.getNumberOfReviews());
                String minimumNights = Integer.toString(listing.getMinimumNights());
                JButton selectProperty = new JButton(hostName + ", " + price + ", " + numberOfReviews + ", " + minimumNights);
                selectProperty.addActionListener((ActionEvent ev) -> {createPropertyDescription(listing); });
                popupPanel.add(selectProperty);
            }
            popup.pack();
        });
        
        popupContentPane.add(popupTopBar, BorderLayout.NORTH);
        popupContentPane.add(scroll, BorderLayout.CENTER);
        for(AirbnbListing listing : map.filterListingsByNeighbourhood(neighbourhood)) {
            String hostName = listing.getHost_name();
            String price = "£" + Integer.toString(listing.getPrice());
            String numberOfReviews = Integer.toString(listing.getNumberOfReviews());
            String minimumNights = Integer.toString(listing.getMinimumNights());
            JButton selectProperty = new JButton(hostName + ", " + price + ", " + numberOfReviews + ", " + minimumNights);
            selectProperty.addActionListener((ActionEvent ev) -> {createPropertyDescription(listing); });
            popupPanel.add(selectProperty);
        }
        popup.pack();
        popup.setVisible(true);
    }

    /**
      * This method is used to describe a particular property.
      */
    private void createPropertyDescription(AirbnbListing listing)
    {
        JFrame propertyDescription = new JFrame("Property");
        Container propertyDescPane = propertyDescription.getContentPane();
        propertyDescPane.setLayout(new BoxLayout(propertyDescPane, BoxLayout.Y_AXIS));
        propertyDescPane.add(new JLabel("<html><b>Property ID: </b>" + listing.getId()));
        propertyDescPane.add(new JLabel("<html><b>Property Name: </b>" + listing.getName()));
        propertyDescPane.add(new JLabel("<html><b>Host ID: </b>" + listing.getHost_id()));
        propertyDescPane.add(new JLabel("<html><b>Host Name: </b>" + listing.getHost_name()));
        propertyDescPane.add(new JLabel("<html><b>Neighbourhood: </b>" + listing.getNeighbourhood()));
        propertyDescPane.add(new JLabel("<html><b>Latitude: </b>" + String.valueOf(listing.getLatitude())));
        propertyDescPane.add(new JLabel("<html><b>Longitude: </b>" + String.valueOf(listing.getLongitude())));
        propertyDescPane.add(new JLabel("<html><b>Room Type: </b>" + listing.getRoom_type()));
        propertyDescPane.add(new JLabel("<html><b>Price: </b>" + Integer.toString(listing.getPrice())));
        propertyDescPane.add(new JLabel("<html><b>Minimum Nights: </b>" + Integer.toString(listing.getMinimumNights())));
        propertyDescPane.add(new JLabel("<html><b>Number of Reviews: </b>" + Integer.toString(listing.getNumberOfReviews())));
        propertyDescPane.add(new JLabel("<html><b>Last Review: </b>" + listing.getLastReview()));
        propertyDescPane.add(new JLabel("<html><b>Reviews per Month: </b>" + String.valueOf(listing.getReviewsPerMonth())));
        propertyDescPane.add(new JLabel("<html><b>Host Listings Count: </b>" + Integer.toString(listing.getCalculatedHostListingsCount())));
        propertyDescPane.add(new JLabel("<html><b>Availability per Year: </b>" + Integer.toString(listing.getAvailability365())));
        JButton selectProperty = new JButton("Select");
        JLabel selected = new JLabel("<html><b>Not selected</b>");
        if(selectedProperty.equals(listing.getId()))
            selected.setText("<html><b>Selected</b>");
        propertyDescPane.add(selectProperty);
        propertyDescPane.add(selected);
        selectProperty.addActionListener(e -> {
            selectedProperty = listing.getId();
            selected.setText("<html><b>Selected</b>");
            reviewPanel.removeAll();
            createReviewPanel();
            frame.pack();
        });
        propertyDescription.pack();
        propertyDescription.setVisible(true);
    }

    /**
      * This method plays a certain sound file.
      */
    private void play(String newFile)
    {
        try {
            File sound = new File(newFile + ".wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength());
        } catch (Exception error) {
            System.err.println(error.getMessage());
        }
    }
    
    /**
     * This method is called when an action is performed (e.g. button click)
     */
    public void actionPerformed(ActionEvent e)
    {
        // When submit button is pressed, map and stats are updated based on the new price range.
        if(e.getSource() == submit) {
            // If lower bound is greater than or equal upper bound, display error
            if((Integer)from.getSelectedItem() >= (Integer)to.getSelectedItem()) {
                JOptionPane.showMessageDialog(frame,"Lower price bound cannot be greater than or equal to upper price bound.",
                "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if((Integer)from.getSelectedItem() != fromPrice || (Integer)to.getSelectedItem() != toPrice) {
                fromPrice = (Integer)from.getSelectedItem();
                fromLabel.setText("From: " + fromPrice);
                toPrice = (Integer)to.getSelectedItem();
                toLabel.setText("To: " + toPrice);
                mapPanel.removeAll();
                createMapPanel();
                statsPanel.removeAll();
                createStatsPanel();
                reviewPanel.removeAll();
                createReviewPanel();
            }
        }
    }
}