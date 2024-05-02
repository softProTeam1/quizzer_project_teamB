import React, {useEffect, useState} from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import {AgGridReact} from "ag-grid-react";

const BACKEND_URL = "http://localhost:8080";

export function GetPublishedQuizzList() {
    return fetch(`${BACKEND_URL}/api/publishedquizz`)
        .then((response) => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .catch((error) => {
            console.error('Fetch error:', error);
        });
}

export default function PublishedQuizz() {

    const [publishedQuizz, setPublishedQuizz] = useState([]);

    const [colDefs, setcolDefs] = useState([
        {field: 'name', filter: true, sortable: true},
        {field: 'description', filter: true, sortable: true, width: 600},
        {field: 'category.name', filter: true, sortable: true, headerName: "Category"},
        {field: 'creationTimeFormatted', filter: true, sortable: true, headerName: "Added on"}
    ]);


    function fetchPublishedQuizz() {
        GetPublishedQuizzList().then((publishedQuizz) => {
            setPublishedQuizz((publishedQuizz));
        });
    }

    useEffect(() => {
        fetchPublishedQuizz();
    }, []);

    return (
        <>
            <h1>Quizzes</h1>
            <div className="ag-theme-material" style={{width: '100%', height: 400}}>
                <AgGridReact
                    rowData={publishedQuizz}
                    columnDefs={colDefs}
                    pagination={true}
                    paginationAutoPageSize={true}
                />
            </div>
        </>
    );
};


