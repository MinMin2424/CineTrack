/*
 * Created by minmin_tranova on 27.04.2026
 */

import React from "react";
import {useNavigate} from "react-router-dom";
import "../../styles/pages/goodbye/GoodbyePageStyle.css"
import "../../styles/mobile-version/GoodbyeMobileStyle.css"
import { FaArrowRight } from "react-icons/fa";

const GoodbyePage = () => {
    const navigate = useNavigate();
    return (
        <div className="goodbye-page">
            <img src="/images/wall-e-goodbye.png" alt="wall-e goodbye" className="goodbye-walle" />
            <img src="/images/wall-e-boot.png" alt="wall-e boot goodbye" className="goodbye-boot" />

            <div className="goodbye-card">
                <h1 className="goodbye-title">Account deleted</h1>
                <p className="goodbye-text">
                    We're sorry to see you go.
                    Your entire profile has been successfully and permanently removed from our platform.
                </p>
                <p className="goodbye-text">
                    Thank you for the time you spent with us. <br/>
                    Farewell!
                </p>
            </div>
            <button
                className="goodbye-btn"
                onClick={() => navigate("/auth/login", {replace: true})}
            >
                Go back to login <FaArrowRight />
            </button>
        </div>
    );
};

export default GoodbyePage;