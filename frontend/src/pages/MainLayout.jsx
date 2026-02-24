/*
 * Created by minmin_tranova on 21.02.2026
 */

import React, { useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import TopBar from "../components/Topbar";

const MainLayout = () => {
    const [sidebarOpen, setSidebarOpen] = useState(true);

    return (
        <div className={`main-layout ${sidebarOpen ? "" : "main-layout--collapsed"}`}>
            <Sidebar />
            <div className="main-layout-content">
                <TopBar onMenuToggle={() => setSidebarOpen(prev => !prev)} />
                <main className="main-layout-main">
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default MainLayout;