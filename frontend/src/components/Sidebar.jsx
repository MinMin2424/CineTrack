/*
 * Created by minmin_tranova on 21.02.2026
 */

import React from "react";
import { NavLink } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext"
import { HiHome } from "react-icons/hi2";
import { BsFillBarChartLineFill } from "react-icons/bs";
import { FaBookmark } from "react-icons/fa";
import { BiSearchAlt } from "react-icons/bi";
import { IoIosSettings } from "react-icons/io";
import { FiLogOut } from "react-icons/fi";

const NAV_ITEMS = [
    { to: "/", label: "HOME", Icon: HiHome },
    { to: "/statistics", label: "STATISTICS", Icon: BsFillBarChartLineFill },
    { to: "/watchlist", label: "WATCHLIST", Icon: FaBookmark },
    { to: "/discovery", label: "DISCOVERY", Icon: BiSearchAlt },
    { to: "/settings", label: "SETTINGS", Icon: IoIosSettings },
];

const Sidebar = () => {
    const { logout } = useAuth();
    return (
        <aside className="sidebar">
            <div className="sidebar-logo">
                <span className="sidebar-logo-text">CineTrack</span>
            </div>
            <div className="sidebar-nav">
                {NAV_ITEMS.map(({ to, label, Icon}) => (
                    <NavLink
                        key={to}
                        to={to}
                        end={to === "/"}
                        className={({ isActive }) => `sidebar-nav-item ${isActive ? "sidebar-nav-item--active" : ""}`}
                    >
                        <Icon className="sidebar-nav-icon" />
                        <span>{label}</span>
                    </NavLink>
                ))}
            </div>
            <div className="sidebar-bottom">
                <button
                    className="sidebar-logout-btn"
                    onClick={logout}
                    aria-label="Log out"
                >
                    <span>LOG OUT</span>
                    <FiLogOut className="sidebar-nav-icon" />
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;
