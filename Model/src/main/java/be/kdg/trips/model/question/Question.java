package be.kdg.trips.model.question;

import be.kdg.trips.exception.TripsException;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@Entity
@Table(name = "T_QUESTION")
public class Question implements QuestionInterface, Serializable
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Size(max = 100)
    @NotNull
    private String question;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> possibleAnswers;
    @NotNull
    private int correctAnswerIndex;

    public Question(String question, List<String> possibleAnswers, int correctAnswerIndex) {
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    private Question() {
    }

    @Override
    public String toString() {
        return this.question;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public boolean checkAnswer(int answerIndex){
        if(answerIndex == this.correctAnswerIndex){
            return true;
        }
        return false;
    }
}
