/*
 * Created by minmin_tranova on 24.03.2026
 */

import React from "react";
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
    ResponsiveContainer, Cell
} from "recharts";
import "../../styles/pages/statistics/StatisticsPageStyle.css"
import "../../styles/mobile-version/StatisticsMobileStyle.css"
import "../../styles/components/layout/SpinnerStyle.css"
import {FiCheckCircle} from "react-icons/fi";
import {FaRegCirclePlay, FaRegClock} from "react-icons/fa6";
import {SlClose} from "react-icons/sl";
import {FaAward} from "react-icons/fa";
import { FaArrowUp } from "react-icons/fa";
import { FaArrowDown } from "react-icons/fa";
import StatusBar from "../../components/statistics/StatusBar";
import DonutChart from "../../components/statistics/DonutChart";
import GenreBar from "../../components/statistics/GenreBar";
import StarRating from "../../components/layout/StarRating";

const StatisticsPageView = ({
    loading,
    error,
    statusOverview,
    summary,
    topGenres,
    topCountries,
    monthlyActive,
    otherStats,
    funFacts,
    topRated,
    selectedYear,
    selectedMonth,
    onMonthChange,
}) => {
    if (loading) return (
        <div className="loading">
            <div className="spinner"/>
        </div>
    );

    if (!statusOverview || !summary) return null;

    const statuses = [
        { key: "completed", label: "Total Completed", icon: <FiCheckCircle />, name: "Completed" },
        { key: "watching", label: "Total Watching", icon: <FaRegCirclePlay />, name: "Watching" },
        { key: "planToWatch", label: "Total Plan to Watch", icon: <FaRegClock />, name: "Plan to Watch" },
        { key: "dropped", label: "Total Dropped", icon: <SlClose />, name: "Dropped" },
    ];

    const maxMovies = Math.max(...statuses.map(s=> statusOverview[s.key]?.movies || 0));
    const maxSeries = Math.max(...statuses.map(s=> statusOverview[s.key]?.series || 0));
    const maxGenre = topGenres.length > 0 ? topGenres[0].count : 1;

    const countriesChartData = topCountries.map(c => ({
        name: c.country,
        Movie: c.movies,
        Series: c.series,
    }));

    const diff = monthlyActive ? (monthlyActive.diffFromPreviousMonth ?? 0) : 0;
    const prevMonth = selectedMonth === 1 ? 12 : selectedMonth - 1;
    const prevYear = selectedYear === 1 ? selectedYear - 1 : selectedYear;

    return (
        <div className="statistics-page">

            {/* STATUS CARDS GRID */}
            <div className="statistics-cards-grid">
                {statuses.map((status) => {
                    const data = statusOverview[status.key];
                    return (
                        <div key={status.key} className="status-card">
                            <div className="status-card-total">{data.total}</div>
                            <div className="status-card-info">
                                <div className="status-card-label">{status.label}</div>
                                <div className="status-card-icon">{status.icon}</div>
                            </div>
                        </div>
                    );
                })}
                {/* OTHER STATUS */}
                {otherStats && (
                    <div className="stat-others-wrapper">
                        <div className="status-breakdown-label">Other status</div>
                        <div className="stat-other-rows">
                            <div className="stat-other-row">
                                <span className="stat-other-label">Completed This Year</span>
                                <span className="stat-other-value">{otherStats.completedThisYear}</span>
                            </div>
                            <div className="stat-other-row">
                                <span className="stat-other-label">Average Rating</span>
                                <span className="stat-other-value">{otherStats.averageRating}</span>
                            </div>
                        </div>
                    </div>
                )}
            </div>

            <div className="statistics-cards-main">

                <div className="statistics-left">

                    <div className="genres-countries">

                        {/* TOP GENRES */}
                        <div className="stat-genres-wrapper">
                            <label className="status-breakdown-label">Top 15 Genres</label>
                            <div className="stat-genres-list">
                                {topGenres.map(g => (
                                    <GenreBar key={g.genre} genre={g.genre} count={g.count} maxCount={maxGenre} />
                                ))}
                                <div className="stat-genre-xaxis">
                                    {Array.from({length: 5}, (_, i) => {
                                        const value = Math.round((i * maxGenre) / 4);
                                        return <span key={i}>{value}</span>
                                    })}
                                </div>
                            </div>
                        </div>

                        {/* TOP COUNTRIES */}
                        <div className="stat-chart-countries">
                            <label className="status-breakdown-label">Top 5 Countries You've Watched From</label>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart
                                    data={countriesChartData}
                                    barCategoryGap="30%"
                                    barGap={4}
                                    margin={{top: 10, right: 10, left: -30, bottom: 20}}
                                >
                                    <CartesianGrid vertical={false} stroke="rgba(255,255,255,0.06)" />
                                    <XAxis dataKey="name" tick={{fill: "#ccc", fontSize: 12}} axisLine={false} tickLine={false} interval={0} />
                                    <YAxis tick={{fill: "#ccc", fontSize: 12}} axisLine={false} tickLine={false} allowDecimals={false} />
                                    <Tooltip contentStyle={{background: "#1E1A35", border: "1px solid #3A3560", borderRadius: 8}} labelStyle={{color: "#FFF"}} itemStyle={{color: "#CCC"}} />
                                    <Legend wrapperStyle={{paddingTop: 16, color: "#CCC", fontSize: 13}} />
                                    <Bar dataKey="Movie" radius={[6, 6, 0, 0]} maxBarSize={32} fill="#F7E741">
                                        {countriesChartData.map((_,i) => (
                                            <Cell key={i} fill="url(#gradMovie2)" />
                                        ))}
                                    </Bar>
                                    <Bar dataKey="Series" radius={[6, 6, 0, 0]} maxBarSize={32} fill="#BC1BE6">
                                        {countriesChartData.map((_,i) => (
                                            <Cell key={i} fill="url(#gradSeries2)" />
                                        ))}
                                    </Bar>
                                    <defs>
                                        <linearGradient id="gradMovie2" x1="0%" y1="0%" x2="0" y2="1">
                                            <stop offset="0%" stopColor="#FFD2AC"/>
                                            <stop offset="100%" stopColor="#F7E741"/>
                                        </linearGradient>
                                        <linearGradient id="gradSeries2" x1="0%" y1="0%" x2="0" y2="1">
                                            <stop offset="0%" stopColor="#c977e1"/>
                                            <stop offset="100%" stopColor="#BC1BE6"/>
                                        </linearGradient>
                                    </defs>
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    </div>

                    {/* STATUS BREAKDOWN */}
                    <div className="status-breakdown">
                        <div className="status-breakdown-header">
                            <label className="status-breakdown-label">Movie</label>
                            <label className="status-breakdown-label">TV Series</label>
                        </div>
                        <div className="status-breakdown-rows">
                            {statuses.map((status) => {
                                const data = statusOverview[status.key];
                                if (!data) return null;
                                return (
                                    <div key={status.key} className="status-breakdown-row">
                                        <div className="status-breakdown-count">{data?.movies ?? 0}</div>
                                        <StatusBar count={data?.movies ?? 0} maxCount={maxMovies} side="left"/>
                                        <div className="status-breakdown-name">{status.name}</div>
                                        <StatusBar count={data?.series ?? 0} maxCount={maxSeries} side="right"/>
                                        <div className="status-breakdown-count">{data?.series ?? 0}</div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>

                    <div className="funfacts-toprated">
                        {/* FUN FACTS */}
                        {funFacts && (
                            <div className="stat-funfacts-wrapper">
                                <label className="status-breakdown-label">Did you know that ... ?</label>
                                <ul className="stat-funfacts-list">
                                    {funFacts.longestSeriesEpisodes && (
                                        <li>
                                            <span className="stat-funfacts-icon"><FaAward /></span>
                                            Your longest TV series have {funFacts.longestSeriesEpisodes} episodes.
                                        </li>
                                    )}
                                    {funFacts.longestMovieRuntime && (
                                        <li>
                                            <span className="stat-funfacts-icon"><FaAward /></span>
                                            Your longest move you've watched lasted {funFacts.longestMovieRuntime} minutes.
                                        </li>
                                    )}
                                    {funFacts.uniqueCountriesCount && (
                                        <li>
                                            <span className="stat-funfacts-icon"><FaAward /></span>
                                            You've watched movies and TV series from {funFacts.uniqueCountriesCount} different countries.
                                        </li>
                                    )}
                                </ul>
                            </div>
                        )}
                        {/* TOP RATED */}
                        <div className="stat-top-rated">
                            <label className="status-breakdown-label">Your Highest Rated Movie / TV Series</label>
                            <div className="stat-top-rated-content">
                                <div className="stat-top-rated-label">
                                    Your most loved movie is {topRated?.movie?.title}
                                </div>
                                <StarRating rating={topRated?.movie?.rating} className="stat-top-rated-stars" />
                            </div>
                            <div className="stat-top-rated-content">
                                <div className="stat-top-rated-label">
                                    Your most loved series is {topRated?.series?.title}
                                </div>
                                <StarRating rating={topRated?.series?.rating} className="stat-top-rated-stars" />
                            </div>
                        </div>
                    </div>

                </div>

                {/* SUMMARY + MONTHLY ACTIVE */}
                <div className="statistics-right">

                    {/* SUMMARY */}
                    <div className="summary">
                        <div className="status-breakdown-label">Summary</div>
                        <div className="summary-total">
                            <div className="summary-total-label">Total Entries</div>
                            <div className="status-card-total">{summary.totalEntries}</div>
                        </div>
                        <div className="summary-movie-series">
                            <div className="summary-type">
                                <div className="summary-label">
                                    <img src="/images/type-movie.png" alt="icon" className="summary-icon"/>
                                    <label className="summary-total-label">Movies</label>
                                </div>
                                <div className="summary-count">{summary.movies}</div>
                            </div>
                            <div className="summary-type">
                                <div className="summary-label">
                                    <img src="/images/type-series.png" alt="icon" className="summary-icon"/>
                                    <label className="summary-total-label">TV Series</label>
                                </div>
                                <div className="summary-count">{summary.series}</div>
                            </div>
                        </div>
                        <div className="summary-donut-chart">
                            <DonutChart
                                movies={summary.movies}
                                series={summary.series}
                            />
                        </div>
                    </div>

                    {/* MONTHLY ACTIVE */}
                    <div className="stat-monthly-active">
                        <label className="status-breakdown-label">Monthly Active</label>
                        <div className="stat-monthly-picker">
                            <select
                                className="stat-monthly-select"
                                value={`${selectedYear}-${String(selectedMonth).padStart(2,"0")}`}
                                onChange={(e) => {
                                    const [y, m] = e.target.value.split("-");
                                    onMonthChange(parseInt(y), parseInt(m));
                                }}
                            >
                                {Array.from({length: 12}, (_,i) => {
                                    const d = new Date();
                                    d.setMonth(d.getMonth() - i);
                                    const y = d.getFullYear();
                                    const m = d.getMonth() + 1;
                                    const val = `${y}-${String(m).padStart(2,"0")}`;
                                    return <option key={val} value={val}>{val}</option>;
                                })}
                            </select>
                        </div>
                        <div className="stat-monthly-count">
                            {monthlyActive?.total ?? 0} <span className="stat-monthly-count-label">added movies & TV series</span>
                        </div>
                        {diff !== 0 && (
                            <div className={`stat-monthly-diff ${diff > 0 ? "stat-monthly-diff--up" : "stat-monthly-diff--down"}`}>
                                {diff > 0 ? <FaArrowUp /> : <FaArrowDown />}
                                <span>{Math.abs(diff)} {diff > 0 ? "more" : "less"}</span>
                            </div>
                        )}
                        <div className="stat-monthly-compare">
                            than {prevYear}-{String(prevMonth).padStart(2,"0")}
                        </div>
                        <img src="/images/olaf.png" alt="olaf" className="stat-monthly-olaf" />
                    </div>

                </div>
            </div>
        </div>
    );
};

export default StatisticsPageView;
