/*
 * Created by minmin_tranova on 25.02.2026
 */

import React, { useState, useEffect, useRef } from "react";
import { searchMedia } from "../../api/MediaApi";
import AddMovieForm from "./AddMovieForm";
import AddSeriesForm from "./AddSeriesForm";
import { IoClose } from "react-icons/io5";
import "../../styles/components/addMediaForms/AddMediaFormStyle.css"

const AddMediaModal = ({ onClose, onMediaAdded }) => {
    const [mediaType, setMediaType] = useState("movie");
    const [titleQuery, setTitleQuery] = useState("");
    const [searchResult, setSearchResult] = useState(null);
    const [searching, setSearching] = useState(false);
    const [searchError, setSearchError] = useState(null);
    const [showDropdown, setShowDropdown] = useState(false);
    const [selectedMedia, setSelectedMedia] = useState(null);
    const [manualMode, setManualMode] = useState(false);
    const debounceTimer = useRef(null);

    useEffect(() => {
        clearTimeout(debounceTimer.current);
        setSearchResult(null);
        setShowDropdown(false);
        setSearchError(null);

        if (titleQuery.trim().length < 2) return;

        debounceTimer.current = setTimeout(async () => {
            setSearching(true);
            try {
                const result = await searchMedia(titleQuery.trim(), mediaType);
                setSearchResult(result);
                setShowDropdown(true);
            } catch (error) {
                // nothing is found - add manually
                setSearchResult(null);
                setShowDropdown(true);
            } finally {
                setSearching(false);
            }
        }, 500);
        return () => clearTimeout(debounceTimer.current);
    }, [titleQuery, mediaType]);

    const handleTypeChange = (type) => {
        setMediaType(type);
        setTitleQuery("");
        setSearchResult(null);
        setShowDropdown(false);
        setSelectedMedia(null);
        setManualMode(false);
    };

    const handleSelectResult = (result) => {
        setSelectedMedia(result);
        setShowDropdown(false);
        setManualMode(false);
    };

    const handleManual = () => {
        setSelectedMedia(null);
        setManualMode(true);
        setShowDropdown(false);
    };

    const handleSuccess = () => {
        onMediaAdded();
        onClose();
    };

    if (selectedMedia || manualMode) {
        if (mediaType === "movie") {
            return (
                <AddMovieForm
                    omdbData={selectedMedia}
                    onSuccess={handleSuccess}
                    onBack={() => { setSelectedMedia(null); setManualMode(false); }}
                    onClose={onClose}
                />
            )
        } else {
            return (
                <AddSeriesForm
                    omdbData={selectedMedia}
                    onSuccess={handleSuccess}
                    onBack={() => { setSelectedMedia(null); setManualMode(false); }}
                    onClose={onClose}
                />
            )
        }
    }

    return (
      <div className="modal-overlay" onClick={onClose}>
          <div className="modal-container" onClick={e => e.stopPropagation()}>

              {/* HEADER */}
              <div className="modal-header">
                  <h2 className="modal-title">Add New Media</h2>
                  <button className="modal-close-btn" onClick={onClose}>
                    <IoClose />
                  </button>
              </div>

              <div className="modal-body">

                  {/* MEDIA TYPE */}
                  <label className="modal-label">Media Type</label>
                  <div className="modal-type-toggle">
                      <button
                          className={`modal-type-btn ${mediaType === "movie" ? "active" : ""}`}
                          onClick={() => handleTypeChange("movie")}
                      >
                          Movie
                      </button>
                      <button
                          className={`modal-type-btn ${mediaType === "series" ? "active" : ""}`}
                          onClick={() => handleTypeChange("series")}
                      >
                          TV Series
                      </button>
                  </div>

                  {/* TITLE SEARCH INPUT */}
                  <label className="modal-label">Title *</label>
                  <div className="modal-search-wrapper">
                      <input
                          type="text"
                          className="modal-input"
                          placeholder={mediaType === "movie" ? "Enter movie title" : "Enter TV series title"}
                          value={titleQuery}
                          onChange={e => setTitleQuery(e.target.value)}
                          autoFocus
                      />

                      {/* DROPDOWN */}
                      {showDropdown && (
                          <ul className="modal-search-dropdown">
                              {mediaType === "movie" && searchResult && (
                                  <li
                                      className="modal-search-item"
                                      onClick={() => handleSelectResult(searchResult)}
                                  >
                                      {searchResult.Title}
                                  </li>
                              )}
                              {mediaType === "series" && searchResult?.availableSeasons?.map(season => (
                                  <li
                                      key={season.season}
                                      className="modal-search-item"
                                      onClick={() => handleSelectResult({
                                          ...searchResult.seriesInfo,
                                          selectedSeason: season.season,
                                      })}
                                  >
                                      {searchResult.seriesInfo.Title} Season {season.season}
                                  </li>
                              ))}
                              <li className="modal-search-item modal-search-manual">
                                  {mediaType === "movie" ? "Add movie manually" : "Add TV series manually"}
                              </li>
                          </ul>
                      )}
                      {searching && <p className="modal-searching-text">Searching...</p>}
                  </div>
              </div>

              {/* FOOTER */}
              <div className="modal-footer">
                <button
                    className="modal-continue-btn"
                    disabled={!selectedMedia && !manualMode}
                    onClick={() => {}}
                >
                    Continue
                </button>
              </div>

          </div>
      </div>
    );
};

export default AddMediaModal;