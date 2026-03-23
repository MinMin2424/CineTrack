/*
 * Created by minmin_tranova on 18.03.2026
 */

import React, { useState } from "react";
import {discoverMedia, getMediaOverview} from "../../api/MediaApi";
import DiscoveryPageView from "./DiscoveryPageView";
import {useToast} from "../../hooks/UseToast";

const DiscoveryPageContainer = () => {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [addedItems, setAddedItems] = useState(new Map());
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searched, setSearched] = useState(false);

    const [showAddForm, setShowAddForm] = useState(false);
    const [selectedMedia, setSelectedMedia] = useState(null);

    const {toast, showToast, hideToast} = useToast();

    const handleSearch = async (searchQuery = query) => {
        if (!searchQuery.trim()) return;
        setLoading(true);
        setError(null);
        setSearched(true);
        try {
            const [discoverResult, overviewResult] = await Promise.allSettled([
                discoverMedia(searchQuery, 10),
                getMediaOverview(),
            ]);
            const itemsMap = new Map(
                overviewResult.status === "fulfilled" && overviewResult.value
                    ? overviewResult.value
                        .filter(item => item.imdbID)
                        .map(item => [item.imdbID, item])
                    : []
            );
            setAddedItems(itemsMap);
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
        setSearched(false);
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") handleSearch();
    };

    const handleAddClick = (omdbItem) => {
        setSelectedMedia(omdbItem);
        setShowAddForm(true);
    };

    const handleAddSuccess = (imdbID, title) => {
        setAddedItems(prev => new Map([...prev, [imdbID, {imdbID, rating: 0}]]));
        setShowAddForm(false);
        setSelectedMedia(null);
        showToast(`${title} has been successfully added to your collection.`)
    };

    const handleFormClose = () => {
        setShowAddForm(false);
        setSelectedMedia(null);
    };

    return (
        <DiscoveryPageView
            query={query}
            results={results}
            addedItems={addedItems}
            loading={loading}
            error={error}
            searched={searched}
            showAddForm={showAddForm}
            selectedMedia={selectedMedia}
            toast={toast}
            onQueryChange={handleQueryChange}
            onKeyDown={handleKeyDown}
            onSearch={handleSearch}
            onAddClick={handleAddClick}
            onAddSuccess={handleAddSuccess}
            onFormClose={handleFormClose}
            onToastClose={hideToast}
        />
    );
};

export default DiscoveryPageContainer;