import { useEffect, useState } from 'react';
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";

import { useGetPublishedQuizzes } from "../fetchapi.jsx";
import { useGetCategory } from "../fetchapi.jsx";
import { useGetQuizzFilteredByCategory } from '../fetchapi.jsx';

export default function PublishedQuizz() {

    const [quizzList, setQuizzList] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState(0);
    const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
    const { category, fetchCategory } = useGetCategory();
    const { filteredQuizzByCategory, fetchFilteredQuizzByCategory } = useGetQuizzFilteredByCategory();
    const [colDefs, setcolDefs] = useState([
        { field: 'name', filter: true, sortable: true },
        { field: 'description', filter: true, sortable: true, width: 600 },
        { field: 'category.name', filter: true, sortable: true, headerName: "Category" },
        { field: 'creationTimeFormatted', filter: true, sortable: true, headerName: "Added on" }
    ]);

    const handleChange = (event) => {
        const categoryId = event.target.value;
        setSelectedCategory(categoryId);
    }

    useEffect(() => {
        fetchQuizzes();
    }, [fetchQuizzes])

    useEffect(() => {
        fetchFilteredQuizzByCategory(selectedCategory);
    }, [selectedCategory], [fetchFilteredQuizzByCategory])

    useEffect(() => {
        fetchCategory();
    }, [fetchCategory]);

    useEffect(() => {
        if (selectedCategory === 0) {
            setQuizzList(quizz);
        } else {
            setQuizzList(filteredQuizzByCategory);
        }
    }, [selectedCategory])

    return (
        <>
            <h1>Quizzes</h1>
            <div>
                <h2>Filter by Category</h2>
                <div>
                    <label>Select a Category: </label>
                    <select value={selectedCategory} onChange={handleChange}>
                        <option key="zero" value={0}>Select a Category</option>
                        {category.map(category => (
                            <option key={category.categoryId} value={category.categoryId}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
            <div className="ag-theme-material" style={{ width: '100%', height: 400 }}>
                <AgGridReact
                    rowData={quizzList}
                    columnDefs={colDefs}
                    pagination={true}
                    paginationAutoPageSize={true}
                />
            </div>
        </>
    );
};
