/*
 * Created by minmin_tranova on 26.03.2026
 */

import React from "react";
import { MdEdit } from "react-icons/md";
import "../../styles/pages/settings/SettingsPageStyle.css"
import "../../styles/components/layout/SpinnerStyle.css"
import "../../styles/components/forms/AddMediaFormStyle.css"
import {FaCalendar, FaTrash} from "react-icons/fa";
import { FaRegEdit } from "react-icons/fa";
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { MdEmail } from "react-icons/md";
import { BsFire } from "react-icons/bs";
import {IoClose} from "react-icons/io5";
import EditUserProfileForm from "../../components/forms/settings/EditUseProfileForm";
import ChangeAvatarForm from "../../components/forms/settings/ChangeAvatarForm";
import ChangePasswordForm from "../../components/forms/settings/ChangePasswordForm";
import DeleteAccountPopup from "../../components/popup/DeleteAccountPopup";

const formatDate = (isoDate) => {
    if (!isoDate) return "";
    return new Date(isoDate).toLocaleDateString("en-GB", {
        day: "numeric", month: "long", year: "numeric"
    });
};

const SettingsPageView = ({
    profile, loading, fetchError, summary,

    showEditModal, onOpenEdit, onCloseEdit,
    editForm, editErrors, editApiError, editLoading,
    onEditChange, onEditSubmit,

    showPasswordModal, onOpenPassword, onClosePassword,
    passwordForm, passwordErrors, passwordApiError, passwordLoading,
    onPasswordChange, onPasswordSubmit,

    showAvatarModal, onOpenAvatar, onCloseAvatar,
    avatarList, selectedAvatar, onSelectAvatar,
    avatarLoading, onAvatarSubmit,

    showDeleteModal, onOpenDelete, onCloseDelete,
    deleteConfirmed, onDeleteConfirmChange,
    deleteLoading, deleteApiError, onDeleteSubmit
}) => {
    const header = profile?.header || {};
    const prof = profile?.profile || {};
    const avatar = header.avatar;
    const fullname = `${header.firstname || ""} ${header.lastname || ""}`.trim() || "-";
    const movieIsMore = summary?.movies > summary?.series;

    if (loading) return (
        <div className="loading">
            <div className="spinner"/>
        </div>
    );
    if (fetchError) return (
        <div className="movie-detail-error">
            <p>{fetchError}</p>
        </div>
    );

    return (
        <div className="sp-page">

            {/* LEFT COLUMN */}
            <div className="sp-left">

                {/* AVATAR CARD */}
                <div className="sp-card sp-avatar-card">
                    <h2 className="sp-fullname">{fullname}</h2>
                    <div className="sp-avatar-wrap">
                        <img src={avatar} alt="avatar" className="sp-avatar-img" />
                        <button
                            className="sp-avatar-edit-btn"
                            onClick={onOpenAvatar}
                            aria-label="Change avatar"
                        >
                            <MdEdit />
                        </button>
                    </div>
                </div>

                {/* DANGER ZONE */}
                <div className="sp-card sp-danger-card">
                    <h3 className="sp-danger-title">Danger zone</h3>
                    <div className="sp-danger-inner">
                        <div className="sp-danger-content">
                            <p className="sp-danger-heading">Delete personal account</p>
                            <p className="sp-danger-decs">
                                Permanently remove your Personal Account and all of its
                                contents from the CineTrack platform. This action is not
                                reversible, so please continue with caution.
                            </p>
                        </div>
                        <button
                            className="sp-danger-btn"
                            onClick={onOpenDelete}
                        >
                            <FaTrash /> Delete account
                        </button>
                    </div>
                </div>
            </div>

            {/* RIGHT COLUMN */}
            <div className="sp-right">

                <div className="sp-bio-pass">
                    {/* BIO CARD */}
                    <div className="sp-card sp-bio-card">
                        <div className="sp-card-header">
                            <h3 className="sp-card-title">Bio & other details</h3>
                            <button
                                className="sp-card-edit-btn"
                                onClick={onOpenEdit}
                                aria-label="Edit profile"
                            >
                                <FaRegEdit />
                            </button>
                        </div>
                        <ul className="sp-bio-list">
                            <li className="sp-bio-item">
                                <FaUser className="sp-bio-icon" />
                                <div className="sp-bio-item-inner">
                                    <span className="sp-bio-label">Username</span>
                                    <span className="sp-bio-value">{prof.username || "-"}</span>
                                </div>
                            </li>
                            <li className="sp-bio-item">
                                <FaCalendar className="sp-bio-icon" />
                                <div className="sp-bio-item-inner">
                                    <span className="sp-bio-label">Joined on</span>
                                    <span className="sp-bio-value">{formatDate(header.creationDate)}</span>
                                </div>
                            </li>
                            <li className="sp-bio-item">
                                <MdEmail className="sp-bio-icon" />
                                <div className="sp-bio-item-inner">
                                    <span className="sp-bio-label">Email</span>
                                    <span className="sp-bio-value">{prof.email || "-"}</span>
                                </div>
                            </li>
                        </ul>
                    </div>

                    {/* PASSWORD CHANGE CARD */}
                    <div className="sp-card sp-password-card"
                         onClick={onOpenPassword}
                         role="button"
                         tabIndex={0}
                         onKeyDown={(e) => e.key === "Enter" && onOpenPassword()}
                    >
                        <div className="sp-card-header">
                            <h3 className="sp-card-title">Change password</h3>
                            <FaLock className="sp-lock-icon" />
                        </div>
                        <div className="sp-password-img">
                            <img src="/images/mouse.png" alt="mouse" />
                        </div>
                    </div>
                </div>

                {/* STATS CARDS */}
                <div className="sp-stats-row">
                    <div className="sp-card sp-stat-card">
                        <div className="sp-stat-number">
                            {summary?.movies ?? 0}
                            {movieIsMore && (
                                <BsFire className="sp-fire-icon" />
                            )}
                        </div>
                        <span className="sp-stat-label"> Total added movies</span>
                    </div>
                    <div className="sp-card sp-stat-card">
                        <div className="sp-stat-number">
                            {summary?.series ?? 0}
                            {!movieIsMore && (
                                <BsFire className="sp-fire-icon" />
                            )}
                        </div>
                        <span className="sp-stat-label"> Total added series</span>
                    </div>
                </div>
            </div>

            {/* EDIT USER PROFILE */}
            {showEditModal && (
                <div className="modal-overlay">
                    <div className="modal-container" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2 className="modal-title">Edit personal information</h2>
                            <button className="modal-close-btn" onClick={onCloseEdit}>
                                <IoClose />
                            </button>
                        </div>
                        <EditUserProfileForm
                            editForm={editForm}
                            editErrors={editErrors}
                            editApiError={editApiError}
                            editLoading={editLoading}
                            onEditChange={onEditChange}
                            onEditSubmit={onEditSubmit}
                            onClose={onCloseEdit}
                        />
                    </div>
                </div>
            )}

            {/* CHANGE AVATAR */}
            {showAvatarModal && (
                <div className="modal-overlay">
                    <div className="modal-container" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2 className="modal-title">Choose your avatar</h2>
                            <button className="modal-close-btn" onClick={onCloseEdit}>
                                <IoClose />
                            </button>
                        </div>
                        <ChangeAvatarForm
                            avatarList={avatarList}
                            selectedAvatar={selectedAvatar}
                            onSelectAvatar={onSelectAvatar}
                            avatarLoading={avatarLoading}
                            onAvatarSubmit={onAvatarSubmit}
                            onClose={onCloseAvatar}
                        />
                    </div>
                </div>
            )}

            {/* CHANGE PASSWORD */}
            {showPasswordModal && (
                <div className="modal-overlay">
                    <div className="modal-container" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2 className="modal-title">Change password</h2>
                            <button className="modal-close-btn" onClick={onCloseEdit}>
                                <IoClose />
                            </button>
                        </div>
                        <ChangePasswordForm
                            passwordForm={passwordForm}
                            passwordErrors={passwordErrors}
                            passwordApiError={passwordApiError}
                            passwordLoading={passwordLoading}
                            onPasswordChange={onPasswordChange}
                            onPasswordSubmit={onPasswordSubmit}
                            onClose={onClosePassword}
                        />
                    </div>
                </div>
            )}

            {/* DELETE ACCOUNT */}
            {showDeleteModal && (
                <div className="modal-overlay">
                    <div className="sp-delete-container" onClick={(e) => e.stopPropagation()}>
                        <DeleteAccountPopup
                            deleteConfirmed={deleteConfirmed}
                            onDeleteConfirmChange={onDeleteConfirmChange}
                            deleteLoading={deleteLoading}
                            deleteApiError={deleteApiError}
                            onDeleteSubmit={onDeleteSubmit}
                            onClose={onCloseDelete}
                        />
                    </div>
                </div>
            )}
        </div>
    );
};

export default SettingsPageView;