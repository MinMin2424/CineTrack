import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import MainLayout from "./pages/MainLayout";
import HomePageContainer from "./pages/home/HomePageContainer";
import MovieDetailContainer from "./pages/movie/MovieDetailContainer";
import SeriesDetailContainer from "./pages/series/SeriesDetailContainer";
import EpisodeDetailContainer from "./pages/episode/EpisodeDetailContainer";
import WatchlistPageContainer from "./pages/watchlist/WatchlistPageContainer";
import DiscoveryPageContainer from "./pages/discovery/DiscoveryPageContainer";

const StatisticsPage = () => <div>Statistics - TODO</div>
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
                <Route path="/movies/:movieId" element={<MovieDetailContainer />} />
                <Route path="/series/:seriesId" element={<SeriesDetailContainer />} />
                <Route path="/series/:seriesId/episodes/:episodeNumber" element={<EpisodeDetailContainer />} />
                <Route path="/statistics" element={<StatisticsPage />} />
                <Route path="/watchlist" element={<WatchlistPageContainer />} />
                <Route path="/discovery" element={<DiscoveryPageContainer />} />
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