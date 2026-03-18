/*
 * Created by minmin_tranova on 18.03.2026
 */

import React, { useState, useEffect } from "react";
import { getMediaOverview } from "../../api/MediaApi";
import WatchlistPageView from "./WatchlistPageView";

const WatchlistPageContainer = () => {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchWatchlist = async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await getMediaOverview(
                    "CREATED_AT_DESC",
                    {statuses: ["PLAN_TO_WATCH"]}
                );
                setItems(data);
            } catch (e) {
                setError(e.response?.data?.error || "Failed to load watchlist");
            } finally {
                setLoading(false);
            }
        };
        fetchWatchlist();
    }, []);

    return (
        <WatchlistPageView
            items={items}
            loading={loading}
            error={error}
        />
    );
};

export default WatchlistPageContainer;