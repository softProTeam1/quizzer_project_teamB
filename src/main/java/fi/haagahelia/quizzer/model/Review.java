package fi.haagahelia.quizzer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private Long reviewId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String reviewText;

    
    @ManyToOne
    @JoinColumn(name = "quizzId")
    private Quizz quizz;

    public Review() {
    }

    public Review(String nickname, int rating, String reviewText, Quizz quizz) {
        super();
        this.nickname = nickname;
        this.rating = rating;
        this.reviewText = reviewText;
        this.quizz = quizz;
    }

    // Getters and Setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }


    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }
}
