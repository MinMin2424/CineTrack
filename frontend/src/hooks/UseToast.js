/*
 * Created by minmin_tranova on 18.03.2026
 */

import {useState, useCallback} from 'react';

export const useToast = () => {
    const [toast, setToast] = useState(null);

    const showToast = useCallback((message) => {
        setToast({message, id: Date.now()});
    }, []);

    const hideToast = useCallback(() => {
        setToast(null);
    }, []);

    return {toast, showToast, hideToast};
};