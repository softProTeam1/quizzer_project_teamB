import { useState } from "react";

// Define BACKEND_URL
const BACKEND_URL = "http://localhost:8080";

// Define a custom React hook that uses useState and fetches data
export function useGetPublishedQuizzes() {
	const [quizz, setQuizz] = useState([]);

	// Define an async function to fetch quizzes
	const fetchQuizzes = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/quizzer/publishedquizz`);
			if (!response.ok) {
				throw new Error("Network response was not ok: " + response.statusText);
			}
			const data = await response.json();
			setQuizz(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};

	return { quizz, fetchQuizzes };
}

export function useGetQuestions(quizzId) {
	const [questions, setQuestions] = useState([]);
	const fetchQuestions = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/questions/${quizzId}`);
			if (!response.ok) {
				throw new Error("Network response was not ok" + response.statusText);
			}
			const data = await response.json();
			setQuestions(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};
	return { questions, fetchQuestions };
}

export function useGetCorrectAnswer(questionId) {
	const [correctAnswer, setCorrectAsnwer] = useState([]);
	const [inputAnswer, setInputAnswer] = useState([]);
	const fetchCorrectAnswer = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/questions/${quizzId}`);
			if (!response.ok) {
				throw new Error("Network response was not ok" + response.statusText);
			}
			const data = await response.json();
			setCorrectAsnwer(data.correctAnswer);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};
	return { correctAnswer, fetchCorrectAnswer };
}

export function inputAnswers() {
	const [inputAnswer, setInputAnswer] = useState([]);
	const postInputAnswer = async (answer) => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/answer/add`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(answer),
			});
			if (!response.ok) {
				throw new Error("Network response was not ok" + response.statusText);
			}
			const data = await response.json();
			setInputAnswer(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};

	return inputAnswer, postInputAnswer;
}
