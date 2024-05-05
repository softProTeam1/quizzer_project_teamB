import { useEffect, useState } from "react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";
import Typography from "@mui/material/Typography";
import { useGetPublishedQuizzes } from "../fetchapi.jsx";
import QuestionList from "./QuestionList.jsx";

function PublishedQuizz() {
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
	useEffect(() => {
		fetchQuizzes(); // This function is from custom hook
	}, []);

	const [colDefs, setcolDefs] = useState([
		{
			headerName: "Name",
			cellRenderer: (params) => (
				<QuestionList  quiz={params.data}/>
			),
			width: 300,
		},
		{ field: "description", filter: true, sortable: true, width: 600 },
		{
			field: "category.name",
			filter: true,
			sortable: true,
			headerName: "Category",
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
