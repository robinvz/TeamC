package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.businessLogic.interfaces.TripBL;
import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.*;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.TripDao;
import be.kdg.trips.utility.Fraction;
import be.kdg.trips.utility.ImageChecker;
import be.kdg.trips.utility.MailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import java.util.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class TripBLImpl implements TripBL
{
    private static final Logger logger = Logger.getLogger(TripBLImpl.class);

    @Autowired
    private TripDao tripDao;

    @Autowired
    private UserBL userBL;

    @Autowired
    private EnrollmentBL enrollmentBL;

    @Transactional
    @Override
    public Trip createTimelessTrip(String title, String description, TripPrivacy privacy, User organizer) throws TripsException {
        Trip trip = null;
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            trip = new TimelessTrip(title, description, privacy, organizer);
            tripDao.saveTrip(trip);
            if(trip.getPrivacy()==TripPrivacy.PRIVATE)
            {
                enrollOrganizer(organizer, trip);
            }
        }
        return trip;
    }

    @Transactional
    @Override
    public Trip createTimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate, Repeatable repeatable, Integer amount) throws TripsException {
        TimeBoundTrip trip=null;
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            if(areDatesValid(startDate, endDate))
            {
                trip = new TimeBoundTrip(title, description, privacy, organizer, startDate, endDate);
                if(repeatable!=null && amount!=null)
                {
                    if(amount>0 && amount<16)
                    {
                        Calendar startCalendar = Calendar.getInstance();
                        Calendar endCalendar = Calendar.getInstance();
                        startCalendar.setTime(startDate);
                        endCalendar.setTime(endDate);
                        for(int i = 0; i < amount; i++)
                        {
                            switch(repeatable)
                            {
                                case WEEKLY:
                                    startCalendar.add(Calendar.DATE, 7);
                                    endCalendar.add(Calendar.DATE, 7);
                                    break;
                                case MONTHLY:
                                    startCalendar.add(Calendar.MONTH, 1);
                                    endCalendar.add(Calendar.MONTH, 1);
                                    break;
                                case ANNUALLY:
                                    startCalendar.add(Calendar.YEAR, 1);
                                    endCalendar.add(Calendar.YEAR, 1);
                            }
                            Trip newTrip = new TimeBoundTrip(title, description, privacy, organizer, startCalendar.getTime(), endCalendar.getTime());
                            tripDao.saveTrip(newTrip);
                        }
                    }
                    else
                    {
                        throw new TripsException("Trip is only repeatable 1-15 times");
                    }
                }
                tripDao.saveTrip(trip);
                if(trip.getPrivacy()==TripPrivacy.PRIVATE)
                {
                    enrollOrganizer(organizer, trip);
                }
            }
        }
        return trip;
    }

    @Override
    public Trip findTripById(int id, User user) throws TripsException {
        Trip trip = tripDao.getTrip(id);
        switch(trip.getPrivacy())
        {
            case PUBLIC:
                return trip;
            case PROTECTED:
                if(user == null)
                {
                    trip.setLocations(null);
                    trip.setEnrollments(null);
                    return trip;
                }
                else if(userBL.isExistingUser(user.getEmail()))
                {
                    return trip;
                }
                break;
            case PRIVATE:
                user = userBL.findUser(user.getEmail());
                if(enrollmentBL.isExistingInvitation(user, trip))
                {
                    return trip;
                }
        }
        throw new TripsException("You do not have viewing rights for this trip");
    }

    @Override
    public List<Trip> findAllNonPrivateTrips(User user) throws TripsException {
        List trips = new ArrayList<Trip>();
        if(user==null)
        {
            trips.addAll(tripDao.getPublicTrips());
            trips.addAll(tripDao.getProtectedTripsWithoutDetails());
        }
        else if(userBL.isExistingUser(user.getEmail()))
        {
            trips.addAll(tripDao.getPublicTrips());
            trips.addAll(tripDao.getProtectedTrips());
        }
        return trips;
    }

    @Override
    public List<Trip> findNonPrivateTripsByKeyword(String keyword, User user) throws TripsException {
        List<Trip> trips = new ArrayList<Trip>();
        if(user == null)
        {
            trips.addAll(tripDao.getPublicTripsByKeyword(keyword.toLowerCase()));
            trips.addAll(tripDao.getProtectedTripsWithoutDetailsByKeyword(keyword.toLowerCase()));
        }
        else if(userBL.isExistingUser(user.getEmail()))
        {
            trips.addAll(tripDao.getPublicTripsByKeyword(keyword.toLowerCase()));
            trips.addAll(tripDao.getProtectedTripsByKeyword(keyword.toLowerCase()));
        }
        return trips;
    }

    @Override
    public List<Trip> findPrivateTrips(User user) throws TripsException {
        List<Trip> trips = new ArrayList<>();
        User dbUser = userBL.findUser(user.getEmail());
        for(Invitation invitation: dbUser.getInvitations())
        {
            trips.add(invitation.getTrip());
        }
        return trips;
    }

    @Override
    public List<Trip> findTripsByOrganizer(User organizer) throws TripsException {
        List<Trip> trips = new ArrayList<>();
        if(userBL.isExistingUser(organizer.getEmail()))
        {
            trips = tripDao.getTripsByOrganizer(organizer);
        }
        return trips;
    }

    @Transactional
    @Override
    public void editTripDetails(Trip trip, String title, String description, boolean chatAllowed, boolean positionVisible, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            if(!title.equals(""))
            {
                trip.setTitle(title);
            }
            if(!description.equals(""))
            {
                trip.setDescription(description);
            }
            if(chatAllowed != trip.isChatAllowed())
            {
                trip.setChatAllowed(chatAllowed);
            }
            if(positionVisible != trip.isPositionVisible())
            {
                trip.setPositionVisible(positionVisible);
            }
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public void publishTrip(Trip trip, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            if(!trip.isPublished())
            {
                trip.setPublished(true);
                if(trip.getPrivacy()!=TripPrivacy.PUBLIC)
                {
                    enrollmentBL.subscribe(trip, organizer);
                }
                tripDao.updateTrip(trip);
            }
            else
            {
                throw new TripsException("Trip is already published");
            }
        }
    }

    @Transactional
    @Override
    public void deleteTrip(Trip trip, User organizer) throws TripsException, MessagingException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            List<String> recipients = new ArrayList<>();
            for (Enrollment e: trip.getEnrollments())
            {
                recipients.add(e.getUser().getEmail());
            }
            tripDao.deleteTrip(trip.getId());
            MailSender.sendMail("Trip '" + trip.getTitle() + "'", "We regret to inform you that the following trip: '" + trip.getTitle() + " - " + trip.getDescription() + "' has been canceled by the organizer.", recipients);
        }
    }

    @Transactional
    @Override
    public void addDateToTimeBoundTrip(Date startDate, Date endDate, Trip trip, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            if(areDatesValid(startDate, endDate) && trip.isTimeBoundTrip())
            {
                Trip newTrip = new TimeBoundTrip(trip.getTitle(), trip.getDescription(), trip.getPrivacy(), organizer, startDate, endDate, trip.getImage());
                tripDao.saveTrip(newTrip);
            }
            else
            {
                throw new TripsException("Trip must be timebound");
            }
        }
    }

    @Transactional
    @Override
    public void addLabelToTrip(Trip trip, User organizer, String label) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            trip.addLabel(label);
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public void addRequisiteToTrip(String name, int amount, Trip trip, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer) && isTripNotActive(trip))
        {
            trip.addRequisite(name, amount);
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public void removeRequisiteFromTrip(String name, int amount, Trip trip, User organizer) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer) && isTripNotActive(trip))
        {
            trip.removeRequisite(name, amount);
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public void addImageToTrip(Trip trip, User organizer, byte[] image) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            byte[] newImage = null;
            if(image != null && ImageChecker.isValidImage(image))
            {
                newImage = image;
            }
            trip.setImage(newImage);
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public void changeThemeOfTrip(Trip trip, String theme) throws TripsException {
        if(isExistingTrip(trip.getId()))
        {
            trip.setTheme(theme);
            tripDao.updateTrip(trip);
        }
    }

    @Transactional
    @Override
    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException {
        Location location = null;
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, country), title, description, trip.getLocations().size());
            trip.addLocation(location);
            tripDao.saveOrUpdateLocation(location);
        }
        return location;
    }

    @Transactional
    @Override
    public Location addLocationToTrip(User organizer, Trip trip, double latitude, double longitude, String street, String houseNr, String city, String postalCode, String country, String title, String description, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException {
        Location location = null;
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            if(!possibleAnswers.isEmpty())
            {
                if(correctAnswerIndex<possibleAnswers.size() && correctAnswerIndex>=0)
                {
                    byte[] newImage = null;
                    if(image != null && ImageChecker.isValidImage(image))
                    {
                        newImage = image;
                    }
                    location =  new Location(trip, latitude, longitude, new Address(street, houseNr, city, postalCode, country), title, description,trip.getLocations().size(), new Question(question, possibleAnswers, correctAnswerIndex, newImage));
                    trip.addLocation(location);
                    tripDao.saveOrUpdateLocation(location);
                }
                else
                {
                    throw new TripsException("The answer doesn't exist");
                }
            }
            else
            {
                throw new TripsException("Please enter possible answers for this question");
            }
        }
        return location;
    }

    @Override
    public Location findLocationById(int id) throws TripsException {
        return tripDao.getLocationById(id);
    }

    @Transactional
    @Override
    public void editTripLocationDetails(User organizer, Trip trip, Location location, String street, String houseNr, String city, String postalCode, String country, String title, String description) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer) && doesLocationBelongToTrip(location, trip))
        {
            Address address = location.getAddress();
            if(!street.equals(""))
            {
                address.setStreet(street);
            }
            if(!houseNr.equals(""))
            {
                address.setHouseNr(houseNr);
            }
            if(!city.equals(""))
            {
                address.setCity(city);
            }
            if(!postalCode.equals(""))
            {
                address.setPostalCode(postalCode);
            }
            if(!country.equals(""))
            {
                address.setCountry(country);
            }
            if(!title.equals(""))
            {
                location.setTitle(title);
            }
            if(!description.equals(""))
            {
                location.setDescription(description);
            }
            location.setAddress(address);
            tripDao.saveOrUpdateLocation(location);
        }
    }

    @Transactional
    @Override
    public void switchLocationSequence(Trip trip, User user, int location1, int location2) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(user.getEmail()) && isOrganizer(trip, user))
        {
            List<Location> locations = trip.getLocations();
            if(location1 >= 0 && location1 < locations.size() && location2 >= 0 && location2 < locations.size())
            {
                if(location1>location2)
                {
                    locations.get(location1).setSequence(location2);
                    tripDao.saveOrUpdateLocation(locations.get(location1));
                    for (int i = 0; i < Math.abs(location2-location1); i++){
                        locations.get(location2+i).setSequence(locations.get(location2+i).getSequence()+1);
                        tripDao.saveOrUpdateLocation(locations.get(location2+i));
                    }
                }
                else if(location1<location2)
                {
                    locations.get(location1).setSequence(location2);
                    tripDao.saveOrUpdateLocation(locations.get(location1));
                    for (int i = 0; i < location2-location1; i++){
                        locations.get(location1+1+i).setSequence(locations.get(location1+1+i).getSequence()-1);
                        tripDao.saveOrUpdateLocation(locations.get(location1+1+i));
                    }
                }
            }
        }
    }

    @Transactional
    @Override
    public void deleteLocation(Trip trip, User organizer, Location location) throws TripsException {
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            boolean deleted = false;
            for(Location locationInTrip: trip.getLocations())
            {
                if(locationInTrip.equals(location))
                {
                    deleted = true;
                    tripDao.deleteLocation(locationInTrip.getId());
                }
                else if(locationInTrip.getSequence()>location.getSequence())
                {
                    locationInTrip.setSequence(locationInTrip.getSequence()-1);
                    tripDao.saveOrUpdateLocation(locationInTrip);
                }
            }
            if(deleted)
            {
                trip.removeLocation(location);
            }
        }
    }

    @Transactional
    @Override
    public void addQuestionToLocation(User organizer, Location location, String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) throws TripsException {
        if(isExistingLocation(location.getId()) && location.getQuestion() == null && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(location.getTrip(), organizer))
        {
            if(!possibleAnswers.isEmpty())
            {
                if(correctAnswerIndex<possibleAnswers.size() && correctAnswerIndex>=0)
                {
                    byte[] newImage = null;
                    if(image != null && ImageChecker.isValidImage(image))
                    {
                        newImage = image;
                    }
                    location.addQuestion(new Question(question, possibleAnswers, correctAnswerIndex, newImage));
                    tripDao.saveOrUpdateLocation(location);
                }
                else
                {
                    throw new TripsException("The answer doesn't exist");
                }
            }
            else
            {
                throw new TripsException("Please enter possible answers for this question");
            }
        }
        else
        {
            throw new TripsException("Location already has a question");
        }
    }

    @Override
    public Map<Question, Fraction> getQuestionsWithAnswerPercentage(Trip trip, User organizer) throws TripsException {
        Map<Question, Fraction> questions = new TreeMap<>();
        if(isExistingTrip(trip.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(trip, organizer))
        {
            for(Enrollment enrollment: trip.getEnrollments())
            {
                Map<Question, Boolean> answeredQuestions = enrollment.getAnsweredQuestions();
                Iterator it = answeredQuestions.keySet().iterator();
                while(it.hasNext())
                {
                    Question answeredQuestion = (Question)it.next();
                    if(questions.containsKey(answeredQuestion))
                    {
                        int denominator = questions.get(answeredQuestion).getDenominator();
                        int divisor = questions.get(answeredQuestion).getDivisor();
                        if(answeredQuestions.get(answeredQuestion))
                        {
                            questions.put(answeredQuestion, new Fraction(denominator+1, divisor+1));
                        }
                        else
                        {
                            questions.put(answeredQuestion, new Fraction(denominator, divisor+1));
                        }
                    }
                    else
                    {
                        if(answeredQuestions.get(answeredQuestion))
                        {
                            questions.put(answeredQuestion, new Fraction(1,1));
                        }
                        else
                        {
                            questions.put(answeredQuestion, new Fraction(0,1));
                        }
                    }
                }
            }
        }
        return questions;
    }

    @Transactional
    @Override
    public void editTripQuestionDetails(User organizer, Location location, String questionTitle, List<String> possibleAnswers, Integer correctAnswerIndex, byte[] image) throws TripsException {
        if(isExistingLocation(location.getId()) && location.getQuestion() != null && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(location.getTrip(), organizer))
        {
            Question question = location.getQuestion();
            if(!questionTitle.equals(""))
            {
                question.setQuestion(questionTitle);
            }
            if(possibleAnswers!=null)
            {
                if(!possibleAnswers.isEmpty())
                {
                    question.setPossibleAnswers(possibleAnswers);
                }
            }
            if(correctAnswerIndex!=null && correctAnswerIndex < question.getPossibleAnswers().size())
            {
                question.setCorrectAnswerIndex(correctAnswerIndex);
            }
            if(image!=null && ImageChecker.isValidImage(image))
            {
                question.setImage(image);
            }
            tripDao.updateQuestion(question);
        }
        else
        {
            throw new TripsException("Location doesn't have a question to edit");
        }
    }

    @Transactional
    @Override
    public void removeQuestionFromLocation(User organizer, Location location) throws TripsException {
        if(isExistingLocation(location.getId()) && location.getQuestion() != null && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(location.getTrip(), organizer))
        {
            Question question = location.getQuestion();
            location.removeQuestion();
            tripDao.saveOrUpdateLocation(location);
            tripDao.deleteQuestion(question.getId());
        }
        else
        {
            throw new TripsException("Location doesn't have a question to remove");
        }
    }

    @Transactional
    @Override
    public void removeImageFromQuestion(User organizer, Question question) throws TripsException {
        Location location = question.getLocation();
        if(isExistingLocation(location.getId()) && userBL.isExistingUser(organizer.getEmail()) && isOrganizer(location.getTrip(), organizer) && question.getImage()!=null)
        {
            question.setImage(null);
            tripDao.updateQuestion(question);
        }
    }

    @Override
    public Trip findTripByQuestion(Question question) throws TripsException {
        return tripDao.getTripByQuestion(question);
    }

    @Override
    public boolean isExistingTrip(int id) throws TripsException {
        return tripDao.isExistingTrip(id);
    }

    @Override
    public boolean isExistingLocation(int id) throws TripsException {
        return tripDao.isExistingLocation(id);
    }

    @Override
    public boolean isOrganizer(Trip trip, User organizer) throws TripsException {
        if(trip.getOrganizer().getEmail().equals(organizer.getEmail()))
        {
            return true;
        }
        logger.warn("User " + organizer.getEmail() + " tried to execute an organizer only action, but is not organizer for Trip " + trip.getTitle());
        throw new TripsException("User with email '"+organizer.getEmail()+"' is not the organizer of the selected trip");
    }

    @Override
    public boolean isTripNotActive(Trip trip) throws TripsException {
        if(!trip.isActive())
        {
            return true;
        }
        throw new TripsException("Trip is already active");
    }

    private boolean areDatesValid(Date startDate, Date endDate) throws TripsException {
        if(startDate.before(endDate))
        {
            return true;
        }
        else
        {
            throw new TripsException("Start date must be before end date");
        }
    }

    private boolean doesLocationBelongToTrip(Location location, Trip trip) throws TripsException
    {
        if(tripDao.getTrip(trip.getId()).getLocations().contains(location))
        {
            return true;
        }
        throw new TripsException("This location does not belong to this trip");
    }

    private void enrollOrganizer(User organizer, Trip trip) throws TripsException {
        enrollmentBL.selfInvite(trip, organizer);
        enrollmentBL.acceptInvitation(trip, organizer);
    }
}
