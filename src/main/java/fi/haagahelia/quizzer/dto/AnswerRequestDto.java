package fi.haagahelia.quizzer.dto;

public class AnswerRequestDto {

    private String correctAnswer;
    private Long questionId;

    public AnswerRequestDto() {
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}