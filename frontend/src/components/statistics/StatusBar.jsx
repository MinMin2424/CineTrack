/*
 * Created by minmin_tranova on 25.03.2026
 */

import "../../styles/components/statistics/StatusBarStyle.css"

const StatusBar = ({
    count,
    maxCount,
    side
}) => {
    const pct = maxCount > 0 ? (count/maxCount) * 100 : 0;
    return (
        <div className={`stat-bar-track stat-bar-track--${side}`}>
            <div
                className={`stat-bar-fill stat-bar-fill--${side}`}
                style={{width: `${pct}%`}}
            />
        </div>
    )
};

export default StatusBar;