/*
 * Created by minmin_tranova on 19.02.2026
 * Protected route component that restricts access to authenticated users only.
 */

import React from 'react';
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

const ProtectedRoute = () => {
    const { isAuthenticated, loading } = useAuth();
    if (loading) {
        return (
            <div>
                Loading...
            </div>
        );
    }
    return isAuthenticated ? <Outlet /> : <Navigate to="/auth/login" replace />;
};

export default ProtectedRoute;