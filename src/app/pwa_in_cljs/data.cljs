(ns pwa-in-cljs.data
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:loading? true
           :visible-cards {}
           :selected-cities []}))
