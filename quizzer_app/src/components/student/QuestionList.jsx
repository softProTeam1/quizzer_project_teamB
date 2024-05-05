import Chip from "@mui/material/Chip";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import { useState, useEffect } from "react";
import Typography from "@mui/material/Typography";
import {inputAnswers, useGetCorrectAnswer, useGetPublishedQuizzes, useGetQuestions} from "../fetchapi";
import {Container, Paper, Snackbar} from "@mui/material";
import {useParams} from "react-router-dom";

// const Transition = React.forwardRef(function Transition(props, ref) {
// 	return <Slide direction="up" ref={ref} {...props} />;
// });
function QuestionList() {
	let { quizzId, questionId } = useParams();

	const { quizz, fetchQuizzes } = useGetPublishedQuizzes(quizzId);
	const { questions, fetchQuestions } = useGetQuestions(quizzId);
	const [inputAnswer, setInputAnswer] = useState({}); //store answers from students
	const [open, setOpen] = useState(false);
	const [feedback, setFeedback] = useState(false);
	const {correctAnswer, fetchCorrectAnswer} = useGetCorrectAnswer(questionId); //corrected answer from teacher

	const handleAnswerChange = (questionId, answer) => {
		setInputAnswer(prev => ({ ...prev, [questionId]: answer }));
	};

	const handleSubmitAnswer = (questionId) => {
		const studentAnswer = inputAnswer[questionId]?.trim();
		if (!studentAnswer) {
			console.error("No answer provided for question ID:", questionId);
			return;
		}

		postInputAnswer(questionId, studentAnswer).then(() => {
			const correct = studentAnswer === correctAnswer[questionId];
			setFeedback(correct);
			setOpen(true);
		});
	}

	const handleAnswerCheck = (event, reason) => {
		if (reason === 'clickaway') {
			return;
		}

		setOpen(false);
	};

	useEffect(() => {
		fetchQuizzes();
		fetchQuestions();
		fetchCorrectAnswer()
	}, [fetchQuizzes, fetchQuestions, fetchCorrectAnswer(), quizzId]);


	return (
		<Container maxWidth="md" sx={{ mt: 4 }}>
			<Typography variant="h4" gutterBottom>
				{quizz.name}
			</Typography>
			<Typography variant="subtitle1" gutterBottom>
				{quizz.description}
			</Typography>
			{questions.map((question, index) => (
				<Paper key={index} elevation={2} sx={{ p: 2, mb: 2 }}>
					<Typography variant="h6" sx={{ mb: 2 }}>{question.questionText}</Typography>
					<Typography variant="body2" sx={{ mb: 2 }}>
						Difficulty level: <Chip label={question.difficulty.level} />
					</Typography>
					<TextField
						fullWidth
						label="Your answer"
						variant="outlined"
						value={inputAnswer[question.id] || ''}
						onChange={e => handleAnswerChange(question.id, e.target.value)}
						sx={{ mb: 2 }}
					/>
					<Button onClick={()=> handleSubmitAnswer(question.id)}>
						SUBMIT YOUR ANSWER
					</Button>
				</Paper>
			))}
			<Snackbar
				open={open}
				autoHideDuration={6000}
				onClose={handleAnswerCheck}
				message={feedback? "This is correct, good job!" : `That is not correct, the correct answer is '${correctAnswer}' !`}
			/>
		</Container>
	);
}

export default QuestionList;
