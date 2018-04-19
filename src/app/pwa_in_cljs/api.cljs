(ns pwa-in-cljs.api
  (:require [kitchen-async.promise :as p]))

(defn fetch-forecast [key label]
  (let [statement (str "select * from weather.forecast where woeid=" key)
        url (str "https://query.yahooapis.com/v1/public/yql?format=json&q=" statement)]
    (p/let [res (if (exists? (.-caches js/window))
                  (p/let [res (js/caches.match url)]
                    (or res (js/fetch url)))
                  (js/fetch url))
            json (.json res)]
      (let [results (-> (.. json -query -results)
                        (js->clj :keywordize-keys true))]
        (assoc results
               :key key
               :label label
               :created (.. json -query -created))))))
