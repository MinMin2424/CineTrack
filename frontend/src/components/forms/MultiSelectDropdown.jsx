/*
 * Created by minmin_tranova on 08.03.2026
 */

import React, { useState, useRef, useEffect } from "react";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/components/forms/MultiSelectDropdownStyle.css"

const MultiSelectDropdown = ({
    label,
    placeholder,
    options,
    selected,
    onToggle,
    error,
}) => {
    const [open, setOpen] = useState(false);
    const wrapperRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (wrapperRef.current && !wrapperRef.current.contains(e.target)) {
                setOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const displayValue = selected.length > 0 ? selected.join(", ") : "";

    return (
        <div className="manual-multiselect-wrapper" ref={wrapperRef}>
            <label className="modal-label">
                {label}
            </label>
            <div
                className={`manual-multiselect-trigger modal-input ${error ? "input-error" : ""}`}
                onClick={() => setOpen(prev => !prev)}
            >
                <span className={`manual-multiselect-value ${selected.length === 0 ? "placeholder" : ""}`}>
                    {displayValue || placeholder}
                </span>
                <IoIosArrowDown className="manual-multiselect-arrow" />
            </div>
            {open && (
                <ul className="manual-multiselect-dropdown">
                    {options.map(opt => (
                        <li key={opt.id} className="manual-multiselect-item">
                            <label>
                                <input
                                    type="checkbox"
                                    checked={selected.includes(opt.label)}
                                    onChange={() => onToggle(opt.label)}
                                />
                                {opt.label}
                            </label>
                        </li>
                    ))}
                </ul>
            )}
            {error && <p className="manual-field-error">{error}</p>}
        </div>
    );
};

export default MultiSelectDropdown;