/*
 * Created by minmin_tranova on 21.02.2026
 */

import React, { useState, useEffect, useCallback, useRef } from "react";
import { useAuth } from "../../contexts/AuthContext"
import { getMediaOverview, getFilterOptions, autocompleteTitles } from "../../api/MediaApi";
import { ImSearch } from "react-icons/im";
import { FaSort } from "react-icons/fa6";

const SORT_OPTIONS = [
    { value: "CREATED_AT_ASC", label: "Oldest", arrow: "↑"},
    { value: "CREATED_AT_DESC", label: "Newest", arrow: "↓"},
    { value: "TITLE_ASC", label: "Title (A-Z)", arrow: "↑"},
    { value: "TITLE_DESC", label: "Title (Z-A)", arrow: "↓"},
    { value: "RELEASE_YEAR_ASC", label: "Released Year - Oldest", arrow: "↑"},
    { value: "RELEASE_YEAR_DESC", label: "Released Year - Newest", arrow: "↓"},
];

const HomePage = () => {
    const { user } = useAuth();
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
        genres: [],
        releaseYears: [],
        countries: []
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
        setOpenFilter(null);
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

}