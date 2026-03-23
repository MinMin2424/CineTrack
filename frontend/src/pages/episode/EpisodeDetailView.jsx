/*
 * Created by minmin_tranova on 15.03.2026
 */

import React from "react";
import { IoArrowBack } from "react-icons/io5";
import { FiEdit2 } from "react-icons/fi";
import { IoChatboxOutline } from "react-icons/io5";
import { FaRegStar } from "react-icons/fa";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/pages/mediaDetail/EpisodeDetailStyle.css"
import "../../styles/components/layout/SpinnerStyle.css"
import EditEpisodeForm from "../../components/forms/editMedia/EditEpisodeForm"

const STATUS_OPTIONS = [
    { value: "WATCHING", label: "Watching" },
    { value: "COMPLETED", label: "Completed" },
    { value: "PLAN_TO_WATCH", label: "Plan to watch" },
    { value: "DROPPED", label: "Dropped" },
    { value: "PAUSED", label: "Paused" },
    { value: "NONE", label: "None" },
];

const EpisodeDetailView = ({
    episode,
    series,
    loading,
    error,
    statusOpen,
    statusLoading,
    statusRef,
    showEditForm,
    editFormData,
    editLoading,
    editError,
    currentEpisodeNumber,
    onBack,
    onEpisodeSelect,
    onStatusToggle,
    onStatusChange,
    onEditClick,
    onEditChange,
    onEditSubmit,
    onEditClose
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
    if (!episode || !series) return null;
    const totalEpisodes = series.episodes || 0;
    const statusLabel = STATUS_OPTIONS.find(s => s.value === episode.status)?.label || episode.status;
    const statusClass = (status) => "status-" + status?.toLowerCase().replace(/_/g, "-");

    const episodeStatuses = {};
    if (series.episodeList) {
        series.episodeList.forEach(ep => {
            episodeStatuses[ep.episode] = ep.status;
        });
    }

    return (
        <div className="ep-page">

            {/* BACK */}
            <button className="ep-back-btn" onClick={onBack}>
                <IoArrowBack />
            </button>

            {/* MAIN CONTENT */}
            <div className="ep-main">
                {/* POSTER */}
                <div className="ep-poster-wrapper">
                    <img
                        src={series.poster || "/images/placeholder.png"}
                        alt={series.title}
                        className="ep-poster"
                        onError={(e) => {e.target.src = "/images/placeholder.png";}}
                    />
                </div>
                {/* RIGHT SIDE */}
                <div className="ep-right">
                    <h1 className="ep-title">{series.title}</h1>
                    {/* EPISODE BUBBLES*/}
                    <div className="ep-bubbles">
                        {Array.from({ length: totalEpisodes }, (_, i) => {
                            const num = i + 1;
                            const epStatus = episodeStatuses[num] || "NONE";
                            const isActive = num === currentEpisodeNumber;
                            return (
                                <button
                                    key={num}
                                    className={`ep-bubble ${statusClass(epStatus)} ${isActive ? "ep-bubble--active" : ""}`}
                                    onClick={() => onEpisodeSelect(num)}
                                >
                                    {num}
                                </button>
                            );
                        })}
                    </div>
                </div>
            </div>
            {/* EPISODE INFO */}
            <div className="ep-info">
                <div className="ep-info-header">
                    <p className="ep-subtitle">
                        <span className="ep-title-label">Title: </span>
                        {episode.title || `Episode ${episode.episode}`}
                    </p>
                    <div className="ep-info-actions">
                        {/* STATUS DROPDOWN */}
                        <div className="ep-status-wrapper" ref={statusRef}>
                            <button
                                className={`ep-status-btn ${statusClass(episode.status)}`}
                                onClick={onStatusToggle}
                                disabled={statusLoading}
                            >
                                {statusLabel}
                                <IoIosArrowDown />
                            </button>
                            {statusOpen && (
                                <ul className="ep-status-dropdown">
                                    {STATUS_OPTIONS.map(opt => (
                                        <li
                                            key={opt.value}
                                            className="ep-status-item"
                                            data-status={opt.value}
                                            onClick={() => onStatusChange(opt.value)}
                                        >
                                            <span className={`ep-status-dot ${statusClass(opt.value)}`} />
                                            {opt.label}
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </div>
                        {/* EDIT */}
                        <button
                            className="ep-icon-btn"
                            onClick={onEditClick}
                            title="Edit"
                        >
                            <FiEdit2 />
                        </button>
                    </div>
                </div>
                {/* RATING + NOTES */}
                <div className="ep-bottom">
                    <div className="ep-bottom-left">
                        <div className="ep-section">
                            <div className="ep-section-title">
                                <FaRegStar className="ep-section-icon" />
                                <span>Rating</span>
                            </div>
                            <span className="ep-badge">{episode.rating ?? "-"}</span>
                        </div>
                    </div>
                    <div className="ep-bottom-right">
                        <div className="ep-section-title">
                            <IoChatboxOutline className="ep-section-icon" />
                            <span>Notes</span>
                        </div>
                        <div className="ep-notes">{episode.notes ?? ""}</div>
                    </div>
                </div>
            </div>

            {/* EDIT FORM */}
            {showEditForm && (
                <EditEpisodeForm
                    formData={editFormData}
                    onChange={onEditChange}
                    onSubmit={onEditSubmit}
                    onClose={onEditClose}
                    loading={editLoading}
                    error={editError}
                />
            )}
        </div>
    );
};

export default EpisodeDetailView;