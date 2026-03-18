/*
 * Created by minmin_tranova on 08.03.2026
 */

import React, { useState, useRef } from 'react';
import {IoClose} from "react-icons/io5";
import axiosConfig from "../../api/AxiosConfig";
import "../../styles/components/forms/AddMediaFormStyle.css"
import "../../styles/components/forms/UploadPosterStyle.css"

const PosterUpload = ({
    value,
    onChange,
    error
}) => {
    const [dragging, setDragging] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [uploadError, setUploadError] = useState(null);
    const inputRef = useRef(null);

    const uploadFile = async (file) => {
        if (!file.type.startsWith("image/")) {
            setUploadError("Only image files are allowed (JPG, PNG, WEBP, ...");
            return;
        }
        if (file.size > 5 * 1024 * 1024) {
            setUploadError("File is too large. Maximum size is 5 MB.");
            return;
        }
        setUploading(true);
        setUploadError(null);

        try {
            const formData = new FormData();
            formData.append("file", file);
            const response = await axiosConfig.post("/media/upload/poster", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            onChange(response.data.posterUrl);
        } catch (error) {
            setUploadError("Upload failed. Please try again.");
        } finally {
            setUploading(false);
        }
    };

    const handleFileInput = (e) => {
        const file = e.target.files?.[0];
        if (file) uploadFile(file);
    };

    const handleDrop = (e) => {
        e.preventDefault();
        setDragging(false);
        const file = e.dataTransfer.files?.[0];
        if (file) uploadFile(file);
    };

    const handleDragOver = (e) => {
        e.preventDefault();
        setDragging(true);
    };

    const handleDragLeave = () => setDragging(false);

    const handleRemove = (e) => {
        e.stopPropagation();
        onChange("");
        if (inputRef.current) inputRef.current.value = "";
    };

    return (
        <div className="poster-upload-wrapper">
            <label className="modal-label">Poster <span className="required-star"> *</span></label>
            {value ? (
                <div className="poster-upload-preview">
                    <img src={value} alt="poster preview" className="poster-upload-img" />
                    <button
                        className="poster-upload-remove"
                        onClick={handleRemove}
                        type="button"
                    >
                        <IoClose />
                    </button>
                </div>
            ) : (
                <div
                    className={`poster-upload-zone ${dragging ? "poster-upload-zone--dragging" : ""} ${error ? "input-error" : ""}`}
                    onClick={() => inputRef.current?.click()}
                    onDrop={handleDrop}
                    onDragOver={handleDragOver}
                    onDragLeave={handleDragLeave}
                >
                    <input
                        ref={inputRef}
                        type="file"
                        accept="image/*"
                        className="poster-upload-input"
                        onChange={handleFileInput}
                    />
                    {uploading ? (
                        <p className="poster-upload-text">Uploading...</p>
                    ) : (
                        <>
                            <span className="poster-upload-icon">+</span>
                            <p className="poster-upload-text">
                                Drag & drop or <span className="poster-upload-link">browse</span>
                            </p>
                            <p className="poster-upload-hint">
                                JPG, PNG, WEBP - max 5 MB
                            </p>
                        </>
                    )}
                </div>
            )}
            {(error || uploadError) && (
                <p className="manual-field-error">{uploadError || error}</p>
            )}
        </div>
    );
};

export default PosterUpload;