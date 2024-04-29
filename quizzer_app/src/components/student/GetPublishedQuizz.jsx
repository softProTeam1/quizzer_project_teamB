import React, { useState, useEffect } from 'react';

const QuizzList = () => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuizzes = async () => {
      try {
        const response = await fetch('/api/publishedquizz'); // Replace with your endpoint
        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }

        const data = await response.json();
        setQuizzes(data);
        setLoading(false);
      } catch (err) {
        setError(err);
        setLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  if (loading) {
    return <div>Loading quizzes...</div>;
  }

  if (error) {
    return <div>Error fetching quizzes: {error.message}</div>;
  }

  return (
    <div>
      <h2>Available Quizzes</h2>
      <ul>
        {quizzes.map((quiz) => (
          <li key={quiz.quizzId}> {/* Use the correct unique key */}
            <h3>{quiz.name}</h3> 
            <p>{quiz.description}</p>
            <p>Category: {quiz.category.name}</p> 
            <p>Created on: {quiz.creationTimeFormatted}</p> 
          </li>
        ))}
      </ul>
    </div>
  );
};

export default QuizzList;
