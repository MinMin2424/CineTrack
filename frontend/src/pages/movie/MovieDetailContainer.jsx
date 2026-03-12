/*
 * Created by minmin_tranova on 10.03.2026
 */

import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getMovie, editMovie, changeMovieStatus, deleteMovie } from "../../api/MovieApi";
import MovieDetailView from "./MovieDetailView";
import axiosConfig from "../../api/AxiosConfig";

const MovieDetailContainer = () => {
    const {movieId} = useParams();
    const navigate = useNavigate();
    const [movie, setMovie] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [statusOpen, setStatusOpen] = useState(false);
    const [statusLoading, setStatusLoading] = useState(false);
    const statusRef = useRef(null);

    useEffect(() => {
        const fetchMovie = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await getMovie(movieId);
                setMovie(data);
            } catch (error) {
                setError(error.response?.data?.error || "Failed to load movie.");
            } finally {
                setLoading(false);
            }
        };
        fetchMovie();
    }, [movieId]);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (statusRef.current && !statusRef.current.contains(e.target)) {
                setStatusOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    });

    const handleStatusChange = async (newStatus) => {
        setStatusOpen(false);
        setStatusLoading(true);
        try {
            const updated = await changeMovieStatus(movieId, newStatus);
            setMovie(updated);
        } catch (error) {
            console.log("Failed to change status", error);
        } finally {
            setStatusLoading(false);
        }
    };

    const handleDelete = async () => {
        if (!window.confirm("Are you sure zou want to delete this movie?")) return;
        try {
            await deleteMovie(movieId);
            navigate("/");
        } catch (error) {
            console.log("Failed to delete movie", error);
        }
    };

    const handleBack = () => navigate("/");

    return (
        <MovieDetailView
            movie={movie}
            loading={loading}
            error={error}
            statusOpen={statusOpen}
            statusLoading={statusLoading}
            statusRef={statusRef}
            onBack={handleBack}
            onStatusToggle={() => setStatusOpen(prev => !prev)}
            onStatusChange={handleStatusChange}
            onDelete={handleDelete}
        />
    );
};

export default MovieDetailContainer;