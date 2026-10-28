/*
 * Created by minmin_tranova on 21.02.2026
 */

import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/layout/Sidebar";
import TopBar from "../components/layout/Topbar";
import "../styles/pages/MainLayoutStyle.css"
import "../styles/mobile-version/MainLayoutMobileStyle.css"

const DEFAULT_FILTERS = {
    types: [],
    statuses: [],
    genreIds: [],
    releaseYears: [],
    countryIds: []
};

const MainLayout = () => {
    const [sidebarOpen, setSidebarOpen] = useState(true);

    const [homeFilters, setHomeFilters] = useState(DEFAULT_FILTERS);
    const [homeSortBy, setHomeSortBy] = useState("RELEASE_YEAR_DESC");
    const [homeSearchQuery, setHomeSearchQuery] = useState("");

    return (
        <div className={`main-layout ${sidebarOpen ? "" : "main-layout--collapsed"}`}>
            <Sidebar />
            <div className="main-layout-content">
                <TopBar onMenuToggle={() => setSidebarOpen(prev => !prev)} />
                <main className="main-layout-main" id="app-content">
                    <Outlet context={{
                        homeFilters, setHomeFilters,
                        homeSortBy, setHomeSortBy,
                        homeSearchQuery, setHomeSearchQuery
                    }}/>
                </main>
            </div>
        </div>
    );
};

export default MainLayout;