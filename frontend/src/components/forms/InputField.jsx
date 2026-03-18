/*
 * Created by minmin_tranova on 10.03.2026
 */

import React from "react";
import "../../styles/components/forms/AddMediaFormStyle.css"

const InputField = ({
    label,
    required = false,
    error,
    type = "text",
    children,
    className = "",
    ...rest
}) => {
    const inputClass = `modal-input ${error ? "input-error" : ""} ${className}`.trim();
    const renderInput = () => {
        if (type === "textarea") {
            return (
                <textarea className={`${inputClass} modal-textarea`} {...rest}/>
            );
        }
        if (type === "select") {
            return (
                <select className={`${inputClass} modal-select`} {...rest}>
                    {children}
                </select>
            )
        }
        return (
            <input type={type} className={inputClass} {...rest} />
        );
    };
    return (
        <div className="modal-field">
            {label && (
                <label className="modal-label">
                    {label}{required && <span className="required-star"> *</span>}
                </label>
            )}
            {renderInput()}
            {error && <p className="field-error">{error}</p>}
        </div>
    );
};

export default InputField;