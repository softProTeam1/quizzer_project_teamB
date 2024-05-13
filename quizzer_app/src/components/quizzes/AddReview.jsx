import React, { useState, useEffect } from "react";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import { FormControl, FormControlLabel, Radio, RadioGroup } from "@mui/material";

import {Link, useParams } from "react-router-dom";
import { getQuizzById } from "../fetchapi.jsx";

function AddReview() {
    const { quizzId } = useParams();
    const { quizz, fetchQuizzById } = getQuizzById(quizzId);
    const [formData, setFormData] = useState({
        nickname: "",
        rating: "",
        reviewText: "",
        quizz: {
            quizzId: quizzId
        }
    });

    const handleSubmit = async () => {
        try {
            const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/api/review/add`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
            });
            console.log(JSON.stringify(formData));
            if (!response.ok) {
                throw new Error("Error in fetch: " + response.statusText);
            }
        } catch (error) {
            console.error("Error:", error);
        }
     

    };

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    useEffect(() => {
        fetchQuizzById();
    }, []);

    return (
        <div>
            <Typography variant="h4">Add review for  "{quizz?.name}"</Typography>
            <TextField
                name="nickname"
                value={formData.nickname}
                label="Nickname"
                variant="outlined"
                onChange={handleChange}
            />


            <RadioGroup
                aria-label="rating"
                name="rating"
                value={formData.rating}
                onChange={handleChange}
            >
                <FormControlLabel value="1" control={<Radio />} label="1 - Useless" />
                <FormControlLabel value="2" control={<Radio />} label="2 - Poor" />
                <FormControlLabel value="3" control={<Radio />} label="3 - Ok" />
                <FormControlLabel value="4" control={<Radio />} label="4 - Good" />
                <FormControlLabel value="5" control={<Radio />} label="5 - Excelent" />
            </RadioGroup>


            <TextField
                name="reviewText"
                value={formData.reviewText}
                label="Review Text"
                variant="outlined"
                multiline
                rows={4}
                onChange={handleChange}
            />
             <Link to={`/quizzer/reviews/quizz/${quizzId}`}>
            <Button onClick={handleSubmit}>
                Submit Review
            </Button>
            </Link>
            <Link to={`/quizzer/reviews/quizz/${quizzId}`}>
                <Button >
                    cancel
                </Button>
            </Link>
        </div>
    );
}

export default AddReview;
