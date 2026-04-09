/*
 * Created by minmin_tranova on 10.03.2026
 */

import React from "react";
import { IoArrowBack } from "react-icons/io5";
import { FiEdit2 } from "react-icons/fi";
import { FaRegTrashCan } from "react-icons/fa6";
import { IoCalendarOutline } from "react-icons/io5";
import { IoChatboxOutline } from "react-icons/io5";
import { FaRegStar } from "react-icons/fa";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/pages/mediaDetail/MovieDetailStyle.css"
import "../../styles//components/layout/SpinnerStyle.css"
import "../../styles/mobile-version/MovieDetailMobileStyle.css"
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

const MovieDetailView = ({
    movie,
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
   if (!movie) return null;
   const statusLabel = STATUS_OPTIONS.find(s => s.value === movie.status)?.label || movie.status;
   const statusClass = (status) => "status-" + status?.toLowerCase().replace(/_/g, "-");

   return (
       <div className="movie-detail-page">

           {/* BACK BTN */}
           <button className="movie-detail-back-btn" onClick={onBack}>
               <IoArrowBack />
           </button>

           {/* MAIN CONTENT */}
           <div className="movie-detail-content">

               {/* POSTER */}
               <div className="movie-detail-poster-wrapper">
                   <img
                       src={movie.poster || "/images/placeholder.png"}
                       alt={movie.title}
                       className="movie-detail-poster"
                       onError={(e) => {e.target.src = "/images/placeholder.png";}}
                   />
               </div>

               {/* INFO */}
               <div className="movie-detail-info">
                   {/* TITLE + ACTIONS */}
                   <div className="movie-detail-header">
                       <h1 className="movie-detail-title">{movie.title}</h1>
                       <div className="movie-detail-actions">
                           {/* STATUS DROPDOWN */}
                           <div className="movie-detail-status-wrapper" ref={statusRef}>
                               <button
                                   className={`movie-detail-status-btn ${statusClass(movie.status)}`}
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
                           {movie.releaseYear}
                       </p>
                       <p>
                           <span className="movie-detail-meta-label">Runtime: </span>
                           {movie.runtime}
                       </p>
                       <p>
                           <span className="movie-detail-meta-label">Countries: </span>
                           {movie.countries.join(", ")}
                       </p>
                       <p>
                           <span className="movie-detail-meta-label">Languages: </span>
                           {movie.languages.join(", ")}
                       </p>
                       <p>
                           <span className="movie-detail-meta-label">Genres: </span>
                           {movie.genres.join(", ")}
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
                           {formatDate(movie.watchStartDate) || "-"}
                       </span>
                   </div>
                   <div className="movie-detail-section">
                       <div className="movie-detail-section-title">
                           <IoCalendarOutline className="movie-detail-section-icon" />
                           <span>End watching</span>
                       </div>
                       <span className="movie-detail-date-badge">
                           {formatDate(movie.watchEndDate) || "-"}
                       </span>
                   </div>
                   <div className="movie-detail-section">
                       <div className="movie-detail-section-title">
                           <FaRegStar className="movie-detail-section-icon" />
                           <span>Rating</span>
                       </div>
                       <span className="movie-detail-date-badge">
                           {movie.rating ?? "-"}
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
                       {movie.notes ?? ""}
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
                    title="movie"
                    onConfirm={onDeleteConfirm}
                    onCancel={onDeleteCancel}
                    loading={deleteLoading}
                />
           )}
       </div>
   );
};

export default MovieDetailView;