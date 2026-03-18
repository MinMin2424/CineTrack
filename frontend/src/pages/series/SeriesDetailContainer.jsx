/*
 * Created by minmin_tranova on 10.03.2026
 */

import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getSeries, editSeries, changeSeriesStatus, deleteSeries } from "../../api/SeriesApi";
import SeriesDetailView from "./SeriesDetailView";

const SeriesDetailContainer = () => {
    const {seriesId} = useParams();
    const navigate = useNavigate();

    const [series, setSeries] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const [statusOpen, setStatusOpen] = useState(false);
    const [statusLoading, setStatusLoading] = useState(false);

    const [showDeletePopup, setShowDeletePopup] = useState(false);
    const [deleteLoading, setDeleteLoading] = useState(false);

    const [showEditForm, setShowEditForm] = useState(false);
    const [editFormData, setEditFormData] = useState({});
    const [editLoading, setEditLoading] = useState(false);
    const [editError, setEditError] = useState(null);

    const statusRef = useRef(null);

    useEffect(() => {
        const fetchSeries = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await getSeries(seriesId);
                setSeries(data);
            } catch (error) {
                setError(error.response?.data?.error || "Failed to load series.")
            } finally {
                setLoading(false);
            }
        };
        fetchSeries();
    }, [seriesId]);

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
            const updated = await changeSeriesStatus(seriesId, newStatus);
            setSeries(updated);
        } catch (error) {
            console.log("Failed to change status", error);
        } finally {
            setStatusLoading(false);
        }
    };

    const handleDeleteConfirm = async () => {
        setDeleteLoading(true);
        try {
            await deleteSeries(seriesId);
            navigate("/");
        } catch (error) {
            setDeleteLoading(false);
            setShowDeletePopup(false)
        }
    };

    const handleEditOpen = () => {
        setEditFormData({
            watchStartDate: series.watchStartDate || "",
            watchEndDate: series.watchEndDate || "",
            rating: series.rating != null ? String(series.rating) : "",
            notes: series.notes || "",
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
            const updated = await editSeries(seriesId, {
                watchStartDate: editFormData.watchStartDate || null,
                watchEndDate: editFormData.watchEndDate || null,
                rating: editFormData.rating || null,
                notes: editFormData.notes || null,
            });
            setSeries(updated);
            setShowEditForm(false);
        } catch (error) {
            setEditError(error.response?.data?.error || "Failed to save changes.");
        } finally {
            setEditLoading(false);
        }
    };

    const handleBack = () => navigate("/");

    const handleEpisodesClick = () => navigate(`/series/${seriesId}/episodes/1`)

    return (
        <SeriesDetailView
            series={series}
            loading={loading}
            error={error}
            statusOpen={statusOpen}
            statusLoading={statusLoading}
            statusRef={statusRef}
            showDeletePopup={showDeletePopup}
            deleteLoading={deleteLoading}
            showEditForm={showEditForm}
            editFormData={editFormData}
            editLoading={editLoading}
            editError={editError}
            onBack={handleBack}
            onEpisodesClick={handleEpisodesClick}
            onStatusToggle={() => setStatusOpen(prev => !prev)}
            onStatusChange={handleStatusChange}
            onDeleteClick={() => setShowDeletePopup(true)}
            onDeleteConfirm={handleDeleteConfirm}
            onDeleteCancel={() => setShowDeletePopup(false)}
            onEditClick={handleEditOpen}
            onEditChange={handleEditChange}
            onEditSubmit={handleEditSubmit}
            onEditClose={() => setShowEditForm(false)}
        />
    );
};

export default SeriesDetailContainer;