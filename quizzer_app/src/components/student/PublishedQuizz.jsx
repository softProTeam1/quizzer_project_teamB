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
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
	const [quizzId, setQuizzId] = useState(null);
	const [selectedCategory, setSelectedCategory] = useState('');

	//updates the state of selectedCategory based on the value selected in an event.
	const handleCategoryChange = (event) => {
		setSelectedCategory(event.target.value);
	};

	// const categories = ["", "Easy", "Normal", "Hard"];
	// Only render quizz that match the selected category
	const filteredQuizz = quizz.filter(quizz =>
		selectedCategory === '' || selectedCategory === quizz.category.name || selectedCategory === 'All categories'
	);

	//getting the categoryId from the URL
	let { categoryId } = useParams();

	const {categories, fetchCategories} = getQuizzByCategory();

	//fetches categories and quizzes whenever the selectedCategory state changes.
	useEffect(() => {
		const fetchData = async () => {
			try {
				await fetchCategories();  // Assuming this sets categories state
				await fetchQuizzes(selectedCategory);
			} catch (error) {
				console.error('Failed to fetch data:', error);
			}
		};

		fetchData();
	}, [selectedCategory]); // Ensure all dependencies are listed here if any


	// useEffect(() => {
	// 	fetchQuizzes(); // This function is from custom hook
	// }, []);

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
					<InputLabel id="category-label">Choose a category</InputLabel>
					<Select
						labelId="category-label"
						id="category-select"
						value={selectedCategory}
						onChange={handleCategoryChange}
					>
						<MenuItem key="" value="">All categories</MenuItem>
						{Array.isArray(categories) && categories.map((category) => (
							<MenuItem key={category} value={category.name}>
								{category.name}
							</MenuItem>
						))}
					</Select>
				</FormControl>
				{/*{filteredQuizz.map((quiz, index) => (*/}
						<AgGridReact
							rowData={quizz} // Pass each quiz object as an array element
							columnDefs={colDefs}
							pagination={true}
							paginationAutoPageSize={true}
							onFirstDataRendered={(params) => {
								params.api.sizeColumnsToFit();
							}}
						/>
				))}
			</div>
		</>
	);
}

export default PublishedQuizz;
