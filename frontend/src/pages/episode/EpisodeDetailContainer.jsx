/*
 * Created by minmin_tranova on 15.03.2026
 */

import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEpisode, changeEpisodeStatus, editEpisode } from "../../api/EpisodeApi";
import { getSeries } from "../../api/SeriesApi";
import EpisodeDetailView from "./EpisodeDetailView";

const EpisodeDetailContainer = () => {
    const {seriesId, episodeNumber} = useParams();
    const navigate = useNavigate();

    const [episode, setEpisode] = useState(null);
    const [series, setSeries] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [statusOpen, setStatusOpen] = useState(false);
    const [statusLoading, setStatusLoading] = useState(false);

    const [showEditForm, setShowEditForm] = useState(false);
    const [editFormData, setEditFormData] = useState({});
    const [editLoading, setEditLoading] = useState(false);
    const [editError, setEditError] = useState(null);

    const statusRef = useRef(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            setError(null);
            try {
                const [episodeData, seriesData] = await Promise.all([
                    getEpisode(seriesId, episodeNumber),
                    getSeries(seriesId),
                ]);
                setEpisode(episodeData);
                setSeries(seriesData);
            } catch (error) {
                setError(error.response?.data?.error || "Failed to load episode.");
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [seriesId, episodeNumber]);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (statusRef.current && !statusRef.current.contains(e.target)) {
                setStatusOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const handleStatusChange = async (newStatus) => {
        setStatusOpen(false);
        setStatusLoading(true);
        try {
            const updated = await changeEpisodeStatus(seriesId, episodeNumber, newStatus);
            setEpisode(updated);
            setSeries(prev => ({
                ...prev,
                episodeList: prev.episodeList?.map(ep =>
                    ep.episode === updated.episode ? {...ep, status: updated.status} : ep
                ),
            }))
        } catch (error) {
            console.log("Failed to change status", error);
        } finally {
            setStatusLoading(false);
        }
    };

    const handleEpisodeSelect = (num) => {
        navigate(`/series/${seriesId}/episodes/${num}`);
    };

    const handleEditOpen = () => {
        setEditFormData({
            rating: episode.rating != null ? String(episode.rating) : "",
            notes: episode.notes || "",
        });
        setEditError(null);
        setShowEditForm(true);
    };

    const handleEditChange = (e) => {
        const {name, value} = e.target;
        setEditFormData(prev => ({...prev, [name]: value}));
    };

    const handleEditSubmit = async () => {
        setEditLoading(true);
        setEditError(null);
        try {
            const updated = await editEpisode(seriesId, episodeNumber, {
                rating: editFormData.rating || null,
                notes: editFormData.notes || null,
            });
            setEpisode(updated);
            setShowEditForm(false);
        } catch (error) {
            setEditError(error.response?.data?.error || "Failed to save changes.");
        } finally {
            setEditLoading(false);
        }
    };

    const handleBack = () => navigate(`/series/${seriesId}`);

    return (
        <EpisodeDetailView
            episode={episode}
            series={series}
            loading={loading}
            error={error}
            statusOpen={statusOpen}
            statusLoading={statusLoading}
            statusRef={statusRef}
            showEditForm={showEditForm}
            editFormData={editFormData}
            editLoading={editLoading}
            editError={editError}
            currentEpisodeNumber={parseInt(episodeNumber)}
            onBack={handleBack}
            onEpisodeSelect={handleEpisodeSelect}
            onStatusToggle={() => setStatusOpen(prev => !prev)}
            onStatusChange={handleStatusChange}
            onEditClick={handleEditOpen}
            onEditChange={handleEditChange}
            onEditSubmit={handleEditSubmit}
            onEditClose={() => setShowEditForm(false)}
        />
    );
};

export default EpisodeDetailContainer;