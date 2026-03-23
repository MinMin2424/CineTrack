/*
 * Created by minmin_tranova on 15.03.2026
 */

import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEpisode, changeEpisodeStatus, editEpisode } from "../../api/EpisodeApi";
import { getSeries } from "../../api/SeriesApi";
import EpisodeDetailView from "./EpisodeDetailView";

const STATUS_MAP = {
    "plan to watch": "PLAN_TO_WATCH",
    "watching": "WATCHING",
    "completed": "COMPLETED",
    "dropped": "DROPPED"
};

const EpisodeDetailContainer = () => {
    const {seriesId, episodeNumber} = useParams();
    const navigate = useNavigate();

    const [episode, setEpisode] = useState(null);
    const [series, setSeries] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [showEditForm, setShowEditForm] = useState(false);
    const [editFormData, setEditFormData] = useState({});
    const [editLoading, setEditLoading] = useState(false);
    const [editError, setEditError] = useState(null);

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

    const handleEpisodeSelect = (num) => {
        navigate(`/series/${seriesId}/episodes/${num}`);
    };

    const handleEditOpen = () => {
        const currentStatus = episode.status?.toLowerCase().replace(/_/g, " ") || "plan to watch";
        setEditFormData({
            status: currentStatus,
            rating: episode.rating != null && episode.rating > 0 ? String(episode.rating) : "",
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
            const newStatusEnum = STATUS_MAP[editFormData.status || episode.status];
            if (newStatusEnum !== episode.status) {
                await changeEpisodeStatus(seriesId, episodeNumber, newStatusEnum);
            }
            const updated = await editEpisode(seriesId, episodeNumber, {
                rating: editFormData.rating || null,
                notes: editFormData.notes || null,
            });
            setEpisode({...updated, status: newStatusEnum});
            setSeries(prev => ({
                ...prev,
                episodeList: prev.episodeList?.map(ep =>
                    ep.episode === parseInt(episodeNumber)
                        ? { ...ep, status: newStatusEnum, rating: updated.rating }
                        : ep
                ),
            }))
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
            showEditForm={showEditForm}
            editFormData={editFormData}
            editLoading={editLoading}
            editError={editError}
            currentEpisodeNumber={parseInt(episodeNumber)}
            onBack={handleBack}
            onEpisodeSelect={handleEpisodeSelect}
            onEditClick={handleEditOpen}
            onEditChange={handleEditChange}
            onEditSubmit={handleEditSubmit}
            onEditClose={() => setShowEditForm(false)}
        />
    );
};

export default EpisodeDetailContainer;