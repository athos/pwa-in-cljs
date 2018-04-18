(ns pwa-in-cljs.handler
  (:require [cljs.reader :as reader]
            [kitchen-async.promise :as p]
            [pwa-in-cljs.api :as api]
            [pwa-in-cljs.data :as data]))

(defn toggle-add-dialog [shown?]
  (swap! data/app-state assoc :dialog-shown? shown?))

(defn save-selected-cities []
  (let [{:keys [selected-cities]} @data/app-state
        serialized (pr-str selected-cities)]
    (js/localStorage.setItem "selected-cities" serialized)))

(defn load-selected-cities []
  ;; TODO: should not use localStorage
  (p/promise [resolve]
    (let [serialized (js/localStorage.getItem "selected-cities")]
      (resolve (reader/read-string serialized)))))

(defn update-forecast-card [key card]
  (swap! data/app-state assoc-in [:visible-cards key] card))

(defn fetch-forecast [key]
  (let [label (data/cities key)]
    (p/let [forecast (api/fetch-forecast key label)]
      (update-forecast-card key forecast))))

(defn add-city [key]
  (let [{:keys [visible-cards]} @data/app-state]
    (p/do (fetch-forecast key)
          (when-not (contains? visible-cards key)
            (swap! data/app-state update :selected-cities conj key)
            (save-selected-cities))
          (toggle-add-dialog false))))

(defn update-selected-cities [cities]
  (swap! data/app-state assoc :selected-cities (vec cities)))

(defn update-forecasts []
  (let [{:keys [selected-cities]} @data/app-state]
    (->> selected-cities
         (map fetch-forecast)
         dorun)))
