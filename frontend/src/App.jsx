import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import HomePage from "./pages/HomePage";
import MainLayout from "./pages/MainLayout";

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
              <Route element={<MainLayout />} />
                <Route path="/" element={<HomePage />} />
                <Route path="/statistics" element={<StatisticsPage />} />
                <Route path="/watchlist" element={<WatchlistPage />} />
                <Route path="/discovery" element={<DiscoveryPage />} />
                <Route path="/settings" element={<SettingsPage />} />
            </Route>

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}

export default App;

// import logo from './logo.svg';
// import './App.css';
//
// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }
//
// export default App;
