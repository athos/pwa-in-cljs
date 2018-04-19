(ns pwa-in-cljs.service-worker
  (:require [kitchen-async.promise :as p]
            [goog.events :as events]))

(enable-console-print!)

(def CACHE_NAME "weatherPWA-step-5-2")
(def files-to-cache
  #js ["/"
       "/index.html"
       "/js/compiled/app.js"
       "/css/inline.css"
       "/images/clear.png"
       "/images/cloudy-scattered-showers.png"
       "/images/cloudy.png"
       "/images/fog.png"
       "/images/ic_add_white_24px.svg"
       "/images/ic_refresh_white_24px.svg"
       "/images/partly-cloudy.png"
       "/images/rain.png"
       "/images/scattered-showers.png"
       "/images/sleet.png"
       "/images/snow.png"
       "/images/thunderstorm.png"
       "/images/wind.png"])

(defn install [event]
  (println "[ServiceWorker] Install")
  (->> (p/let [cache (js/caches.open CACHE_NAME)]
         (println "[ServiceWorker] Caching app shell")
         (.addAll cache files-to-cache))
       (.waitUntil event)))

(.addEventListener js/self "install" install)

(defn activate [event]
  (println "[ServiceWorker] Activate")
  (->> (p/let [keys (js/caches.keys)]
         (->> keys
              (map (fn [key]
                     (when-not (= key CACHE_NAME)
                       (println "[ServiceWorker] Removing old cache" key)
                       (.delete js/caches key))))
              p/all))
       (.waitUntil event)))

(.addEventListener js/self "activate" activate)

(defn fetch [event]
  (println "[ServiceWorker] Fetch" (.. event -request -url))
  (->> (p/let [res (js/caches.match (.-request event))]
         (or res (js/fetch (.-request event))))
       (.respondWith event)))

(.addEventListener js/self "fetch" fetch)
