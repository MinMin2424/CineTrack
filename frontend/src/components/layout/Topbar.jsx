/*
 * Created by minmin_tranova on 21.02.2026
 */

import { useAuth } from "../../contexts/AuthContext";
import { IoMenu } from "react-icons/io5";
import "../../styles/components/TopbarStyle.css"

const TopBar = ({ onMenuToggle }) => {
    const { user, userProfile } = useAuth();
    const username = userProfile?.profile?.username || user?.username || "User";
    const avatar = userProfile?.header?.avatar;

    return (
        <header className="topbar">
            <button
                className="topbar-menu-btn"
                onClick={onMenuToggle}
                aria-label="Menu"
            >
                <IoMenu />
            </button>
            <div className="topbar-user">
                <img src={avatar} alt="user avatar" className="topbar-avatar" />
                <span className="topbar-username">{username}</span>
            </div>
        </header>
    );
};

export default TopBar;