import React from "react";
import Chip from "@mui/material/Chip";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import { useState, useEffect } from "react";
import Dialog from "@mui/material/Dialog";
import ListItemText from "@mui/material/ListItemText";
import ListItemButton from "@mui/material/ListItemButton";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import CloseIcon from "@mui/icons-material/Close";
import Slide from "@mui/material/Slide";
import {useGetPublishedQuizzes, useGetQuestions} from "../fetchapi";
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

	const [inputAnswe, setInputAnswer] = useState([]); //answers from students
	const [answer, setAnswer] = useState([]);
	const [correctAnswer, setCorrectAnswer] = useState([]); //corrected answer from teacher
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
