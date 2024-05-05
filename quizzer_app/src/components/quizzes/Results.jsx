import { useEffect, useState } from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";

import { useGetQuestions } from "../fetchapi.jsx";

export default function Results({ quizzId , Title}) {
    const { questions, fetchAnswersByQuizId } = useGetQuestions();
    const [answerStats, setAnswerStats] = useState({
        totalAnswers: 0,
        correctPercentage: 0,
        correctAnswers: 0,
        wrongAnswers: 0
    });

    const [colDefs] = useState([
        { headerName: "Question Text", field: "questionText", flex: 1, filter: true, sortable: true },
        {
            headerName: "Difficulty",
            field: "difficulty.level",
            filter: true,
            sortable: true,
            cellRenderer: (params) => {
                return (
                    <span
                        style={{
                            backgroundColor: "rgb(220, 218, 218)", // Brighter gray
                            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                            borderRadius: "20px",
                            padding: "5px 10px",
                        }}
                    >
						{params.value}
					</span>
                );
            },
        },
        { headerName: "Total Answers", field: "totalAnswers", filter: true, sortable: true },
        { headerName: "Correct Answers %", field: "correctPercentage", filter: true, sortable: true },
        { headerName: "Correct Answers", field: "correctAnswers", filter: true, sortable: true },
        { headerName: "Wrong Answers", field: "wrongAnswers", filter: true, sortable: true }
    ]);

    useEffect(() => {
        if (quizzId) {
            fetchAnswersByQuizId(quizzId);
        }
    }, [fetchAnswersByQuizId, quizzId]);

    useEffect(() => {
        if (questions && questions.length > 0) {
            let totalQuestions = 0;
            let totalAnswers = 0;
            let correctAnswers = 0;
            let wrongAnswers = 0;

            questions.forEach(question => {
                totalQuestions++;
                totalAnswers += question.answer.length;
                question.answer.forEach(answer => {
                    if (answer.correctness) {
                        correctAnswers++;
                    } else {
                        wrongAnswers++;
                    }
                });
            });

            const correctPercentage = totalAnswers > 0 ? ((correctAnswers / totalAnswers) * 100).toFixed(2) + "%" : "0%";

            setAnswerStats({
                totalAnswers: totalQuestions,
                correctPercentage: correctPercentage,
                correctAnswers: correctAnswers,
                wrongAnswers: wrongAnswers
            });
        }
    }, []);

    // Combine question data with answer stats
    const rowData = questions.map(question => ({
        ...question,
        totalAnswers: question.answer.length,
        correctPercentage: ((question.answer.filter(answer => answer.correctness).length / question.answer.length) * 100).toFixed(2) + "%",
        correctAnswers: question.answer.filter(answer => answer.correctness).length,
        wrongAnswers: question.answer.filter(answer => !answer.correctness).length
    }));

    return (
        <>
           
            <h1>Results of Quizz "{Title}"</h1>
            <div className="ag-theme-material" style={{ width: '100%', height: 400 }}>
                <AgGridReact
                    rowData={rowData}
                    columnDefs={colDefs}
                    pagination={true}
                    paginationAutoPageSize={true}
                />
            </div>
        </>
    );
};
