package fi.haagahelia.quizzer.dto;

import fi.haagahelia.quizzer.model.Status;

public class QuizzDto {
    private Long quizzId;
    private String name;
    private String description;
    private Status status;

    public QuizzDto() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public QuizzDto(Long quizzId, String name, String description) {
        this.quizzId = quizzId;
        this.name = name;
        this.description = description;

    }

    public Long getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(Long quizzId) {
        this.quizzId = quizzId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
