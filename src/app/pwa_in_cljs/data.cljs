(ns pwa-in-cljs.data
  (:require [reagent.core :as r]))

(defonce app-state
  (r/atom {:loading? true
           :dialog-shown? false
           :visible-cards {}
           :selected-cities []}))

(def days-of-week
  ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"])

(def cities
  {"2357536" "Austin, TX"
   "2367105" "Boston, MA"
   "2379574" "Chicago, IL"
   "2459115" "New York, NY"
   "2475687" "Portland, OR"
   "2487956" "San Francisco, CA"
   "2490383" "Seattle, WA"})
