/*
 * Created by minmin_tranova on 01.04.2026
 */

import React from "react";
import "../../../styles/components/forms/AddMediaFormStyle.css";
import "../../../styles/components/forms/ChangeAvatarFormStyle.css";

const ChangeAvatarForm = ({
    avatarList,
    selectedAvatar,
    onSelectAvatar,
    avatarLoading,
    onAvatarSubmit,
    onClose,
}) => {
    return (
        <>
            <div className="modal-body">
                <div className="sp-avatar-grid">
                    {avatarList.map((av) => (
                        <button
                            key={av}
                            className={`sp-avatar-option ${selectedAvatar === av ? "sp-avatar-option--selected" : ""}`}
                            onClick={() => onSelectAvatar(av)}
                            aria-label={`Select ${av}`}
                            type="button"
                        >
                            <img src={`${av}`} alt={av} />
                        </button>
                    ))}
                </div>
            </div>

            <div className="modal-footer">
                <button
                    className="modal-cancel-btn"
                    onClick={onClose}
                >
                    Cancel
                </button>
                <button
                    className="modal-submit-btn"
                    onClick={onAvatarSubmit}
                    disabled={!selectedAvatar || avatarLoading}
                >
                    {avatarLoading ? "Saving..." : "Save"}
                </button>
            </div>
        </>
    );
};

export default ChangeAvatarForm;