package be.kdg.trips.utility;

import be.kdg.trips.exception.TripsException;
import org.apache.tika.Tika;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ImageChecker
{
    public static boolean isValidImage(byte[] image) throws TripsException {
        String contentType = new Tika().detect(image);
        if (contentType.equals("image/gif") || contentType.equals("image/jpeg") || contentType.equals("image/png"))
        {
            if(image.length/(1024*1024)<=3)
            {
                return true;
            }
            else
            {
                throw new TripsException("Maximum size file allowed: 3 MB");
            }
        }
        else
        {
            throw new TripsException("Content types allowed: gif, jpeg and png");
        }
    }
}
