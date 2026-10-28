/*
 * Created by minmin_tranova on 28.10.2026
 */

const CONTENT_ID = 'app-content';

export const getContentEl = () => document.getElementById(CONTENT_ID);

export const saveScroll = (key = 'homeScrollY') => {
    const el = getContentEl();
    sessionStorage.setItem(key, String(el ? el.scrollTop : 0));
};

export const restoreScroll = (key = 'homeScrollY') => {
    const raw = sessionStorage.getItem(key);
    if (raw === null) return false;
    const el = getContentEl();
    if (el) el.scrollTop = parseInt(raw, 10);
    sessionStorage.removeItem(key);
    return true;
};

export const resetContentScroll = () => {
    const el = getContentEl();
    if (el) el.scrollTop = 0;
};