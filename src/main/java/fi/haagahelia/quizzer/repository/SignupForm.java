package fi.haagahelia.quizzer.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupForm {
    @NotEmpty
    @Size(min = 5, max = 30)
    private String username = "";

    @NotEmpty
    @Size(min = 7, max = 30)
    private String password = "";

    @NotEmpty
    @Size(min = 7, max = 30)
    private String passwordCheck = "";

    @NotEmpty
    @Size(min = 7, max = 30)
    private String email = "";

    @NotEmpty
    private String role = "USER";

}
