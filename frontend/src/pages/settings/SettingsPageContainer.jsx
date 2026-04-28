/*
 * Created by minmin_tranova on 26.03.2026
 */

import React, {useState, useEffect, useCallback} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../../contexts/AuthContext";
import SettingsPageView from "./SettingsPageView";
import {
    getUserProfile,
    editUserProfile,
    changeUserPassword,
    changeUserAvatar,
    deleteCurrentUser
} from "../../api/UserApi";
import {getSummary} from "../../api/StatisticsApi";
import {validatePasswordStrength} from "../../utils/ValidatePasswordStrength";

const AVATAR_LIST = [
    "static/avatars/Avatar01.png",
    "static/avatars/Avatar03.png",
    "static/avatars/Avatar04.png",
    "static/avatars/Avatar05.png",
    "static/avatars/Avatar06.png",
    "static/avatars/Avatar07.png",
    "static/avatars/Avatar08.png",
    "static/avatars/Avatar09.png",
    "static/avatars/Avatar10.png",
    "static/avatars/Avatar11.png",
    "static/avatars/Avatar12.png",
    "static/avatars/Avatar13.png",
    "static/avatars/Avatar14.png",
    "static/avatars/Avatar15.png",
    "static/avatars/Avatar16.png",
    "static/avatars/Avatar17.png",
    "static/avatars/Avatar18.png",
    "static/avatars/Avatar19.png",
    "static/avatars/Avatar21.png",
];

