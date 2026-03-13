/*
 * Created by minmin_tranova on 13.03.2026
 */

import React from "react";
import "../../styles/components/popups/DeleteMediaPopup.css"

const DeleteMediaPopup = ({
    title,
    onConfirm,
    onCancel,
    loading = false,
}) => {
    return (
        <div className="delete-popup-overlay" onClick={onCancel}>
            <div className="delete-popup" onClick={e => e.stopPropagation()}>
                <h3 className="delete-popup-title">
                    You are about to stop following this movie.
                </h3>
                <p className="delete-popup-text">
                    This will remove the {title} from your overview.<br />
                    Are you sure?
                </p>
                <div className="delete-popup-actions">
                    <button
                        className="delete-popup-cancel"
                        onClick={onCancel}
                        disabled={loading}
                    >
                        Cancel
                    </button>
                    <button
                        className="delete-popup-confirm"
                        onClick={onConfirm}
                        disabled={loading}
                    >
                        {loading ? "Deleting..." : "Delete"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default DeleteMediaPopup;