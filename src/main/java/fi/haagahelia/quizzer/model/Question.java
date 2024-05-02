package fi.haagahelia.quizzer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long questionId;

    @Column(nullable = false)
    private String questionText;

    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "difficultyId")
    private Difficulty difficulty;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quizzId")
    private Quizz quizz;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Answer> answers;

    public Question() {
    }

    public Question(String questionText, String correctAnswer, Difficulty difficulty, Quizz quizz) {
        super();
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.quizz = quizz;
    }

    // getter
    public Long getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    @JsonIgnore
    public Quizz getQuizz() {
        return quizz;
    }

    public List<Answer> getAnswer() {
        return answers;
    }

    // setter
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @JsonIgnore
    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public void setAnswer(List<Answer> answers) {
        this.answers = answers;
    }
    @JsonIgnore
    @Override
    public String toString() {
        if (this.difficulty == null && this.quizz == null) {
            return "Question [ QuestionText = " + getQuestionText() + ", CorrectAnswer = " + getCorrectAnswer() + " ]";
        } else if (this.quizz == null) {
            return "Question [ QuestionText = " + getQuestionText() + ", CorrectAnswer = " + getCorrectAnswer()
                    + ", Difficulty = " + getDifficulty() + " ]";
        } else if (this.difficulty == null) {
            return "Question [ QuestionText = " + getQuestionText() + ", CorrectAnswer = " + getCorrectAnswer()
                    + ", Quizz = " + getQuizz() + " ]";
        } else {
            return "Question [ QuestionText = " + getQuestionText() + ", CorrectAnswer = " + getCorrectAnswer()
                    + ", Difficulty = " + getDifficulty() + ", Quizz = " + getQuizz() + " ]";
        }
    }
}