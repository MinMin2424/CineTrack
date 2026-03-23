/*
 * Created by minmin_tranova on 10.03.2026
 */

import React from "react";
import { IoArrowBack } from "react-icons/io5";
import { IoArrowForward } from "react-icons/io5";
import { FiEdit2 } from "react-icons/fi";
import { FaRegTrashCan } from "react-icons/fa6";
import { IoCalendarOutline } from "react-icons/io5";
import { IoChatboxOutline } from "react-icons/io5";
import { FaRegStar } from "react-icons/fa";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/pages/mediaDetail/MovieDetailStyle.css"
import "../../styles//components/layout/SpinnerStyle.css"
import DeleteMediaPopup from "../../components/popup/DeleteMediaPopup";
import EditMediaForm from "../../components/forms/editMedia/EditMediaForm";

const STATUS_OPTIONS = [
    { value: "WATCHING", label: "Watching" },
    { value: "COMPLETED", label: "Completed" },
    { value: "PLAN_TO_WATCH", label: "Plan to watch" },
    { value: "DROPPED", label: "Dropped" },
];

const formatDate = (date) => {
    if (!date) return null;
    const d = new Date(date);
    return d.toLocaleDateString(
        "cs-CZ",
        {day: "2-digit", month: "2-digit", year: "numeric"}
    );
};

const SeriesDetailView = ({
    series,
    loading,
    error,
    statusOpen,
    statusLoading,
    statusRef,
    showDeletePopup,
    deleteLoading,
    showEditForm,
    editFormData,
    editLoading,
    editError,
    onBack,
    onEpisodesClick,
    onStatusToggle,
    onStatusChange,
    onDeleteClick,
    onDeleteConfirm,
    onDeleteCancel,
    onEditClick,
    onEditChange,
    onEditSubmit,
    onEditClose,
}) => {
    if (loading) return (
        <div className="loading">
            <div className="spinner"/>
        </div>
    );
    if (error) return (
        <div className="movie-detail-error">
            <p>{error}</p>
            <button onClick={onBack} className="movie-detail-back-btn">
                Go back
            </button>
        </div>
    );
    if (!series) return null;
    const statusLabel = STATUS_OPTIONS.find(s => s.value === series.status)?.label || series.status;
    const statusClass = (status) => "status-" + status?.toLowerCase().replace(/_/g, "-");

    return (
        <div className="movie-detail-page">

            {/* BACK BTB + EPISODES BTN */}
            <div className="movie-detail-top-bar">
                <button className="movie-detail-back-btn" onClick={onBack}>
                    <IoArrowBack />
                </button>
                <button className="movie-detail-episodes-btn" onClick={onEpisodesClick}>
                    Episodes
                    <IoArrowForward />
                </button>
            </div>

            {/* MAIN CONTENT */}
            <div className="movie-detail-content">

                {/* POSTER */}
                <div className="movie-detail-poster-wrapper">
                    <img
                        src={series.poster || "/images/placeholder.png"}
                        alt={series.title}
                        className="movie-detail-poster"
                        onError={(e) => {e.target.src = "/images/placeholder.png";}}
                    />
                </div>

                {/* INFO */}
                <div className="movie-detail-info">
                    {/* TITLE + ACTIONS */}
                    <div className="movie-detail-header">
                        <h1 className="movie-detail-title">{series.title}</h1>
                        <div className="movie-detail-actions">
                            {/* STATUS DROPDOWN */}
                            <div className="movie-detail-status-wrapper" ref={statusRef}>
                                <button
                                    className={`movie-detail-status-btn ${statusClass(series.status)}`}
                                    onClick={onStatusToggle}
                                    disabled={statusLoading}
                                >
                                    {statusLabel}
                                    <IoIosArrowDown />
                                </button>
                                {statusOpen && (
                                    <ul className="movie-detail-status-dropdown">
                                        {STATUS_OPTIONS.map(opt => (
                                            <li
                                                key={opt.value}
                                                className="movie-detail-status-item"
                                                data-status={opt.value}
                                                onClick={() => onStatusChange(opt.value)}
                                            >
                                                <span className={`movie-detail-status-dot ${statusClass(opt.value)}`} />
                                                {opt.label}
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                            {/* EDIT */}
                            <button
                                className="movie-detail-icon-btn"
                                onClick={onEditClick}
                                title="Edit"
                            >
                                <FiEdit2 />
                            </button>
                            {/* DELETE */}
                            <button
                                className="movie-detail-icon-btn movie-detail-icon-btn--delete"
                                onClick={onDeleteClick}
                                title="Delete"
                            >
                                <FaRegTrashCan />
                            </button>
                        </div>
                    </div>
                    {/* METADATA */}
                    <div className="movie-detail-meta">
                        <p>
                            <span className="movie-detail-meta-label">Released Year: </span>
                            {series.releaseYear}
                        </p>
                        <p>
                            <span className="movie-detail-meta-label">Season: </span>
                            {series.season}
                        </p>
                        <p>
                            <span className="movie-detail-meta-label">Countries: </span>
                            {series.countries.join(", ")}
                        </p>
                        <p>
                            <span className="movie-detail-meta-label">Languages: </span>
                            {series.languages.join(", ")}
                        </p>
                        <p>
                            <span className="movie-detail-meta-label">Genres: </span>
                            {series.genres.join(", ")}
                        </p>
                    </div>
                </div>
            </div>

            {/* BOTTOM SECTION - DATES, RATING, NOTES */}
            <div className="movie-detail-bottom">
                {/* LEFT - DATES + RATING */}
                <div className="movie-detail-bottom-left">
                    <div className="movie-detail-section">
                        <div className="movie-detail-section-title">
                            <IoCalendarOutline className="movie-detail-section-icon" />
                            <span>Start watching</span>
                        </div>
                        <span className="movie-detail-date-badge">
                           {formatDate(series.watchStartDate) || "-"}
                       </span>
                    </div>
                    <div className="movie-detail-section">
                        <div className="movie-detail-section-title">
                            <IoCalendarOutline className="movie-detail-section-icon" />
                            <span>End watching</span>
                        </div>
                        <span className="movie-detail-date-badge">
                           {formatDate(series.watchEndDate) || "-"}
                       </span>
                    </div>
                    <div className="movie-detail-section">
                        <div className="movie-detail-section-title">
                            <FaRegStar className="movie-detail-section-icon" />
                            <span>Rating</span>
                        </div>
                        <span className="movie-detail-date-badge">
                           {series.rating ?? "-"}
                       </span>
                    </div>
                </div>
                {/* RIGHT - NOTES */}
                <div className="movie-detail-bottom-right">
                    <div className="movie-detail-section-title">
                        <IoChatboxOutline className="movie-detail-section-icon" />
                        <span>Notes</span>
                    </div>
                    <div className="movie-detail-notes">
                        {series.notes ?? ""}
                    </div>
                </div>
            </div>

            {/* EDIT FORM */}
            {showEditForm && (
                <EditMediaForm
                    formData={editFormData}
                    onChange={onEditChange}
                    onSubmit={onEditSubmit}
                    onClose={onEditClose}
                    loading={editLoading}
                    error={editError}
                />
            )}

            {/* DELETE CONFIRM POPUP */}
            {showDeletePopup && (
                <DeleteMediaPopup
                    title="TV series"
                    onConfirm={onDeleteConfirm}
                    onCancel={onDeleteCancel}
                    loading={deleteLoading}
                />
            )}
        </div>
    );
};

export default SeriesDetailView;