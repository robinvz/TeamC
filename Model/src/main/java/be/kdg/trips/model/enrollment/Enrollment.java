package be.kdg.trips.model.enrollment;

import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_ENROLLMENT")
public class Enrollment implements Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "tripId")
    @NotNull
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull
    private User user;
    @NotNull
    private Date date;
    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location lastLocationVisited;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_ENROLLMENT_REQUISITE", joinColumns = @JoinColumn(name = "enrollmentId"))
    @Column(name="requisites_VALUE")
    private Map<String, Integer> requisites;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_ENROLLMENT_COST", joinColumns = @JoinColumn(name = "enrollmentId"))
    @Column(name="costs_VALUE")
    private Map<String, Double> costs;
    private Status status;
    @NotNull
    private int score;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_ENROLLMENT_ANSWEREDQUESTION", joinColumns = @JoinColumn(name = "enrollmentId"))
    @Column(name="answeredQuestions_VALUE")
    private Map<Question, Boolean> answeredQuestions;

    public Enrollment(Trip trip, User user)
    {
        this.trip = trip;
        this.user = user;
        trip.addEnrollment(this);
        user.addEnrollment(this);
        this.date = new Date();
        this.requisites = new HashMap<>();
        this.costs = new HashMap<>();
        this.answeredQuestions = new HashMap<>();
        this.status = Status.READY;
    }

    public Enrollment()
    {
    }

    public Integer getId() {
        return id;
    }

    public Trip getTrip()
    {
        return trip;
    }

    public User getUser()
    {
        return user;
    }

    public Date getDate()
    {
        return new Date(this.date.getTime());
    }

    public Location getLastLocationVisited() {
        return lastLocationVisited;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
    }

    public Map<Question, Boolean> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(Map<Question, Boolean> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public void addAnsweredQuestion(Question question, Boolean correct)
    {
        this.answeredQuestions.put(question, correct);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setLastLocationVisited(Location lastLocationVisited) {
        if(lastLocationVisited!=null)
        {
            if(!trip.isTimeBoundTrip() || (trip.isTimeBoundTrip() && trip.isActive()))
            {
                this.lastLocationVisited = lastLocationVisited;
            }
        }
    }

    public Map<String, Integer> getRequisites()
    {
        return requisites;
    }

    public void addRequisite(String name, int amount)
    {
        if(amount == 0)
        {
            amount = 1;
        }
        if(this.requisites.containsKey(name))
        {
            this.requisites.put(name, this.requisites.get(name) + Math.abs(amount));
        }
        else
        {
            this.requisites.put(name, Math.abs(amount));
        }
    }

    public void removeRequisite(String name, int amount)
    {
        if(this.requisites.containsKey(name))
        {
            int value = this.requisites.get(name);
            if(amount < value)
            {
                this.requisites.put(name, value - Math.abs(amount));
            }
            else
            {
                this.requisites.remove(name);
            }
        }
    }

    public Map<String, Double> getCosts()
    {
        return costs;
    }

    public void addCost(String name, double amount)
    {
        if(this.costs.containsKey(name))
        {
            this.costs.put(name, this.costs.get(name) + Math.abs(amount));
        }
        else
        {
            this.costs.put(name, Math.abs(amount));
        }
    }

    public void removeCost(String name, double amount)
    {
        if(this.costs.containsKey(name))
        {
            double value = this.costs.get(name);
            if(amount < value)
            {
                this.costs.put(name, value - Math.abs(amount));
            }
            else
            {
                this.costs.remove(name);
            }
        }
    }

    public String getFullScore(){
        return this.score+"/"+getAnsweredQuestions().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enrollment that = (Enrollment) o;

        if (!trip.equals(that.trip)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trip.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
