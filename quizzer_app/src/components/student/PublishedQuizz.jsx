import {useEffect, useState} from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import {AgGridReact} from "ag-grid-react";

import {useGetPublishedQuizzes} from "../fetchapi.jsx";

export default function PublishedQuizz() {

    const {quizz, fetchQuizzes} = useGetPublishedQuizzes();

    const [colDefs, setcolDefs] = useState([
        {field: 'name', filter: true, sortable: true},
        {field: 'description', filter: true, sortable: true, width: 600},
        {field: 'category.name', filter: true, sortable: true, headerName: "Category"},
        {field: 'creationTimeFormatted', filter: true, sortable: true, headerName: "Added on"}
    ]);

    useEffect(() => {
        fetchQuizzes(); // This function is from custom hook
    }, [fetchQuizzes]); // Adding fetchQuizzes as a dependency ensures it's not repeatedly called

    return (
        <>
            <h1>Quizzes</h1>
            <div className="ag-theme-material" style={{width: '100%', height: 400}}>
                <AgGridReact
                    rowData={quizz}
                    columnDefs={colDefs}
                    pagination={true}
                    paginationAutoPageSize={true}
                />
            </div>
        </>
    );
};
