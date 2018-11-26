/**
 * The Review class represents a single review of a property.
 *
 * @author Ziad Al-Dara, Alexander Bass, William Cohen, Mihnea-Andrei Radulescu, Bhanu Guntupalli
 * @version 31.03.2018
 */
public class Review
{
    private String id;
    private String author;
    private int rating;
    private String comment;
    /**
     * Constructor for objects of class Review
     */
    public Review(String id, String author, int rating, String comment)
    {
        this.id = id;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }

    public String getId()
    {
        return id;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public int getRating()
    {
        return rating;
    }
    
    public String getComment()
    {
        return comment;
    }
}
