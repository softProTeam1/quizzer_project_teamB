package fi.haagahelia.quizzer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerRequestDto {
    @NotBlank(message = "answer text is required")
    private String answerText;

    @NotNull(message ="question id is required")
    private Long questionId;


    @NotNull(message ="is the answer correct or not")
    private boolean correctness;


    public AnswerRequestDto() {}

    public AnswerRequestDto(String answerText,Long questionId){
        super();
        this.answerText = answerText;
        this.questionId = questionId;
    }

    //getter
    public String getAnswerText(){
        return answerText;
    }
    public Long getQuestionId(){
        return questionId;
    }
    public boolean getCorrectness(){
        return correctness;
    }

    //setter
    public void setAnswerText(String answerText){ 
        this.answerText = answerText;
    }
    public void setQuestionId(Long questionId){
        this.questionId = questionId;
    }
    public void setCorrectness(boolean correctness){
        this.correctness = correctness;
    }

}