/*
 * Created by minmin_tranova on 18.03.2026
 */

import React from "react";
import { ImSearch } from "react-icons/im";
import { FaPlus } from "react-icons/fa";
import { FaCheck } from "react-icons/fa";
import { FaRegStar } from "react-icons/fa";
import { FaStarHalfAlt } from "react-icons/fa";
import { FaStar } from "react-icons/fa";
import "../../styles/components/layout/SpinnerStyle.css"
import "../../styles/pages/discovery/DiscoveryPageStyle.css"
import AddMovieForm from "../../components/forms/movie/AddMovieForm";
import AddSeriesForm from "../../components/forms/series/AddSeriesForm";

const StarRating = ({rating}) => {
    const num = parseInt(rating);
    const isValid = !isNaN(num) && rating !== "N/A";
    const filled = isValid ? Math.round(num / 2) : 0;
    return (
        <div className="disc-stars">
            {Array.from({length: 5}, (_, i) => (
                i < filled
                    ? <FaStar key={i} className="disc-star disc-star--filled" />
                    : <FaRegStar key={i} className="disc-star disc-star--empty" />
            ))}
            <span className="disc-rating-label">
                {isValid ? `${num}/10` : "Not yet rated"}
            </span>
        </div>
    );
};

const DiscoveryPageView = ({
    query,
    results,
    addedIds,
    loading,
    error,
    searched,
    showAddForm,
    selectedMedia,
    onQueryChange,
    onKeyDown,
    onSearch,
    onAddClick,
    onAddSuccess,
    onFormClose,
}) => {
    const showShip = !loading && results.length === 0;
    const showNoResults = !loading && !error && searched && results.length === 0;
    return (
        <div className="disc-page">

            <img
                src="/images/jack-sparrow.png"
                alt="Jack Sparrow"
                className="disc-jack"
            />

            {/* SEARCH BAR */}
            <div className="disc-search-wrapper">
                <ImSearch className="disc-search-icon" />
                <input
                    type="text"
                    className="disc-search-input"
                    placeholder="Discover new content..."
                    value={query}
                    onChange={onQueryChange}
                    onKeyDown={onKeyDown}
                />
            </div>

            {/* LOADING */}
            {loading && (
                <div className="loading">
                    <div className="spinner" />
                </div>
            )}

            {/* ERROR */}
            {!loading && error && (
                <p className="disc-error">{error}</p>
            )}

            {/* NO RESULT */}
            {showShip && (
                <div className="disc-ship-wrapper">
                    <img
                        src="/images/ship.png"
                        alt="Ship"
                        className="disc-ship"
                    />
                    {showNoResults && (
                        <p className="disc-no-result">No results found for "{query}"</p>
                    )}
                </div>
            )}

            {/* RESULTS */}
            {!loading && !error && results.length > 0 && (
                <div className="disc-results">
                    {results.map(item => {
                        const isAdded = addedIds.has(item.imdbID);
                        return (
                            <div
                                key={item.imdbID}
                                className="disc-card"
                            >
                                <img
                                    src={item.Poster !== "N/A" ? item.Poster : "/images/placeholder.png"}
                                    alt={item.Title}
                                    className="disc-card-poster"
                                />
                                <div className="disc-card-info">
                                    <div className="disc-card-header">
                                        <h3 className="disc-card-title">{item.Title}</h3>
                                        {isAdded ? (
                                            <button className="disc-btn-added" disabled>
                                                Added <FaCheck />
                                            </button>
                                        ) : (
                                            <button
                                                className="disc-btn-add"
                                                onClick={() => onAddClick(item)}
                                            >
                                                <FaPlus />
                                            </button>
                                        )}
                                    </div>
                                    <p className="disc-card-year">{item.Year}</p>
                                    <StarRating rating={item.imdbRating} />
                                    <p className="disc-card-plot-label">Plot:</p>
                                    <div className="disc-card-plot-wrapper">
                                        <p className="disc-card-plot">
                                            {item.Plot !== "N/A" ? item.Plot : "-"}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                </div>
            )}

            {/* ADD FORMS */}
            {showAddForm && selectedMedia && (
                selectedMedia.Type === "series"
                    ? <AddSeriesForm
                        omdbData={selectedMedia}
                        onSuccess={() => onAddSuccess(selectedMedia.imdbID)}
                        onBack={onFormClose}
                        onClose={onFormClose}
                    />
                    : <AddMovieForm
                        omdbData={selectedMedia}
                        onSuccess={() => onAddSuccess(selectedMedia.imdbID)}
                        onBack={onFormClose}
                        onClose={onFormClose}
                    />
            )}
        </div>
    );
};

export default DiscoveryPageView;