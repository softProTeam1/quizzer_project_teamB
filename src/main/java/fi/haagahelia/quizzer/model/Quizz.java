package fi.haagahelia.quizzer.model;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalDouble;

@Entity
public class Quizz {
    @Id
    @GeneratedValue
    private Long quizzId;

    // time when the quizz is created
    @CreationTimestamp
    private Instant creationTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizz")
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizz")
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "statusId")
    private Status status;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    private String username;

    public Quizz() {
    }

    public Quizz(String name, String description, Status status, Category category) {
        super();
        this.name = name;
        this.description = description;
        this.status = status;
        this.category = category;
    }

    // getter
    public Long getQuizzId() {
        return quizzId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public String getCreationTimeFormatted() {
        ZonedDateTime zdt = creationTime.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm");
        return formatter.format(zdt);
    }

    @JsonIgnore
    public List<Question> getQuestion() {
        return questions;
    }

    public Status getStatus() {
        return status;
    }

    public Category getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    // setter
    public void setQuizzId(Long quizzId) {
        this.quizzId = quizzId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatetionTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    @JsonIgnore
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // returns the size of the reviews list if it is not null
    @JsonProperty("reviewCount")
    public int getReviewCount() {
        return reviews != null ? reviews.size() : 0;
    }

    @JsonProperty("ratingAverage")
    // calculates the average of the ratings
    public String getRatingAverage() {
        // If the reviews list is not empty, it computes the average
        if (reviews != null && !reviews.isEmpty()) {
            OptionalDouble average = reviews.stream() // creates a stream from the reviews list.
                    .mapToInt(Review::getRating) // transforms each Review object in the stream into an int
                    .average();// calculates the average of all the ratings
            if (average.isPresent()) {
                // formats numbers using the given pattern
                // “#.#”, which means one digit before the decimal point and one digit after the
                // decimal point.
                DecimalFormat df = new DecimalFormat("#.#"); //
                return df.format(average.getAsDouble());
            }
        }
        // if the list is empty or null, it returns 0.0
        return "0.0";
    }

    @JsonIgnore
    @Override
    public String toString() {
        if (this.status == null && this.category == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + " ]";
        } else if (this.status == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Category = " + getCategory() + " ]";
        } else if (this.category == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Status = " + getStatus() + " ]";
        } else {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Category = " + getCategory() + ", Status = " + getStatus() + " ]";
        }
    }
}