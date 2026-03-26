/*
 * Created by minmin_tranova on 25.03.2026
 */

import "../../styles/components/statistics/GenreBarStyle.css"

const GenreBar = ({
    genre,
    count,
    maxCount,
}) => {
    const pct = maxCount > 0 ? (count / maxCount) * 100 : 0;
    return (
        <div className="stat-genre-row">
            <span className="stat-genre-label">{genre}</span>
            <div className="stat-genre-bar-track">
                <div className="stat-genre-bar-fill" style={{width: `${pct}%`}}/>
            </div>
        </div>
    );
};

export default GenreBar;