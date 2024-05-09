import React, { useState } from "react";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

import {Link, useParams} from "react-router-dom";

function Reviews() {
    const { quizzId } = useParams(); // Get the quizzId from the URL params




   
    
    return (
        <div>
            <Typography variant="h4">Reviews for Quizz {quizzId}</Typography>

         
				
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
