/*
 * Created by minmin_tranova on 24.02.2026
 */

import { ImSearch } from "react-icons/im";
import { IoMdArrowDropdown } from "react-icons/io";
import { FaPlus } from "react-icons/fa";
import React from "react";
import "../../styles/pages/home/HomePageStyle.css"
import "../../styles/pages/home/HomeSearchStyle.css"
import "../../styles/pages/home/HomeSortStyle.css"
import "../../styles/pages/home/HomeFilterStyle.css"

const STATUS_LABELS = {
    COMPLETED: "Completed",
    PAUSED: "Paused",
    DROPPED: "Dropped",
    WATCHING: "Watching",
    PLAN_TO_WATCH: "Plan to watch",
};

const TYPE_LABELS = {
    MOVIE: "Movie",
    SERIES: "Series",
}

const HomePageView = ({
    displayedItems,
    loading,
    error,
    sortBy,
    sortOpen,
    sortOptions,
    onSortChange,
    onSortToggle,
    filterOptions,
    selectedFilters,
    openFilter,
    onFilterChange,
    onFilterToggle,
    searchQuery,
    suggestions,
    onSearchChange,
    onSuggestionClick,
    sortRef,
    filterRefs,
}) => {
    return (
        <div className="home-page">

            {/* SEARCH INPUT */}
            <div className="home-search-section">
                <div className="home-search-wrapper">
                    <span className="home-search-icon">
                        <ImSearch />
                    </span>
                    <input
                        type="text"
                        placeholder="Search in my collection..."
                        value={searchQuery}
                        onChange={onSearchChange}
                        className="home-search-input"
                    />
                    {/*{suggestions.length > 0 && (*/}
                    {/*    <ul className="home-autocomplete-list">*/}
                    {/*        {suggestions.map((title, idx) => (*/}
                    {/*            <li*/}
                    {/*                key={idx}*/}
                    {/*                onClick={() => onSuggestionClick(title)}*/}
                    {/*                className="home-autocomplete-item"*/}
                    {/*            >*/}
                    {/*                {title}*/}
                    {/*            </li>*/}
                    {/*        ))}*/}
                    {/*    </ul>*/}
                    {/*)}*/}
                </div>

                {/* SORT BUTTON */}
                <div className="home-sort-wrapper" ref={sortRef}>
                    <button
                        className="home-sort-button"
                        onClick={onSortToggle}
                    >
                        <img src="/images/sort.png" alt="sort icon" className="home-sort-icon" />
                    </button>
                    {sortOpen && (
                        <ul className="home-sort-dropdown">
                            {sortOptions.map(opt => {
                                const ArrowIcon = opt.arrow;
                                return (
                                    <li
                                        key={opt.value}
                                        onClick={() => onSortChange(opt.value)}
                                        className={`home-sort-item ${sortBy === opt.value ? 'active' : ''}`}
                                    >
                                        {opt.label} <ArrowIcon/>
                                    </li>
                                );
                            })}
                        </ul>
                    )}
                </div>
            </div>

            {/* FILTERS */}
            <div className="home-filters-row">

                {/* TYPES */}
                <div
                    className="home-filter-dropdown-wrapper"
                    ref={el => filterRefs.current.types = el}
                >
                    <button
                        className="home-filter-btn"
                        onClick={() => onFilterToggle("types")}
                    >
                        {selectedFilters.types.length > 0
                            ? `${selectedFilters.types.length} selected`
                            : 'All Types'}
                        <IoMdArrowDropdown />
                    </button>
                    {openFilter === 'types' && (
                        <ul className="home-filter-dropdown">
                            {filterOptions.types.map(type => (
                                <li key={type} className="home-filter-dropdown-item">
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={selectedFilters.types.includes(type)}
                                            onChange={() => onFilterChange('types', type)}
                                        />
                                        {TYPE_LABELS[type] || type}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* STATUSES */}
                <div
                    className="home-filter-dropdown-wrapper"
                    ref={el => filterRefs.current.statuses = el}
                >
                    <button
                        className="home-filter-btn"
                        onClick={() => onFilterToggle("statuses")}
                    >
                        {selectedFilters.statuses.length > 0
                            ? `${selectedFilters.statuses.length} selected`
                            : 'All Status'}
                        <IoMdArrowDropdown />
                    </button>
                    {openFilter === 'statuses' && (
                        <ul className="home-filter-dropdown">
                            {filterOptions.statuses.map(status => (
                                <li key={status} className="home-filter-dropdown-item">
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={selectedFilters.statuses.includes(status)}
                                            onChange={() => onFilterChange('statuses', status)}
                                        />
                                        {STATUS_LABELS[status] || status}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* GENRES */}
                <div
                    className="home-filter-dropdown-wrapper"
                    ref={el => filterRefs.current.genres = el}
                >
                    <button
                        className="home-filter-btn"
                        onClick={() => onFilterToggle("genres")}
                    >
                        {selectedFilters.genreIds.length > 0
                            ? `${selectedFilters.genreIds.length} selected`
                            : 'All Genres'}
                        <IoMdArrowDropdown />
                    </button>
                    {openFilter === 'genres' && (
                        <ul className="home-filter-dropdown">
                            {filterOptions.genres.map(genre => (
                                <li key={genre.id} className="home-filter-dropdown-item">
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={selectedFilters.genreIds.includes(genre.id)}
                                            onChange={() => onFilterChange('genreIds', genre.id)}
                                        />
                                        {genre.type}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* RELEASE YEARS */}
                <div
                    className="home-filter-dropdown-wrapper"
                    ref={el => filterRefs.current.years = el}
                >
                    <button
                        className="home-filter-btn"
                        onClick={() => onFilterToggle("years")}
                    >
                        {selectedFilters.releaseYears.length > 0
                            ?`${selectedFilters.releaseYears.length} selected`
                            : 'All Released Years'}
                        <IoMdArrowDropdown />
                    </button>
                    {openFilter === 'years' && (
                        <ul className="home-filter-dropdown">
                            {filterOptions.releaseYears.map(year => (
                                <li key={year} className="home-filter-dropdown-item">
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={selectedFilters.releaseYears.includes(year)}
                                            onChange={() => onFilterChange('releaseYears', year)}
                                        />
                                        {year}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* COUNTRIES */}
                <div
                    className="home-filter-dropdown-wrapper"
                    ref={el => filterRefs.current.countries = el}
                >
                    <button
                        className="home-filter-btn"
                        onClick={() => onFilterToggle("countries")}
                    >
                        {selectedFilters.countryIds.length > 0
                            ? `${selectedFilters.countryIds.length} selected`
                            : 'Global'}
                        <IoMdArrowDropdown />
                    </button>
                    {openFilter === 'countries' && (
                        <ul className="home-filter-dropdown">
                            {filterOptions.countries.map(country => (
                                <li key={country.id} className="home-filter-dropdown-item">
                                    <label>
                                        <input
                                            type="checkbox"
                                            checked={selectedFilters.countryIds.includes(country.id)}
                                            onChange={() => onFilterChange('countryIds', country.id)}
                                        />
                                        {country.countryName}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>

            {/* LIST OF MOVIES / SERIES */}

            {/* loading state */}
            {loading && (
                <p>Loading...</p>
            )}

            {/* error state */}
            {!loading && error && (
                <p>{error}</p>
            )}

            {/* Empty list */}
            {!loading && !error && displayedItems.length === 0 && (
                <div className="home-empty-state">
                        <span>
                            <img src="/images/empty-state-icon.png" alt="empty state icon" className="home-empty-state-icon" />
                        </span>
                    <h2>No movies or series yet</h2>
                    <p>
                        Start building your collection by adding your first movie or TV series.
                        Keep track of what you've watched and what you plan to watch!
                    </p>
                    {/* TODO ADDING FIRST MEDIA BUTTON */}
                    <button className="home-add-btn">
                        <FaPlus /> Add Your First Media
                    </button>
                </div>
            )}

            {/* Grid media */}
            {!loading && !error && displayedItems.length > 0 && (
                <div className="home-media-grid">
                    {displayedItems.map(item => (
                        <div key={`${item.type} ${item.id}`} className="home-media-card">
                            <img
                                src={item.poster || '/images/placeholder.png'}
                                alt={item.title}
                                className="home-media-poster"
                            />
                            <span className={`home-media-badge home-badge-${item.status?.toLowerCase()}`}>
                                    {STATUS_LABELS[item.status] || item.status}
                            </span>
                            <div className="home-media-info">
                                <div className="home-media-info-left">
                                    <span className="home-media-type-icon">
                                        {item.type === 'MOVIE'
                                            ? <img src="/images/type-movie.png" alt="movie type" className="home-media-type-img" />
                                            : <img src="/images/type-series.png" alt="series type" className="home-media-type-img" />
                                        }
                                    </span>
                                </div>
                                <div className="home-media-info-right">
                                    <span className="home-media-title">{item.title}</span>
                                    <span className="home-media-year">{item.releaseYear}</span>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default HomePageView;