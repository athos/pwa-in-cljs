(ns pwa-in-cljs.views
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [pwa-in-cljs.data :as data]
            [pwa-in-cljs.handler :as handler]
            [reagent.core :as r]))

(defn icon-class [code]
  (case (js/parseInt code)
    (25 32 33 34 36 3200) "clear-day"
    (0 1 2 6 8 9 10 11 12 17 35 40) "rain"
    (3 4 37 38 39 45 47) "thunderstorms"
    (5 7 13 14 16 18 41 42 43 46) "snow"
    (15 19 20 21 22) "fog"
    (24 23) "windy"
    (26 27 28 31) "cloudy"
    (29 30 44) "partly-cloud-day"))

(defn header []
  [:header.header
   [:h1.header__title "Weather PWA"]
   [:button#butRefresh.headerButton {:aria-label "Refresh"}]
   [:button#butAdd.headerButton
    {:aria-label "Add" :on-click #(handler/toggle-add-dialog true)}]])

(defn current-weather [card current]
  (let [sunrise (get-in card [:channel :astronomy :sunrise])
        sunset (get-in card [:channel :astronomy :sunset])
        humidity (get-in card [:channel :atmosphere :humidity])
        wind (get-in card [:channel :wind])]
    [:div.current
     [:div.visual
      [:div.icon {:class (icon-class (:code current))}]
      [:div.temperature
       [:span.value (js/Math.round (:temp current))]
       [:span.scale "°F"]]]
     [:div.description
      [:div.humidity (str (js/Math.round humidity) "%")]
      [:div.wind
       [:span.value (js/Math.round (:speed wind))]
       [:span.scale "mph"]
       [:span.direction (js/Math.round (:direction wind))]]
      [:div.sunrise sunrise]
      [:div.sunset sunset]]]))

(defn oneday [daily day]
  [:div.oneday
   [:div.date (nth data/days-of-week day)]
   [:div.icon {:class (icon-class (:code daily))}]
   [:div.temp-high
    [:span.value (js/Math.round (:high daily))]]
   [:div.temp-low
    [:span.value (js/Math.round (:low daily))]]])

(defn future-weather [forecast]
  (let [today (.getDay (js/Date.))]
    [:div.future
     (for [i (range 7)
           :let [day (rem (+ i today) 7)]]
       ^{:key i}
       [oneday (nth forecast i) day])]))

(defn main []
  (let [{:keys [visible-cards]} @data/app-state]
    [:main.main
     (for [[_ card] visible-cards
           :let [current (get-in card [:channel :item :condition])
                 forecast (get-in card [:channel :item :forecast])]]
       ^{:key (:key card)}
       [:div.card.weather-forecast
        [:div.city-key (:key card)]
        [:div.card-last-updated (:created card)]
        [:div.location (:label card)]
        [:div.date (:date current)]
        [:div.description (:description current)]
        [current-weather card current]
        [future-weather forecast]])]))

(defn dialog-container []
  (let [selected-city (r/atom nil)
        {:keys [dialog-shown?]} @data/app-state]
    (when dialog-shown?
      [:div.dialog-container
       [:div.dialog
        [:div.dialog-title "Add new city"]
        [:div.dialog-body
         [:select#selectCityToAdd
          {:on-change (fn [e]
                        (let [key (.. e -target -value)]
                          (reset! selected-city key)))}
          (for [[id city] data/cities]
            ^{:key id}
            [:option {:value id} city])]]
        [:div.dialog-buttons
         [:button#butAddCity.button
          {:on-click #(handler/add-city @selected-city)}
          "Add"]
         [:button#butAddCancel.button
          {:on-click #(handler/toggle-add-dialog false)}
          "Cancel"]]]])))

(defn loader []
  (let [{:keys [loading?]} @data/app-state]
    (when loading?
      [:div.loader
       [:svg {:view-box "0 0 32 32" :width 32 :height 32}
        [:circle#spinner {:cx 16 :cy 16 :r 14 :fill :none}]]])))

(defn app []
  [:div
   [header]
   [main]
   [dialog-container]
   [loader]])
