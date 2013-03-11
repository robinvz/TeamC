package be.kdg.trips.model.question;

import be.kdg.trips.model.location.Location;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
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
    @GeneratedValue
    private int id;
    @Size(max = 100, message = "Question has a maximum amount of 100 characters")
    @NotNull
    private String question;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_QUESTION_POSSIBLEANSWER", joinColumns = @JoinColumn(name = "questionId"))
    @Column(name="possibleAnswer")
    private List<String> possibleAnswers;
    @NotNull
    private int correctAnswerIndex;
    @Lob
    private byte[] image;

    public Question(String question, List<String> possibleAnswers, int correctAnswerIndex, byte[] image) {
        this.question = question;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswerIndex = correctAnswerIndex;
        this.image = image;
    }

    public Question() {
    }

    @Override
    public String toString() {
        return this.question;
    }

    public String getQuestion() {
        return question;
    }

    public int getId() {
        return this.id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        if (correctAnswerIndex != question1.correctAnswerIndex) return false;
        if (!question.equals(question1.question)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + correctAnswerIndex;
        return result;
    }
}
