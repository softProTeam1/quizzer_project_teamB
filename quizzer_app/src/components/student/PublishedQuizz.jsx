import { useEffect, useState } from "react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";
import Typography from "@mui/material/Typography";
import { useGetPublishedQuizzes } from "../fetchapi.jsx";
import {Link} from "react-router-dom";

function PublishedQuizz() {
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
	useEffect(() => {
		fetchQuizzes(); // This function is from custom hook
	}, []);

	const [colDefs, setcolDefs] = useState([

		{
			headerName: "Name",
			field: "name",
			cellRenderer: (params) => {
				console.log(params.data); // Check the entire data object
				const link = `/questions/${params.data.quizzId}`;
				console.log("Link:", link); // Debugging
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
				return <span style={{
					backgroundColor: "rgb(220, 218, 218)", // Brighter gray
					boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
					borderRadius: "20px",
					padding: "5px 10px"
				}}>
					{params.value}
				</span>;
			}
		},
		{
			field: "creationTimeFormatted",
			filter: true,
			sortable: true,
			headerName: "Added on",
		},
	]);
	return (
		<>
			<div
				className="ag-theme-material"
				style={{ width: "100%", height: "90vh" }}
			>
				<Typography variant="h4">Quizzes</Typography>
				<AgGridReact
					rowData={quizz}
					columnDefs={colDefs}
					pagination={true}
					paginationAutoPageSize={true}
				/>
			</div>
		</>
	);
}
export default PublishedQuizz;
