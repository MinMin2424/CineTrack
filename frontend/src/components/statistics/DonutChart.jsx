/*
 * Created by minmin_tranova on 25.03.2026
 */

import "../../styles/components/statistics/DonutChartStyle.css"

const DonutChart = ({
    movies,
    series,
}) => {
    const total = movies + series;
    if (total === 0) return null;

    const r = 70;
    const stroke = 22;
    const cx = 100; const cy = 100;
    const circumference = 2 * Math.PI * r;
    const movieFraction = movies / total;
    const seriesFraction = series / total;
    const movieDash = movieFraction * circumference;
    const seriesDash = seriesFraction * circumference;

    return (
        <svg viewBox="0 0 200 200" className="stat-donut-svg">
            <defs>
                <linearGradient id="gradMovie" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="#F7E741"/>
                    <stop offset="100%" stopColor="#F7E741"/>
                </linearGradient>
                <linearGradient id="gradSeries" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="#BC1BE6"/>
                    <stop offset="100%" stopColor="#BC1BE6"/>
                </linearGradient>
            </defs>
            <circle cx={cx} cy={cy} r={r} fill="none" stroke="#1E1A35" strokeWidth={stroke} />
            <circle
                cx={cx} cy={cy} r={r} fill="none"
                stroke="url(#gradMovie)" strokeWidth={stroke}
                strokeDasharray={`${movieDash} ${circumference - movieDash}`}
                strokeDashoffset={circumference / 4}
                strokeLinecap="butt"
                style={{transition: "stroke-dasharray 0.6s ease"}}
            />
            <circle
                cx={cx} cy={cy} r={r} fill="none"
                stroke="url(#gradSeries)" strokeWidth={stroke}
                strokeDasharray={`${seriesDash} ${circumference - seriesDash}`}
                strokeDashoffset={circumference / 4 - movieDash}
                strokeLinecap="butt"
                style={{transition: "stroke-dasharray 0.6s ease"}}
            />
        </svg>
    );
};

export default DonutChart;