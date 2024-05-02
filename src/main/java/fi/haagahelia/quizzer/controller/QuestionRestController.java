package fi.haagahelia.quizzer.controller;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Question", description = "Operations for accessing and managing the questions")
public class QuestionRestController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DifficultyRepository difficultyRepository;
    @Autowired
    private QuizzRepository quizzRepository;

    @Operation(summary = "Get all questions by quizz ID", description = "Returns all questions of a quizz by ID")
    @ApiResponses(value = {
            // The responseCode property defines the HTTP status code of the response
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Questions with the provided quiz id does not exist")
    })
    // endpoint path /api/quizzlist/{quizzId}/questions/?{difficultyId}
    // http://localhost:8080/api/questionlist?quizzId=1&level=Easy

    @GetMapping("/questions/{quizzId}")
    public List<Question> getQuestions(@PathVariable Long quizzId,
            @RequestParam(name = "difficulty", required = false) String level) {
        // handle if quizz id is not found
        Quizz quiz = quizzRepository.findById(quizzId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error: Quiz ID cannot be found"));

        // handle if quizz id is not publish
        if (!quiz.getStatus().getStatus()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error: Quiz is not published");
        }

        // handle the level if the difficulty is provided or not
        // if level is provided
        if (level != null) {
            if (level.equals("Easy")) {
                return questionRepository.findByQuizzQuizzIdAndDifficulty(quizzId,
                        difficultyRepository.findByLevel("Easy"));
            } else if (level.equals("Normal")) {
                return questionRepository.findByQuizzQuizzIdAndDifficulty(quizzId,
                        difficultyRepository.findByLevel("Normal"));
            } else if(level.equals("Hard")){
                return questionRepository.findByQuizzQuizzIdAndDifficulty(quizzId,
                        difficultyRepository.findByLevel("Hard"));
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Difficulty is invalid(not Easy, Normal, or Hard)");
            }
            // if level is not provided
        } else {
            return questionRepository.findByQuizz(quiz);
        }

    }
    // public ResponseEntity<?> getQuizQuestions(
    // @RequestParam("quizzId") Long quizzId,
    // @RequestParam(required = false) String level) {
    //
    // if (quizzId == null) {
    // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    // }
    // // Retrieve quiz and check if it exists
    // Optional<Quizz> quizLookup = quizzRepository.findById(quizzId);
    // if (quizLookup.isEmpty()) {
    // return ResponseEntity
    // .status(HttpStatus.NOT_FOUND)
    // .body("Error: Quiz with the provided ID does not exist");
    // }
    // // get quizz with quizz ID found
    // Quizz quizz = quizLookup.get();
    //
    // Status status = statusRepository.findByStatus(true);
    // // Check if the quiz is published
    // if (!quizz.getStatus().getStatus()) {
    // return ResponseEntity.status(HttpStatus.FORBIDDEN)
    // .body("Quiz with id " + quizzId + " is not published");
    // }
    // //question of one quizz
    // List<Question> questions = questionRepository.findByQuizzQuizzId(quizzId);
    //
    // //get quiz with level
    // if (level != null) {
    // if (!level.equals("Easy") && !level.equals("Normal") &&
    // !level.equals("Hard")) {
    // return ResponseEntity
    // .status(HttpStatus.NOT_FOUND)
    // .body("Error: Level \"" + level + "\" does not exist.");
    // }
    // Difficulty difficulty = difficultyRepository.findByLevel(level);
    //
    // List<Question> questionsByLevel =
    // questionRepository.findByQuizzQuizzIdAndDifficulty(quizzId, difficulty);
    // return ResponseEntity.ok(questionsByLevel);
    // }
    //
    // return ResponseEntity.ok(questions);
    //
    // }
}
