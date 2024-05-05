import { useEffect, useState } from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";
import Button from '@mui/material/Button'; 
import Results from '../quizzes/Results';

import { useGetPublishedQuizzes } from "../fetchapi.jsx";

export default function PublishedQuizz() {

    const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
    const [quizzId, setQuizzId] = useState(null);

    const [colDefs, setcolDefs] = useState([
        { field: 'name', filter: true, sortable: true },
        { field: 'description', filter: true, sortable: true, width: 600 },
        { field: 'category.name', filter: true, sortable: true, headerName: "Category" },
        { field: 'creationTimeFormatted', filter: true, sortable: true, headerName: "Added on" },
        { 
            cellRenderer: params => (
                <Button onClick={() => {setQuizzId(params.data.quizzId)}}>see results</Button>
            ) 
        }
    ]);

    useEffect(() => {
        fetchQuizzes(); // This function is from custom hook
    }, []); 

 

    return (
        <>
            <h1>Quizzes</h1>
            <div className="ag-theme-material" style={{ width: '100%', height: 400 }}>
                {quizzId !== null ? (
                    <Results quizzId={quizzId} />
                ) : (
                    <AgGridReact
                        rowData={quizz}
                        columnDefs={colDefs}
                        pagination={true}
                        paginationAutoPageSize={true}
                    />
                )}
            </div>
        </>
    );
};
