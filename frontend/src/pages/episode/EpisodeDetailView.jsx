/*
 * Created by minmin_tranova on 15.03.2026
 */

import React from "react";
import { IoArrowBack } from "react-icons/io5";
import { FiEdit2 } from "react-icons/fi";
import "../../styles/pages/mediaDetail/EpisodeDetailStyle.css"
import "../../styles/components/layout/SpinnerStyle.css"
import "../../styles/mobile-version/EpisodeDetailMobileStyle.css"
import EditEpisodeForm from "../../components/forms/editMedia/EditEpisodeForm"
import { FaRegClock } from "react-icons/fa6";
import { FiCheckCircle } from "react-icons/fi";
import { SlClose } from "react-icons/sl";
import { FaRegCirclePlay } from "react-icons/fa6";
import StarRating from "../../components/layout/StarRating";

const STATUS_OPTIONS = [
    { value: "WATCHING", label: "Watching" },
    { value: "COMPLETED", label: "Completed" },
    { value: "PLAN_TO_WATCH", label: "Plan to watch" },
    { value: "DROPPED", label: "Dropped" },
];

const STATUS_ICON = {
    COMPLETED: <FiCheckCircle className="ep-table-status-icon ep-table-status-icon--completed" />,
    WATCHING: <FaRegCirclePlay className="ep-table-status-icon ep-table-status-icon--watching" />,
    PLAN_TO_WATCH: <FaRegClock className="ep-table-status-icon ep-table-status-icon--planning" />,
    DROPPED: <SlClose className="ep-table-status-icon ep-table-status-icon--dropped" />,
};

const EpisodeDetailView = ({
    episode,
    series,
    loading,
    error,
    showEditForm,
    editFormData,
    editLoading,
    editError,
    currentEpisodeNumber,
    onBack,
    onEpisodeSelect,
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
    const statusLabel = STATUS_OPTIONS.find(s => s.value === episode.status)?.label || episode.status;
    const statusClass = (status) => "status-" + status?.toLowerCase().replace(/_/g, "-");
    const completedCount = series.episodeList?.filter(ep => ep.status === "COMPLETED").length || 0;
    const sortedEpisodes = [...(series.episodeList || [])].sort((a,b) => a.episode - b.episode);

    return (
        <div className="ep-page">

            {/* BACK */}
            <button className="ep-back-btn" onClick={onBack}>
                <IoArrowBack />
            </button>

            {/* MAIN CARD */}
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

                {/* INFO */}
                <div className="ep-info-block">
                    {/* SERIES TITLE + EDIT BTN */}
                    <div className="ep-info-toprow">
                        <span className="ep-series-label">
                            {series.title?.toLocaleUpperCase()} • S{String(series.season).padStart(2,"0")}
                        </span>
                        <button className="ep-edit-btn" onClick={onEditClick}>
                            Edit episode <FiEdit2 />
                        </button>
                    </div>
                    {/* EPISODE TITLE */}
                    <h1 className="ep-episode-title">
                        <span className="ep-ep-number">
                            EP{currentEpisodeNumber}:<span> </span>
                        </span>
                        {episode.title || `Episode ${episode.episode}`}
                    </h1>
                    {/* RATING + NOTES */}
                    <div className="ep-details-row">
                        <div className="ep-details-left">
                            <div className="ep-detail-item">
                                <span className="ep-detail-label">Rating:</span>
                                <StarRating rating={episode.rating}/>
                            </div>
                            <div className="ep-detail-item">
                                <span className="ep-detail-label">Status:</span>
                                <button className={`ep-status-badge ${statusClass(episode.status)}`}>
                                    {statusLabel}
                                </button>
                            </div>
                        </div>
                        <div className="ep-details-right">
                            <span className="ep-detail-label">Notes:</span>
                            <div className="ep-notes-box">{episode.notes || ""}</div>
                        </div>
                    </div>
                </div>
            </div>

            {/* EPISODE TABLE */}
            <div className="ep-table-section">
                <div className="ep-table-header-row">
                    <h2 className="ep-table-title">Season {series.season} Episodes</h2>
                    <div className="ep-table-stats">
                        <span className="ep-table-stat ep-table-stat--total">
                            Total: {series.episodes} episodes
                        </span>
                        <span className="ep-table-stat ep-table-stat--completed">
                            {completedCount} Completed
                        </span>
                    </div>
                </div>
                <div className="ep-table-wrapper">
                    <table className="ep-table">
                        <thead>
                            <tr>
                                <th className="ep-table-th ep-table-th--ep">EP</th>
                                <th className="ep-table-th ep-table-th--title">Episode title</th>
                                <th className="ep-table-th ep-table-th--rating">Rating</th>
                                <th className="ep-table-th ep-table-th--status">Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {sortedEpisodes.map(ep => {
                                const isActive = ep.episode === currentEpisodeNumber;
                                const epStatusLabel = STATUS_OPTIONS.find(s => s.value === ep.status)?.label || "-";
                                const icon = STATUS_ICON[ep.status] || null;
                                return (
                                    <tr
                                        key={ep.episode}
                                        className={`ep-table-row ${isActive ? "ep-table-row--active" : ""}`}
                                        onClick={() => onEpisodeSelect(ep.episode)}
                                    >
                                        <td className="ep-table-td ep-table-td--ep">{ep.episode}</td>
                                        <td className="ep-table-td ep-table-td--title">{ep.title}</td>
                                        <td className="ep-table-td ep-table-td--rating">
                                            {ep.rating > 0 ? `${ep.rating}/10` : "-"}
                                        </td>
                                        <td className="ep-table-td ep-table-td--status">
                                            <span className="ep-table-status-cell">
                                                {icon}
                                                {epStatusLabel}
                                            </span>
                                        </td>
                                    </tr>
                                )
                            })}
                        </tbody>
                    </table>
                </div>
            </div>

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