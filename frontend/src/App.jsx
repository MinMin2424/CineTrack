import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import MainLayout from "./pages/MainLayout";
import HomePageContainer from "./pages/home/HomePageContainer";

const StatisticsPage = () => <div>Statistics - TODO</div>
const WatchlistPage = () => <div>Watchlist - TODO</div>
const DiscoveryPage = () => <div>Discovery - TODO</div>
const SettingsPage = () => <div>Settings - TODO</div>

function App() {
  return (
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/auth/login" element={<LoginPage />} />
            <Route path="/auth/register" element={<RegisterPage />} />

            <Route element={<ProtectedRoute />} >
              <Route element={<MainLayout />} >
                <Route path="/" element={<HomePageContainer />} />
                <Route path="/statistics" element={<StatisticsPage />} />
                <Route path="/watchlist" element={<WatchlistPage />} />
                <Route path="/discovery" element={<DiscoveryPage />} />
                <Route path="/settings" element={<SettingsPage />} />
              </Route>
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}

export default App;