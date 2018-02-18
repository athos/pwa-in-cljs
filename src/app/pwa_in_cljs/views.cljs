(ns pwa-in-cljs.views
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [pwa-in-cljs.data :as data]
            [reagent.core :as r]))

(defn header []
  [:header.header
   [:h1.header__title "Weather PWA"]
   [:button#butRefresh.headerButton {:aria-label "Refresh"}]
   [:button#butAdd.headerButton {:aria-label "Add"}]])

(defn current []
  [:div.current
   [:div.visual
    [:div.icon]
    [:div.temperature
     [:span.value]
     [:span.scale "°F"]]]
   [:div.description
    [:div.humidity]
    [:div.wind
     [:span.value]
     [:span.scale "mph"]
     [:span.direction]]
    [:div.sunrise]
    [:div.sunset]]])

(defn oneday []
  [:div.oneday
   [:div.date]
   [:div.icon]
   [:div.temp-high
    [:span.value]]
   [:div.temp-low
    [:span.value]]])

(defn future []
  [:div.future
   (for [i (range 7)]
     ^{:key i}
     [oneday])])

(defn main []
  [:main.main
   [:div.card.cardTemplate.weather-forecast {:hidden true}
    [:div.city-key {:hidden true}]
    [:div.card-last-updated {:hidden true}]
    [:div.location]
    [:div.date]
    [:div.description]
    [current]
    [future]]])

(defn dialog-container []
  [:div.dialog-container
   [:div.dialog
    [:div.dialog-title "Add new city"]
    [:div.dialog-body
     [:select#selectCityToAdd
      [:option {:value "2357536"} "Austin, TX"]
      [:option {:value "2367105"} "Boston, MA"]
      [:option {:value "2379574"} "Chicago, IL"]
      [:option {:value "2459115"} "New York, NY"]
      [:option {:value "2475687"} "Portland, OR"]
      [:option {:value "2487956"} "San Francisco, CA"]
      [:option {:value "2490383"} "Seattle, WA"]]]
    [:div.dialog-buttons
     [:button#butAddCity.button "Add"]
     [:button#butAddCancel.button "Cancel"]]]])

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
