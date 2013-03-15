package be.kdg.trips.utility;

import be.kdg.trips.businessLogic.exception.TripsException;
import org.apache.tika.Tika;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class ImageChecker
{
    /**
     * Checks if an image is valid by calling the Tika library to check the image type,
     * and also checks if the image size is no larger than 3 MB
     *
     * @param image
     * @return true if image is valid
     * @throws TripsException when image isn't valid
     */
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