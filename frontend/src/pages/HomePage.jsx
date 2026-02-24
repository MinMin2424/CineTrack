/*
 * Created by minmin_tranova on 21.02.2026
 */

import React, { useState, useEffect, useCallback, useRef } from "react";
// import { useAuth } from "../contexts/AuthContext"
import { getMediaOverview, getFilterOptions, autocompleteTitles } from "../api/MediaApi";
import { ImSearch } from "react-icons/im";
import { FaSort } from "react-icons/fa6";
import { IoMdArrowDropdown } from "react-icons/io";
import { FaPlus } from "react-icons/fa";

const SORT_OPTIONS = [
    { value: "CREATED_AT_ASC", label: "Oldest", arrow: "↑"},
    { value: "CREATED_AT_DESC", label: "Newest", arrow: "↓"},
    { value: "TITLE_ASC", label: "Title (A-Z)", arrow: "↑"},
    { value: "TITLE_DESC", label: "Title (Z-A)", arrow: "↓"},
    { value: "RELEASE_YEAR_ASC", label: "Released Year - Oldest", arrow: "↑"},
    { value: "RELEASE_YEAR_DESC", label: "Released Year - Newest", arrow: "↓"},
];

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

const HomePage = () => {
    // const { user } = useAuth();
    const [mediaItems, setMediaItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [sortBy, setSortBy] = useState("CREATED_AT_DESC");
    const [sortOpen, setSortOpen] = useState(false);
    const [filterOptions, setFilterOptions] = useState({
        types: [],
        statuses: [],
        genres: [],
        releaseYears: [],
        countries: []
    });
    const [selectedFilters, setSelectedFilters] = useState({
        types: [],
        statuses: [],
        genreIds: [],
        releaseYears: [],
        countryIds: []
    });
    const [openFilter, setOpenFilter] = useState(null);
    const [searchQuery, setSearchQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const debounceTimer = useRef(null);

    const fetchMedia = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await getMediaOverview(sortBy, selectedFilters);
            setMediaItems(data);
        } catch (error) {
            setError("Failed to load media. Please try again.")
        } finally {
            setLoading(false);
        }
    }, [sortBy, selectedFilters]);

    useEffect(() => {
        fetchMedia();
    }, [fetchMedia]);

    useEffect(() => {
        const loadFilterOptions = async () => {
            try {
                const options = await getFilterOptions();
                setFilterOptions({
                    types: options.types || [],
                    statuses: options.statuses || [],
                    genres: options.genres || [],
                    releaseYears: options.releaseYears || [],
                    countries: options.countries || [],
                });
            } catch (error) {
                console.error("Failed to load filter options", error);
            }
        };
        loadFilterOptions();
    }, []);

    useEffect(() => {
        clearTimeout(debounceTimer.current);
        if (searchQuery.trim().length === 0) {
            setSuggestions([]);
            return;
        }
        debounceTimer.current = setTimeout(async () => {
            try {
                const results = await autocompleteTitles(searchQuery.trim());
                setSuggestions(results);
            } catch (error) {
                setSuggestions([]);
            }
        }, 300);
        return () => clearTimeout(debounceTimer.current);
    }, [searchQuery]);

    const handleFilterChange = (filterKey, value) => {
        setSelectedFilters(prev => {
            const current = prev[filterKey];
            const updated = current.includes(value)
                ? current.filter(v => v !== value)
                : [...current, value];
            return { ...prev, [filterKey]: updated };
        });
        // setOpenFilter(null);
    };

    const handleSortChange = (value) => {
        setSortBy(value);
        setSortOpen(false);
    };

    const handleSuggestionClick = (title) => {
        setSearchQuery(title);
        setSuggestions([]);
    };

    const displayedItems = searchQuery.trim()
        ? mediaItems.filter(item =>
            item.title?.toLowerCase().includes(searchQuery.toLowerCase())
        )
        : mediaItems;

    console.log("openFilter:", openFilter);
    console.log("filterOptions:", filterOptions);

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
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="home-search-input"
                    />
                    {suggestions.length > 0 && (
                        <ul className="home-autocomplete-list">
                            {suggestions.map((title, idx) => (
                                <li
                                    key={idx}
                                    onClick={() => handleSuggestionClick(title)}
                                    className="home-autocomplete-item"
                                >
                                    {title}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* SORT BUTTON */}
                <div className="home-sort-wrapper">
                    <button
                        className="home-sort-button"
                        onClick={() => setSortOpen(prev => !prev)}
                    >
                        <FaSort />
                    </button>
                    {sortOpen && (
                        <ul className="home-sort-dropdown">
                            {SORT_OPTIONS.map(opt => (
                                <li
                                    key={opt.value}
                                    onClick={() => handleSortChange(opt.value)}
                                    className={`home-sort-item ${sortBy === opt.value ? 'active' : ''}`}
                                >
                                    {opt.label} {opt.arrow}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>

            {/* FILTERS */}
            <div className="home-filters-row">

                {/* TYPES */}
                <div className="home-filter-dropdown-wrapper">
                    <button
                        className="home-filter-btn"
                        onClick={() => setOpenFilter(openFilter === 'types' ? null : 'types')}
                    >
                        {selectedFilters.types.length > 0
                            ? selectedFilters.types.map(t => TYPE_LABELS[t] || t).join(", ")
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
                                            onChange={() => handleFilterChange('types', type)}
                                        />
                                        {TYPE_LABELS[type] || type}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* STATUSES */}
                <div className="home-filter-dropdown-wrapper">
                    <button
                        className="home-filter-btn"
                        onClick={() => setOpenFilter(openFilter === 'statuses' ? null : 'statuses')}
                    >
                        {selectedFilters.statuses.length > 0
                            ? selectedFilters.statuses.map(s => STATUS_LABELS[s] || s).join(", ")
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
                                            onChange={() => handleFilterChange('statuses', status)}
                                        />
                                        {STATUS_LABELS[status] || status}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* GENRES */}
                <div className="home-filter-dropdown-wrapper">
                    <button
                        className="home-filter-btn"
                        onClick={() => setOpenFilter(openFilter === 'genres' ? null : 'genres')}
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
                                            onChange={() => handleFilterChange('genreIds', genre.id)}
                                        />
                                        {genre.type}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* RELEASE YEARS */}
                <div className="home-filter-dropdown-wrapper">
                    <button
                        className="home-filter-btn"
                        onClick={() => setOpenFilter(openFilter === 'years' ? null : 'years')}
                    >
                        {selectedFilters.releaseYears.length > 0
                            ? selectedFilters.releaseYears.join(", ")
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
                                            onChange={() => handleFilterChange('releaseYears', year)}
                                        />
                                        {year}
                                    </label>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* COUNTRIES */}
                <div className="home-filter-dropdown-wrapper">
                    <button
                        className="home-filter-btn"
                        onClick={() => setOpenFilter(openFilter === 'countries' ? null : 'countries')}
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
                                            onChange={() => handleFilterChange('countryIds', country.id)}
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
                    <p>Start building your collection by adding your first movie or TV series.</p>
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
                        <div key={item.id} className="home-media-card">
                            <img
                                src={item.poster || '/images/placeholder.png'}
                                alt={item.title}
                                className="home-media-poster"
                            />
                            <span className={`home-media-badge home-badge-${item.status?.toLowerCase()}`}>
                                    {item.status}
                                </span>
                            <div className="home-media-info">
                                    <span className="home-media-type-icon">
                                        {item.type === 'MOVIE'
                                            ? <img src="/images/type-movie.png" alt="movie type" className="home-media-type-img" />
                                            : <img src="/images/type-series.png" alt="series type" className="home-media-type-img" />
                                        }
                                    </span>
                                <span className="home-media-title">{item.title}</span>
                                <span className="home-media-year">{item.releaseYear}</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default HomePage;