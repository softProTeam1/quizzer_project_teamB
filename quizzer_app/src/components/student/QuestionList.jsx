import React, { useEffect, useState } from "react";
import Chip from "@mui/material/Chip";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import {
	getQuestionByDifficulty,
	getQuizById, useGetAnswerById,
	useGetPublishedQuizzes,
	useGetQuestions,
} from "../fetchapi";
import Box from "@mui/material/Box";
import {
	Container,
	FormControl,
	InputLabel,
	MenuItem,
	Paper,
	Select,
	Snackbar
} from "@mui/material";
import {useParams} from "react-router-dom";

function ExpandMoreIcon() {
	return null;
}

function QuestionList() {
	//for stack bar to work *STARTING HERE*
	const [state, setState] = React.useState({
		open: false,
		vertical: "top",
		horizontal: "center",
	});
	const { vertical, horizontal, open } = state;

	const handleClose = () => {
		setState({ ...state, open: false });
	};
	//setting the message for the stack bar *TO HERE*
	const [message, setMessage] = React.useState("");

	//making a single answer object
	const [answerToPost, setAnswerToPost] = React.useState({
		answerText: "",
		questionId: "",
		correctness: false,
	});

	//getting the quizzId from the URL
	let { quizzId } = useParams();

	//getting ALL the quizz cause the useGetPublishedQuizzes doesn't have parameter for single quiz  
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes(quizzId); // this just fetch every published quizzes, quizzId does NOTHING here  

	//ACTUALLY getting a single quiz by Id 
	const { quiz, fetchQuiz} = getQuizById(quizzId);
	//getting the question from the quizzId 
	const { questions, fetchQuestions } = useGetQuestions(quizzId);
	//Initialize an array of answers with the same length as the questions array
	const [answers, setAnswers] = useState(Array(questions.length).fill(""));

	const [selectedDifficulty, setSelectedDifficulty] = useState('');

	const handleDifficultyChange = (event) => {
		setSelectedDifficulty(event.target.value);
	};

	const difficulties = ["", "Easy", "Normal", "Hard"];

	// Only render questions that match the selected difficulty
	const filteredQuestions = questions.filter(question =>
		selectedDifficulty === '' || selectedDifficulty === question.difficulty.level
	);

	const {question, fetchDifficulty} = getQuestionByDifficulty(quizzId, selectedDifficulty);
	useEffect(() => {
		fetchDifficulty();
	}, [selectedDifficulty]);


	//fetch the data on load
	useEffect(() => {
		fetchQuiz();
		fetchQuizzes();
		fetchQuestions();
	}, []);


	// Update the specific answer based on the question index
	const handleInputChange = (e, index) => {
		const newAnswers = [...answers]; // created new array which is a copy of the answers array with same values
		newAnswers[index] = { ...newAnswers[index], answerText: e.target.value };
		setAnswers(newAnswers);
	};

	//function to check if the answer is correct or to send the stackbar message
	const checkAnswer = (answer, question, newState) => {
		//for setting the stack bar state to be open(appear)
		setState({ ...newState, open: true });

		//catch typing error for when user doesn't input anything in 
		try {
			//get answer text and the question text to compare them
			//if correct
			if (answer.answerText.toLowerCase() == question.correctAnswer.toLowerCase())  {
				//setting the stack bar message
				setMessage("That is correct, good job!");
				//setting a single answer to be send to the db
				setAnswerToPost({
					answerText: answer.answerText,
					questionId: question.questionId,
					correctness: true,
				});
			}
			//if incorrect
			else {
				//setting the stack bar message
				setMessage(
					'That is incorrect, the correct answer is "' +
					question.correctAnswer +
					'"',
				);
				//setting a single answer to be send to the db
				setAnswerToPost({
					answerText: answer.answerText,
					questionId: question.questionId,
					correctness: false,
				});
			}
		} catch {
			setMessage("Please input your answer");
		}
	};
	//saving the answer to the db by sending post request
	React.useEffect(() => {
		// Make sure answerToPost is not in its initial state
		if (answerToPost.answerText !== "" && answerToPost.questionId !== "") {
			saveAnswer(answerToPost);
		}
	}, [answerToPost]); // Only run the effect when answerToPost changes

	//calling the http post method from backend
	const saveAnswer = async (answer) => {
		try {
			const response = await fetch("http://localhost:8080/api/answer/add", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(answer),
			});

			if (!response.ok) {
				throw new Error("Error in fetch: " + response.statusText);
			}
		} catch (err) {
			console.error(err);
		}
	};

	return (
		<Container maxWidth="md" sx={{ mt: 4 }}>
			<Typography variant="h4" gutterBottom>
				{quiz.name}
			</Typography>
			<Typography variant="subtitle1" gutterBottom>
				{quiz.description}
			</Typography>
				<FormControl sx={{ minWidth: 850 }}>
					<InputLabel id="difficulty-level-label">Difficulty</InputLabel>
					<Select
						labelId="difficulty-level-label"
						id="difficulty-level-select"
						value={selectedDifficulty}
						label="Difficulty Level"
						onChange={handleDifficultyChange}
					>
						{difficulties.map((difficulty) => (
							<MenuItem key={difficulty} value={difficulty}>{difficulty || "Any"}</MenuItem>
						))}
					</Select>
				</FormControl>
			{filteredQuestions.map((question, index) => (
				<Paper key={index} elevation={2} sx={{ p: 2, mb: 2 }}>
					<Typography variant="h6" sx={{ mb: 2 }}>
						{question.questionText}
					</Typography>
					<Typography variant="body2" component="div" sx={{ mb: 2 }}>
						Difficulty Level: <Chip label={question.difficulty.level} />
					</Typography>
					<TextField
						required
						margin="dense"
						fullWidth
						label="Your answer"
						variant="outlined"
						value={answers[index]?.answerText || ""}
						onChange={(e) => handleInputChange(e, index)}
					/>
					<Button
						variant="outlined"
						onClick={() => {
							checkAnswer(answers[index], question);
						}}
					>
						SUBMIT YOUR ANSWER
					</Button>
				</Paper>
			))}

			<Box sx={{ width: 500 }}>
				<Snackbar
					anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
					open={open}
					onClose={handleClose}
					message={message}
					key={vertical + horizontal}
				/>
			</Box>
		</Container>
	);
}

export default QuestionList;
