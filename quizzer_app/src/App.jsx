import React from "react";
import { BrowserRouter, Route, Routes, Link } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import PublishedQuizz from "./components/student/PublishedQuizz";
import QuestionList from "./components/student/QuestionList.jsx";
import Results from "./components/quizzes/Results.jsx";
import Reviews from "./components/quizzes/Reviews.jsx";
import AddReview from "./components/quizzes/AddReview.jsx";

function App() {
	const [value, setValue] = React.useState("one");

	const handleChange = (event, newValue) => {
		setValue(newValue);
	};

	return (
		<BrowserRouter>
			<AppBar position="static">
				<Toolbar>
					<Typography variant="h6">Quizzer</Typography>
					<Tabs value={value} onChange={handleChange} textColor="inherit">
						<Tab value="one" label="Quizzer" component={Link} to="/" />
					</Tabs>
				</Toolbar>
			</AppBar>
			<Routes>
				<Route path="/" exact Component={PublishedQuizz} />
				<Route path="/questions/:quizzId" exact Component={QuestionList} />
				<Route path="/quizzer/quizz/:quizzId" exact Component={Results} />
				<Route path="/quizzer/reviews/quizz/:quizzId" exact Component={Reviews} />
				<Route path="/quizzer/addreview/quizz/:quizzId" exact Component={AddReview} />
			</Routes>
		</BrowserRouter>
	);
}

export default App;
