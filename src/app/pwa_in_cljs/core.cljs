(ns pwa-in-cljs.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [pwa-in-cljs.views :as views]
            [reagent.core :as r]))

(enable-console-print!)

(defn mount []
  (println "mounting ...")
  (r/render [views/app] (dom/getElement "app")))

(events/listen js/window "load" mount)

(defn on-js-reload []
  (mount))
