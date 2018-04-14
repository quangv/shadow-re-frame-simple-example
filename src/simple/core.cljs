(ns simple.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [clojure.string :as str]))

;; -- Domino 1 Event Dispatch

(defn dispatch-timer-event []
  (let [now (js/Date.)]
    (rf/dispatch [:timer now])))

(defonce do-timer (js/setInterval dispatch-timer-event 1000))

;; -- Domino 2 - Event Handlers

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:time (js/Date.)
    :time-color "#f88"}))

(rf/reg-event-db
 :time-color-change
 (fn [db [_ new-color-value]]
   (assoc db :time-color new-color-value)))

(rf/reg-event-db
 :timer
 (fn [db [_ new-time]]
   (assoc db :time new-time)))


;; -- Domino 4 - Query

(rf/reg-sub
 :time
 (fn [db _]
   (:time db)))

(rf/reg-sub
 :time-color
 (fn [db _]
   (:time-color db)))


;; -- Domino 5 - View FUnctions

(defn clock []
  [:div.example-clock
   {:style {:color @(rf/subscribe [:time-color])}}
   (-> @(rf/subscribe [:time])
       .toTimeString
       (str/split " ")
       first)])

(defn color-input []
  [:div.color-input
   "Time color: "
   [:input {:type "text"
            :value @(rf/subscribe [:time-color])
            :on-change #(rf/dispatch [:time-color-change (-> % .-target .-value)])}]])

(defn ui []
  [:div
   [:h1 "Hello world, it is now"]
   [clock]
   [color-input]])

(defn render []
  (reagent/render [ui]
                  (js/document.getElementById "app")))

(defn ^:export init []
  (js/console.log "init")
  (rf/dispatch-sync [:initialize])
  (render))
