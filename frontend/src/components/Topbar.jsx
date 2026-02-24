/*
 * Created by minmin_tranova on 21.02.2026
 */

import { useAuth } from "../contexts/AuthContext";
import { TiThMenu } from "react-icons/ti";
import { FaUserCircle } from "react-icons/fa";

const TopBar = ({ onMenuToggle }) => {
    const { user } = useAuth();
    const username = user.username;

    return (
        <header className="topbar">
            <button
                className="topbar-menu-btn"
                onClick={onMenuToggle}
                aria-label="Menu"
            >
                <TiThMenu />
            </button>
            <div className="topbar-user">
                <FaUserCircle className="topbar-avatar" />
                <span className="topbar-username">{username}</span>
            </div>
        </header>
    );
};

export default TopBar;