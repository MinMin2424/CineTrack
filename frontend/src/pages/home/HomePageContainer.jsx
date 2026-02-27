/*
 * Created by minmin_tranova on 24.02.2026
 */

import React, { useState, useEffect, useCallback, useRef } from "react";
import { getMediaOverview, getFilterOptions, autocompleteTitles } from "../../api/MediaApi";
import HomePageView from "./HomePageView";
import AddMediaModal from "../../components/media/AddMediaModal"
import {FaLongArrowAltDown, FaLongArrowAltUp} from "react-icons/fa";

const SORT_OPTIONS = [
    { value: "CREATED_AT_ASC", label: "Oldest", arrow: FaLongArrowAltUp},
    { value: "CREATED_AT_DESC", label: "Newest", arrow: FaLongArrowAltDown},
    { value: "TITLE_ASC", label: "Title (A-Z)", arrow: FaLongArrowAltUp},
    { value: "TITLE_DESC", label: "Title (Z-A)", arrow: FaLongArrowAltDown},
    { value: "RELEASE_YEAR_ASC", label: "Released Year - Oldest", arrow: FaLongArrowAltUp},
    { value: "RELEASE_YEAR_DESC", label: "Released Year - Newest", arrow: FaLongArrowAltDown},
];

const HomePage = () => {
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

    const sortRef = useRef(null);
    const filterRefs = useRef({
        types: null,
        statuses: [],
        genres: [],
        years: [],
        countries: []
    });

    const [showAddModal, setShowAddModal] = useState(false);

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

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (sortRef.current && !sortRef.current.contains(event.target)) {
                setSortOpen(false);
            }
            if (openFilter) {
                const filterRef = filterRefs.current[openFilter];
                if (filterRef && !filterRef.contains(event.target)) {
                    setOpenFilter(null);
                }
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [openFilter]);

    const handleFilterChange = (filterKey, value) => {
        setSelectedFilters(prev => {
            const current = prev[filterKey];
            const updated = current.includes(value)
                ? current.filter(v => v !== value)
                : [...current, value];
            return {...prev, [filterKey]: updated};
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

    const handleMediaAdded = () => {
        fetchMedia();
    };

    return (
        <>
            <HomePageView
                displayedItems={displayedItems}
                loading={loading}
                error={error}
                sortBy={sortBy}
                sortOpen={sortOpen}
                sortOptions={SORT_OPTIONS}
                onSortChange={handleSortChange}
                onSortToggle={() => setSortOpen(prev => !prev)}
                filterOptions={filterOptions}
                selectedFilters={selectedFilters}
                openFilter={openFilter}
                onFilterChange={handleFilterChange}
                onFilterToggle={(key) => setOpenFilter(openFilter === key ? null : key)}
                searchQuery={searchQuery}
                suggestions={suggestions}
                onSearchChange={(e) => setSearchQuery(e.target.value)}
                onSuggestionClick={handleSuggestionClick}
                sortRef={sortRef}
                filterRefs={filterRefs}
                onAddMedia={() => setShowAddModal(true)}
            />
            {showAddModal && (
                <AddMediaModal
                    onClose={() => setShowAddModal(false)}
                    onMediaAdded={handleMediaAdded}
                />
            )}
        </>
    );
}

export default HomePage;