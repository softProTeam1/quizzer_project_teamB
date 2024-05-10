import React, {useEffect, useState} from "react";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import dayjs from 'dayjs';  // Make sure this import is at the top of your file
import {Link, useParams} from "react-router-dom";
import {getQuizById, useGetReviews} from "../fetchapi.jsx";
import {Paper, Rating} from "@mui/material";

function Reviews() {
    const { quizzId } = useParams(); // Get the quizzId from the URL params
	const { quiz, fetchQuiz} = getQuizById(quizzId);
	const {reviews, fetchReviews} = useGetReviews(quizzId)

	//fetch the data on load
	useEffect(() => {
		fetchQuiz();
		fetchReviews()
	});

    return (
        <div>
            <Typography variant="h4">Reviews for Quizz "{quiz.name}"</Typography>
		<div>
			<Typography variant="body1" style={{ marginTop: '8px' }}>
				Reviews: {quiz.reviewCount}, Rating average: {quiz.ratingAverage}/5
			</Typography>
			<Rating name="read-only" value={quiz.ratingAverage} readOnly />
                        
			<Link to={`/quizzer/addreview/quizz/${quizzId}`}>
				<Button >
					Write your review
				</Button>
			</Link>
			{reviews.map((review) => (
			<Paper elevation={1} style={{ padding: '16px', margin: '16px', maxWidth: '600px' }}>
				<Typography variant="h6" component="h3">
					{review.nickname}
				</Typography>
				<Typography color="textSecondary">
					Written on {dayjs(review.reviewTime).format('MMMM D, YYYY')}
				</Typography>
				<Typography>
					Rating: {review.rating}/5
				</Typography>
				<Rating name="read-only" value={review.rating} readOnly />
				<Typography variant="body2" style={{ marginTop: '8px' }}>
					{review.reviewText}
				</Typography>
			</Paper>
			))}
		</div>
				
        </div>
    );
}

export default Reviews;
