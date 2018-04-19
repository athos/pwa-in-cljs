(ns pwa-in-cljs.service-worker
  (:require [clojure.string :as str]
            [kitchen-async.promise :as p]
            [goog.events :as events]))

(enable-console-print!)

(def CACHE_NAME "weatherPWA-final-1")
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

(def DATA_CACHE_NAME "weatherData-v1")
(def data-url "https://query.yahooapis.com/v1/public/yql")

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
                     (when-not (#{CACHE_NAME DATA_CACHE_NAME} key)
                       (println "[ServiceWorker] Removing old cache" key)
                       (.delete js/caches key))))
              p/all))
       (.waitUntil event)))

(.addEventListener js/self "activate" activate)

(defn fetch [event]
  (let [req (.-request event), url (.-url req)]
    (println "[ServiceWorker] Fetch" url)
    (->> (if (str/starts-with? url data-url)
           (p/let [cache (js/caches.open DATA_CACHE_NAME)
                   res (js/fetch req)]
             (do (.put cache url (.clone res))
                 res))
           (p/let [res (js/caches.match req)]
             (or res (js/fetch req))))
         (.respondWith event))))

(.addEventListener js/self "fetch" fetch)
