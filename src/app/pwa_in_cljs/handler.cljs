(ns pwa-in-cljs.handler
  (:require [kitchen-async.promise :as p]
            [pwa-in-cljs.api :as api]
            [pwa-in-cljs.data :as data]))

(defn toggle-add-dialog [shown?]
  (swap! data/app-state assoc :dialog-shown? shown?))
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
            (swap! data/app-state update :selected-cities conj key))
          (toggle-add-dialog false))))
