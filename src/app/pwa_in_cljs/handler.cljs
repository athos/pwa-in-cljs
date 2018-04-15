(ns pwa-in-cljs.handler
  (:require [pwa-in-cljs.data :as data]))

(defn toggle-add-dialog [shown?]
  (swap! data/app-state assoc :dialog-shown? shown?))
