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
import { useGetQuestions } from "../fetchapi";
const Transition = React.forwardRef(function Transition(props, ref) {
	return <Slide direction="up" ref={ref} {...props} />;
});
function QuestionList(props) {
	const { questions, fetchQuestions } = useGetQuestions(props.quiz.quizzId);
	useEffect(() => {
		fetchQuestions();
	}, []);
	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};
	return (
		<React.Fragment>
			<Button onClick={handleClickOpen}>{props.quiz.name}</Button>
			<Dialog
				fullScreen
				open={open}
				onClose={handleClose}
				TransitionComponent={Transition}
			>
				<AppBar sx={{ position: "relative" }}>
					<Toolbar>
						<IconButton
							edge="start"
							color="inherit"
							onClick={handleClose}
							aria-label="close"
						>
							<CloseIcon />
						</IconButton>
						<Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
							Quizzer
						</Typography>
					</Toolbar>
				</AppBar>
				<Typography sx={{ mb: 2 }} variant="h4" component="div">
					{props.quiz.name}
				</Typography>
				<Typography sx={{ mb: 2 }} variant="h5" component="div">
					{props.quiz.description}
				</Typography>
				<List>
					{questions.map((q, index) => (
						<React.Fragment key={index}>
							<ListItemButton>
								<Box flexDirection="column">
									<ListItemText
										primary={q.questionText}
										secondary={
											<>
												Difficulty level: <Chip label={q.difficulty.level} />
											</>
										}
									/>
									<div>
										<TextField
											id="inputAnswer"
											label="your answer"
											variant="outlined"
										/>
									</div>
									<div>
										<Button
											onClick={() => {
												console.log("to be added");
											}}
										>
											SUBMIT YOUR ANSWER
										</Button>
									</div>
								</Box>
							</ListItemButton>
							<Divider />
						</React.Fragment>
					))}
				</List>
			</Dialog>
		</React.Fragment>
	);
}

export default QuestionList;
