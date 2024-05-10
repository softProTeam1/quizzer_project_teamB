import React, {useEffect, useState} from "react";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

import {Link, useParams} from "react-router-dom";
import {getQuizById} from "../fetchapi.jsx";

function Reviews() {
    const { quizzId } = useParams(); // Get the quizzId from the URL params
	const { quiz, fetchQuiz} = getQuizById(quizzId);

	//fetch the data on load
	useEffect(() => {
		fetchQuiz();
	});

    return (
        <div>
            <Typography variant="h4">Reviews for Quizz "{quiz.name}"</Typography>
		<div>
                        
						<Link to={`/quizzer/addreview/quizz/${quizzId}`}>
							<Button >
								Write your review
							</Button>
						</Link>
					</div>
				
        </div>
    );
}

export default Reviews;
