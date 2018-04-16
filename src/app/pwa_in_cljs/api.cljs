(ns pwa-in-cljs.api
  (:require [kitchen-async.promise :as p]))

(defn fetch-forecast [key label]
  (let [statement (str "select * from weather.forecast where woeid=" key)
        url (str "https://query.yahooapis.com/v1/public/yql?format=json&q=" statement)]
    (p/let [json (p/-> (js/fetch url) .json)]
      (let [results (-> (.. json -query -results)
                        (js->clj :keywordize-keys true))]
        (assoc results
               :key key
               :label label
               :created (.. json -query -created))))))
