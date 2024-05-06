import { useState } from "react";

// Define BACKEND_URL
const BACKEND_URL = "http://localhost:8080";

// Define a custom React hook that uses useState and fetches data
export function useGetPublishedQuizzes(selectedCategory) {
	const [quizz, setQuizz] = useState([]);

	// Define an async function to fetch quizzes
	const fetchQuizzes = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/quizzer/publishedquizz?published=true&categoryId=${selectedCategory}`);
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

export function useGetAnswerById(quizzId) {
	const [questions, setAnswers] = useState([]);
	const fetchAnswersById = async (quizzId) => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/questions/${quizzId}`);
			if (!response.ok) {
				throw new Error("Network response was not ok" + response.statusText);
			}
			const data = await response.json();
			setAnswers(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};
	return { questions, fetchAnswersById };
}

export function getQuizById(quizzId) {
	const [quiz, setQuiz] = useState([]);

	// Define an async function to fetch quizzes
	const fetchQuiz = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/quizzer/quizz/${quizzId}`);
			if (!response.ok) {
				throw new Error("Network response was not ok: " + response.statusText);
			}
			const data = await response.json();
			setQuiz(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};

	return { quiz, fetchQuiz };
}

export function getQuestionByDifficulty(quizzId, difficulty) {
	const [question, setQuestion] = useState([]);
	const fetchDifficulty = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/questions/${quizzId}?difficulty=${difficulty}`);
			if (!response.ok) {
				throw new Error("Network response was not ok: " + response.statusText);
			}
			const data = await response.json();
			setQuestion(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};

	return { question, fetchDifficulty };
}

export function getQuizzByCategory() {
	const [categories, setCategories] = useState([]);
	const fetchCategories = async () => {
		try {
			const response = await fetch(`${BACKEND_URL}/api/category`);
			if (!response.ok) {
				throw new Error("Network response was not ok: " + response.statusText);
			}
			const data = await response.json();
			setCategories(data);
		} catch (err) {
			console.error("Fetch error:", err);
		}
	};

	return { categories, fetchCategories };
}

