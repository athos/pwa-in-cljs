(ns pwa-in-cljs.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [kitchen-async.promise :as p]
            [pwa-in-cljs.handler :as handler]
            [pwa-in-cljs.views :as views]
            [reagent.core :as r]))

(enable-console-print!)

(defn main []
  (println "mounting ...")
  (r/render [views/app] (dom/getElement "app"))
  (p/let [cities (handler/load-selected-cities)]
    (handler/update-selected-cities cities)
    (handler/update-forecasts)))

(events/listen js/window "load" main)

(defn on-js-reload []
  (main))
