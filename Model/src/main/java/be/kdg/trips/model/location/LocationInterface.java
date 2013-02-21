package be.kdg.trips.model.location;

import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.question.Question;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface LocationInterface {
    public double getLatitude();
    public void setLatitude(double latitude);
    public double getLongitude();
    public void setLongitude(double longitude);
    public String getDescription();
    public void setDescription(String description);
    public Address getAddress();
    public void setAddress(Address address);
    public Question getQuestion();
    public void setQuestion(Question question);
}
