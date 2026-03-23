/*
 * Created by minmin_tranova on 18.03.2026
 */

import React, {useEffect, useState} from "react";
import "../../styles/components/layout/ToastStyle.css"

const Toast = ({
    message,
    duration = 5000,
    onClose
}) => {
    const [visible, setVisible] = useState(true);
    useEffect(() => {
        const timer = setTimeout(() => {
            setVisible(false);
            setTimeout(onClose, 300);
        }, duration);
        return () => clearTimeout(timer);
    }, [duration, onClose]);

    return (
        <div className={`toast ${visible ? "toast--visible" : "toast--hidden"}`}>
            {message}
        </div>
    );
};

export default Toast;