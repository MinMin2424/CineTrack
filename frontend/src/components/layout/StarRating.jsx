/*
 * Created by minmin_tranova on 26.03.2026
 */

import {FaRegStar, FaStar} from "react-icons/fa";
import React from "react";

const StarRating = ({rating}) => {
    const num = parseFloat(rating);
    const isValid = !isNaN(num) && num > 0;
    const filled = isValid ? Math.round(num / 2) : 0;
    return (
        <div className="ep-stars">
            {Array.from({length: 5}, (_, i) => (
                i < filled
                    ? <FaStar key={i} className="ep-star ep-star--filled" />
                    : <FaRegStar key={i} className="ep-star ep-star--empty" />
            ))}
            <span className="ep-star-value">({num}/10)</span>
        </div>
    );
};

export default StarRating;