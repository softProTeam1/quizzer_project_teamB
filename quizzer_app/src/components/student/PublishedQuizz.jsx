import React, { useEffect, useState } from "react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";
import Typography from "@mui/material/Typography";
import {getQuestionByDifficulty, getQuizById, getQuizzByCategory, useGetPublishedQuizzes} from "../fetchapi.jsx";
import {Link, useParams} from "react-router-dom";
import Button from "@mui/material/Button";
import Results from "../quizzes/Results.jsx";
import {FormControl, InputLabel, MenuItem, Paper, Select} from "@mui/material";

function PublishedQuizz() {
	const [quizzId, setQuizzId] = useState(null);
	const [selectedCategory, setSelectedCategory] = useState('');
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes(selectedCategory);

	//updates the state of selectedCategory based on the value selected in an event.
	const handleCategoryChange = (event) => {
		const categoryName = event.target.value;
		setSelectedCategory(categoryName);
	};


	// Only render quizz that match the selected category
	// Assuming quizz is the state where all quizzes are stored.
	const filteredQuizz = quizz.filter(q => q.category.name === selectedCategory || selectedCategory === '');

	//getting the categoryId from the URL
	let { categoryId } = useParams();

	const {categories, fetchCategories} = getQuizzByCategory(categoryId);

	//fetches categories and quizzes whenever the selectedCategory state changes.
	useEffect(() => {
		fetchCategories();
		fetchQuizzes(selectedCategory)
	}, [selectedCategory]);


	const [colDefs, setcolDefs] = useState([
		{
			headerName: "Name",
			field: "name",
			cellRenderer: (params) => {
				const link = `/questions/${params.data.quizzId}`;
				return <Link to={link}>{params.value}</Link>;
			},
			width: 300,
		},
		{ field: "description", filter: true, sortable: true, width: 600 },
		{
			field: "category.name",
			filter: true,
			sortable: true,
			headerName: "Category",
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
		{
			field: "creationTimeFormatted",
			filter: true,
			sortable: true,
			headerName: "Added on",
		},
		{
			cellRenderer: (params) => {
				const link = `/quizzer/quizz/${params.data.quizzId}`;
				return (
					<div>
						<Link to={link}>
							<Button onClick={() => {setQuizzId(params.data.quizzId)}}>
								See Results
							</Button>
						</Link>
					</div>
				);
			}

		}
	]);
	return (
		<>
			<div
				className="ag-theme-material"
				style={{width: "100%", height: "90vh"}}
			>
				<Typography variant="h4">Quizzes</Typography>
				<FormControl sx={{minWidth: 1300}}>
					<InputLabel id="category-label">Select category</InputLabel>
					<Select
						labelId="category-label"
						id="category-select"
						value={selectedCategory}
						label="Category"
						onChange={handleCategoryChange}
					>
						<MenuItem value="">All categories</MenuItem>
						{quizz.map((quiz) => (
							<MenuItem key={quiz.category.name} value={quiz.category.name}>
								{quiz.category.name}
							</MenuItem>
						))}

					</Select>
				</FormControl>
						<AgGridReact
							rowData={filteredQuizz} // Pass each quiz object as an array element
							columnDefs={colDefs}
							pagination={true}
							paginationAutoPageSize={true}
							onFirstDataRendered={(params) => {
								params.api.sizeColumnsToFit();
							}}
						/>
				))
			</div>
		</>
	);
}

export default PublishedQuizz;
