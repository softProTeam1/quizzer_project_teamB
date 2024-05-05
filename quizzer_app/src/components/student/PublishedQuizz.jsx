import { useEffect, useState } from "react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-material.css";
import { AgGridReact } from "ag-grid-react";
import Typography from "@mui/material/Typography";
import { useGetPublishedQuizzes } from "../fetchapi.jsx";
import { Link } from "react-router-dom";
import Button from "@mui/material/Button";
import Results from "../quizzes/Results.jsx";

function PublishedQuizz() {
	const { quizz, fetchQuizzes } = useGetPublishedQuizzes();
	const [quizzId, setQuizzId] = useState(null);
	useEffect(() => {
		fetchQuizzes(); // This function is from custom hook
	}, []);

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
				style={{ width: "100%", height: "90vh" }}
			>
				<Typography variant="h4">Quizzes</Typography>
				{/*{quizzId !== null ? (*/}
				{/*	<Results quizzId={quizzId} Title={quizz.find(q => q.quizzId === quizzId)?.name} />) : (*/}
				<AgGridReact
					rowData={quizz}
					columnDefs={colDefs}
					pagination={true}
					paginationAutoPageSize={true}
					onFirstDataRendered={(params) => {
						params.api.sizeColumnsToFit();
					}}
				/>)
			</div>
		</>
	);
}
export default PublishedQuizz;
