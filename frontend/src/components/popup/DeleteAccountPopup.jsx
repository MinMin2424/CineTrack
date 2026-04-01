/*
 * Created by minmin_tranova on 26.03.2026
 */

import React from "react";
import { FaTrash } from "react-icons/fa6";
import "../../styles/components/forms/AddMediaFormStyle.css";
import "../../styles/components/popups/DeleteAccountPopupStyle.css";

const DeleteAccountPopup = ({
    deleteConfirmed,
    onDeleteConfirmChange,
    deleteLoading,
    deleteApiError,
    onDeleteSubmit,
    onClose,
}) => {
    return (
        <>
            <div className="sp-delete-modal-body">
                <div className="sp-delete-icon-wrap">
                    <FaTrash className="sp-delete-modal-icon" />
                </div>
                <h2 className="sp-delete-modal-title">Delete Account</h2>
                <p className="sp-delete-modal-desc">
                    Are you sure you want to delete your account?
                </p>
                <label className="sp-delete-confirm-label">
                    <input
                        type="checkbox"
                        checked={deleteConfirmed}
                        onChange={(e) => onDeleteConfirmChange(e.target.checked)}
                    />
                    <span>
                        I understand that I won't be able to recover my account.
                    </span>
                </label>

                <div className="modal-error-wrapper">
                    {deleteApiError && <p className="modal-error">{deleteApiError}</p>}
                </div>
            </div>

            <div className="modal-footer">
                <button
                    className="sp-cancel-btn"
                    onClick={onClose}
                >
                    Cancel
                </button>
                <button
                    className="sp-submit-btn"
                    onClick={onDeleteSubmit}
                    disabled={!deleteConfirmed || deleteLoading}
                >
                    {deleteLoading ? "Deleting..." : "Delete"}
                </button>
            </div>
        </>
    );
};

export default DeleteAccountPopup;