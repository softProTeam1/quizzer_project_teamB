import React, {useEffect} from "react";
import Chip from "@mui/material/Chip";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import { useState, useEffect } from "react";
import Typography from "@mui/material/Typography";
import {inputAnswers, useGetCorrectAnswer, useGetPublishedQuizzes, useGetQuestions} from "../fetchapi";
import {Container, Paper} from "@mui/material";
import {useParams} from "react-router-dom";

// const Transition = React.forwardRef(function Transition(props, ref) {
// 	return <Slide direction="up" ref={ref} {...props} />;
// });
function QuestionList() {
	let { quizzId } = useParams();

	const { quizz, fetchQuizzes } = useGetPublishedQuizzes(quizzId);
	const { questions, fetchQuestions } = useGetQuestions(quizzId);

	useEffect(() => {
		fetchQuizzes();
		fetchQuestions();
	}, [fetchQuizzes, fetchQuestions, quizzId]);

	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};

	const {inputAnswer, postInputAnswer} = inputAnswers(); //answers from students
	useEffect(() => {
		postInputAnswer(answer);
	}, []);

	const [answer, setAnswer] = useState([]);

	const {correctAnswer, fetchCorrectAnswer} = useGetCorrectAnswer(quizzId); //corrected answer from teacher
	useEffect(() => {
		fetchCorrectAnswer();
	}, []);

	const handleSubmitAnswer = ()

	const [feedback, setFeedback] = useState(false)

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
						sx={{ mb: 2 }}
					/>
					<Button onClick={() => console.log('Submit answer logic here')}>
						SUBMIT YOUR ANSWER
					</Button>
				</Paper>
			))}
		</Container>
	);
}

export default QuestionList;
