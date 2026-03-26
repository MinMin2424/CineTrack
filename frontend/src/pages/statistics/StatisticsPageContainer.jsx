/*
 * Created by minmin_tranova on 24.03.2026
 */

import React, {useState, useEffect} from 'react';
import {
    getStatusOverview,
    getSummary,
    getTopGenres,
    getTopCountries,
    getMonthlyActive,
    getOtherStats,
    getFunFacts,
    getTopRated
} from "../../api/StatisticsApi";
import StatisticsPageView from "./StatisticsPageView";

const StatisticsPageContainer = () => {
    const now = new Date();
    const [selectedYear, setSelectedYear] = useState(now.getFullYear());
    const [selectedMonth, setSelectedMonth] = useState(now.getMonth() + 1);
    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [statusOverview, setStatusOverview] = useState(null);
    const [summary, setSummary] = useState(null);
    const [topGenres, setTopGenres] = useState([]);
    const [topCountries, setTopCountries] = useState([]);
    const [monthlyActive, setMonthlyActive] = useState(null);
    const [otherStats, setOtherStats] = useState(null);
    const [funFacts, setFunFacts] = useState(null);
    const [topRated, setTopRated] = useState(null);

    useEffect(() => {
        fetchAllStatistics();
    }, []);

    const fetchAllStatistics = async () => {
        setLoading(true);
        setError(null);
        try {
            const [
                statusData,
                summaryData,
                genresData,
                countriesData,
                monthlyData,
                otherData,
                funFactsData,
                topRatedData
            ] = await Promise.all([
                getStatusOverview(),
                getSummary(),
                getTopGenres(15),
                getTopCountries(5),
                getMonthlyActive(),
                getOtherStats(),
                getFunFacts(),
                getTopRated()
            ]);
            setStatusOverview(statusData);
            setSummary(summaryData);
            setTopGenres(genresData);
            setTopCountries(countriesData);
            setMonthlyActive(monthlyData);
            setOtherStats(otherData);
            setFunFacts(funFactsData);
            setTopRated(topRatedData);
        } catch (error) {
            setError("Failed to load statistics data. Please try again later.")
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const fetchMonthly = async () => {
            try {
                const data = await getMonthlyActive(selectedYear, selectedMonth);
                setMonthlyActive(data);
            } catch (error) {
                console.error("Failed to load monthly data", error);
            }
        };
        fetchMonthly();
    }, [selectedYear, selectedMonth]);

    const handleMonthChange = (year, month) => {
        setSelectedYear(year);
        setSelectedMonth(month);
    }

    return (
        <StatisticsPageView
            loading={loading}
            error={error}
            statusOverview={statusOverview}
            summary={summary}
            topGenres={topGenres}
            topCountries={topCountries}
            monthlyActive={monthlyActive}
            otherStats={otherStats}
            funFacts={funFacts}
            topRated={topRated}
            selectedYear={selectedYear}
            selectedMonth={selectedMonth}
            onMonthChange={handleMonthChange}
        />
    );
};

export default StatisticsPageContainer;