const SettingsPageContainer = () => {
    const navigate = useNavigate();
    const {logout, refreshProfile} = useAuth();

    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const [summary, setSummary] = useState(null);

    const [showEditModal, setShowEditModal] = useState(false);
    const [showPasswordModal, setShowPasswordModal] = useState(false);
    const [showAvatarModal, setShowAvatarModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    const [editForm, setEditForm] = useState({
        username: "",
        firstname: "",
        lastname: "",
        email: "",
    });
    const [editErrors, setEditErrors] = useState({});
    const [editLoading, setEditLoading] = useState(false);
    const [editApiError, setEditApiError] = useState("");

    const [passwordForm, setPasswordForm] = useState({
        currentPassword: "",
        password: "",
        confirmPassword: "",
    });
    const [passwordErrors, setPasswordErrors] = useState({});
    const [passwordLoading, setPasswordLoading] = useState(false);
    const [passwordApiError, setPasswordApiError] = useState("");

    const [selectedAvatar, setSelectedAvatar] = useState(null);
    const [avatarLoading, setAvatarLoading] = useState(false);

    const [deleteConfirmed, setDeleteConfirmed] = useState(false);
    const [deleteLoading, setDeleteLoading] = useState(false);
    const [deleteApiError, setDeleteApiError] = useState("");

    const fetchProfile = useCallback(async () => {
        setLoading(true);
        setFetchError(null);
        try {
            const data = await getUserProfile();
            setProfile(data);
        } catch (err) {
            setFetchError("Failed to load profile.");
        } finally {
            setLoading(false);
        }
    }, []);

    const fetchStatistics = async () => {
        setLoading(true);
        setFetchError(null);
        try {
            const data = await getSummary();
            setSummary(data);
        } catch (err) {
            setFetchError("Failed to load summary.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchStatistics();
    }, []);

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]);

    /* ===== EDIT USER PROFILE ===== */

    const handleOpenEdit = () => {
        setEditForm({
            username: profile?.profile?.username || "",
            firstname: profile?.profile?.firstname || "",
            lastname: profile?.profile?.lastname || "",
            email: profile?.profile?.email || "",
        });
        setEditErrors({});
        setEditApiError("");
        setShowEditModal(true);
    };

    const handleEditChange = (e) => {
        const {name, value} = e.target;
        setEditForm(prev => ({...prev, [name]: value}));
        setEditErrors(prev => ({...prev, [name]: ""}));
        setEditApiError("");
    };

    const validateEdit = () => {
        const errs = {};
        if (!editForm.username.trim()) errs.username = "Username is required.";
        if (!editForm.firstname.trim()) errs.firstname = "First name is required.";
        if (!editForm.lastname.trim()) errs.lastname = "Last name is required.";
        if (!editForm.email.trim()) errs.email = "Email is required.";
        else if (!/\S+@\S+\.\S+/.test(editForm.email)) errs.email = "Invalid email format.";
        return errs;
    };

    const handleEditSubmit = async () => {
        const errs = validateEdit();
        if (Object.keys(errs).length > 0) {
            setEditErrors(errs);
            return;
        }
        setEditLoading(true);
        setEditApiError("");
        try {
            await editUserProfile(editForm);
            await fetchProfile();
            if (refreshProfile) await refreshProfile();
            setShowEditModal(false);
        } catch (err) {
            setEditApiError(err?.response?.data?.message || "Failed to update profile.");
        } finally {
            setEditLoading(false);
        }
    };

    /* ===== EDIT USER PASSWORD ===== */

    const handlePasswordChange = (e) => {
        const {name, value} = e.target;
        setPasswordForm(prev => ({...prev, [name]: value}));
        setPasswordErrors(prev => ({...prev, [name]: ""}));
        setPasswordApiError("");
    };

    const validatePassword = () => {
        const errs = {};
        if (!passwordForm.currentPassword) errs.currentPassword = "Current password is required.";
        const passwordError = validatePasswordStrength(passwordForm.password);
        if (passwordError) errs.password = passwordError;
        if (!passwordForm.confirmPassword) errs.confirmPassword = "Please confirm your new password.";
        else if (passwordForm.password !== passwordForm.confirmPassword) errs.confirmPassword = "Passwords do not match.";
        return errs;
    };

    const handlePasswordSubmit = async () => {
        const errs = validatePassword();
        if (Object.keys(errs).length > 0) {
            setPasswordErrors(errs);
            return;
        }
        setPasswordLoading(true);
        setPasswordApiError("");
        try {
            await changeUserPassword({
                currentPassword: passwordForm.currentPassword,
                password: passwordForm.password,
            });
            setShowPasswordModal(false);
            setPasswordForm({
                currentPassword: "",
                password: "",
                confirmPassword: "",
            });
        } catch (err) {
            setPasswordApiError(err?.response?.data?.message || "Failed to change password");
        } finally {
            setPasswordLoading(false);
        }
    };

    /* ===== EDIT USER AVATAR ===== */

    const handleOpenAvatar = () => {
        setSelectedAvatar(profile?.header?.avatar || null);
        setShowAvatarModal(true);
    };

    const handleAvatarSubmit = async () => {
        if (!selectedAvatar) return;
        setAvatarLoading(true);
        try {
            await changeUserAvatar({
                avatar: selectedAvatar,
            });
            await fetchProfile();
            if (refreshProfile) await refreshProfile();
            setShowAvatarModal(false);
        } catch (err) {
            console.log("Failed to change avatar.", err);
        } finally {
            setAvatarLoading(false);
        }
    };

    /* ===== DELETE USER ACCOUNT ===== */

    const handleDeleteSubmit = async () => {
        if (!deleteConfirmed)  return;
        setDeleteLoading(true);
        setDeleteApiError("");
        try {
            await deleteCurrentUser();
        } catch (err) {
            setDeleteApiError(err?.response?.data?.message || "Failed to delete the current user");
            setDeleteLoading(false);
            return;
        }
        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("expiresAt");
        navigate("/goodbye", {replace: true});
    };

    return (
        <SettingsPageView
            profile={profile}
            loading={loading}
            fetchError={fetchError}
            summary={summary}

            showEditModal={showEditModal}
            onOpenEdit={handleOpenEdit}
            onCloseEdit={() => setShowEditModal(false)}
            editForm={editForm}
            editErrors={editErrors}
            editApiError={editApiError}
            editLoading={editLoading}
            onEditChange={handleEditChange}
            onEditSubmit={handleEditSubmit}

            showPasswordModal={showPasswordModal}
            onOpenPassword={() => {setPasswordErrors({}); setPasswordApiError(""); setShowPasswordModal(true);}}
            onClosePassword={() => setShowPasswordModal(false)}
            passwordForm={passwordForm}
            passwordErrors={passwordErrors}
            passwordApiError={passwordApiError}
            passwordLoading={passwordLoading}
            onPasswordChange={handlePasswordChange}
            onPasswordSubmit={handlePasswordSubmit}

            showAvatarModal={showAvatarModal}
            onOpenAvatar={handleOpenAvatar}
            onCloseAvatar={() => setShowAvatarModal(false)}
            avatarList={AVATAR_LIST}
            selectedAvatar={selectedAvatar}
            onSelectAvatar={setSelectedAvatar}
            avatarLoading={avatarLoading}
            onAvatarSubmit={handleAvatarSubmit}

            showDeleteModal={showDeleteModal}
            onOpenDelete={() => {setDeleteConfirmed(false); setDeleteApiError(""); setShowDeleteModal(true);}}
            onCloseDelete={() => setShowDeleteModal(false)}
            deleteConfirmed={deleteConfirmed}
            onDeleteConfirmChange={setDeleteConfirmed}
            deleteLoading={deleteLoading}
            deleteApiError={deleteApiError}
            onDeleteSubmit={handleDeleteSubmit}
        />
    );
};

export default SettingsPageContainer;