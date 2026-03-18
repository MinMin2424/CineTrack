/*
 * Created by minmin_tranova on 18.03.2026
 */

import React, { useState, useCallback } from "react";
import {discoverMedia, getMediaOverview} from "../../api/MediaApi";
import DiscoveryPageView from "./DiscoveryPageView";

const DiscoveryPageContainer = () => {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [addedIds, setAddedIds] = useState(new Set());
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searched, setSearched] = useState(false);

    const [showAddForm, setShowAddForm] = useState(false);
    const [selectedMedia, setSelectedMedia] = useState(null);

    // const fetchAddedIds = useCallback(async () => {
    //     try {
    //         const data = await getMediaOverview();
    //         const ids = new Set(data.map(item => item.imdbID).filter(Boolean));
    //         setAddedIds(ids);
    //     } catch (error) {
    //         console.error("Failed to fetch existing media");
    //     }
    // }, []);

    const handleSearch = async (searchQuery = query) => {
        if (!searchQuery.trim()) return;
        setLoading(true);
        setError(null);
        setSearched(true);
        try {
            // await fetchAddedIds();
            const [discoverResult, overviewResult] = await Promise.allSettled([
                discoverMedia(searchQuery, 10),
                getMediaOverview(),
            ]);
            const ids = new Set(
                overviewResult.status === "fulfilled" && overviewResult.value
                    ? overviewResult.value.map(item => item.imdbID).filter(Boolean)
                    : []
            );
            setAddedIds(ids);
            setResults(
                discoverResult.status === "fulfilled"
                    ? discoverResult.value
                    : []
            );
        } catch (error) {
            setError("Nothing found.");
        } finally {
            setLoading(false);
        }
    };

    const handleQueryChange = (e) => {
        setQuery(e.target.value);
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") handleSearch();
    };

    const handleAddClick = (omdbItem) => {
        setSelectedMedia(omdbItem);
        setShowAddForm(true);
    };

    const handleAddSuccess = (imdbID) => {
        setAddedIds(prev => new Set([...prev, imdbID]));
        setShowAddForm(false);
        setSelectedMedia(null);
    };

    const handleFormClose = () => {
        setShowAddForm(false);
        setSelectedMedia(null);
    };

    return (
        <DiscoveryPageView
            query={query}
            results={results}
            addedIds={addedIds}
            loading={loading}
            error={error}
            searched={searched}
            showAddForm={showAddForm}
            selectedMedia={selectedMedia}
            onQueryChange={handleQueryChange}
            onKeyDown={handleKeyDown}
            onSearch={handleSearch}
            onAddClick={handleAddClick}
            onAddSuccess={handleAddSuccess}
            onFormClose={handleFormClose}
        />
    );
};

export default DiscoveryPageContainer;