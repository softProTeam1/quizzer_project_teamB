package fi.haagahelia.quizzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long answerId;
    @Column(nullable = false)
    private String answerText;

    @Column(nullable = false)
    private boolean correctness;

    @ManyToOne
    @JoinColumn(name = "quizzId")
    private Quizz quizz;

    public Answer() {}

    public Answer(String answerText, boolean correctness) {
        super();
        this.answerText = answerText;
        this.correctness = correctness;
    }


    //getter
    public Long getAnswerId(){
        return answerId;
    }
    public String getAnswerText(){
        return answerText;
    }
    public boolean getCorrectness(){
        return correctness;
    }
    @JsonIgnore
    public Quizz getQuizz(){
        return quizz;
    }



    //setter
    public void setAnswerId(Long answerId){
        this.answerId = answerId;
    }
    public void setAnswerText(String answerText){
        this.answerText = answerText;
    }
    public void setCorrectness(boolean correctness){
        this.correctness = correctness;
    }
    @JsonIgnore
    public void setQuizz(Quizz quizz){
        this.quizz = quizz;
    }
}