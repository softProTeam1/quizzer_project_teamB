import {useState} from "react";

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
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            const data = await response.json();
            setQuizz(data);
        } catch (err) {
            console.error('Fetch error:', err);
        }
    };

    return {quizz, fetchQuizzes};
}

export function useGetQuestions() {
    const [questions, setQuestions] = useState([]);
    const fetchAnswersByQuizId = async (quizzId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/questions/${quizzId}`);
            if (!response.ok) {
                throw new Error('Network response was not ok' + response.statusText);
            }
            const data = await response.json();
            setQuestions(data);
        } catch (err) {
            console.error('Fetch error:', err);
        }
    };
    return {questions, fetchAnswersByQuizId};
}

