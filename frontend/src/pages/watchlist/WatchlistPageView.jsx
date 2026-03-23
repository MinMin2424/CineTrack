/*
 * Created by minmin_tranova on 18.03.2026
 */

import React from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/pages/watchlist/WatchlistPageStyle.css"
import "../../styles/pages/home/HomePageStyle.css"
import "../../styles/components/layout/SpinnerStyle.css"

const WatchlistPageView = ({
    items,
    loading,
    error,
}) => {
    const navigate = useNavigate();
    const handleCardClick = (item) => {
        if (item.type === "MOVIE") navigate(`/movies/${item.id}`);
        if (item.type === "SERIES") navigate(`/series/${item.id}`);
    };

    return (
        <div className="home-page">

            {/* loading state */}
            {loading && (
                <div className="loading">
                    <div className="spinner"/>
                </div>
            )}

            {/* error state */}
            {!loading && error && (
                <p className="watchlist-error">{error}</p>
            )}

            {/* empty */}
            {!loading && !error && items.length === 0 && (
                <div className="home-empty-state">
                    <img
                        src="/images/empty-state-icon.png"
                        alt="empty"
                        className="watchlist-empty-icon"
                    />
                    <h2>Your watchlist is empty</h2>
                    <p>Add movies or series with status "Plan to watch" to see them here.</p>
                </div>
            )}

            {!loading && !error && items.length > 0 && (
                <div className="home-media-grid">
                    {items.map((item) => (
                        <div
                            key={`${item.type}-${item.id}`}
                            className="home-media-card"
                            onClick={() => handleCardClick(item)}
                        >
                            <img
                                src={item.poster || "/images/placeholder.png"}
                                alt={item.title}
                                className="home-media-poster"
                                onError={(e) => {e.target.src = "/images/placeholder.png";}}
                            />
                            <div className="watchlist-info">
                                <span className="watchlist-title">{item.title}</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default WatchlistPageView;