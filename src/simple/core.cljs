(ns simple.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [simple.routes :as routes]
            ))

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

(rf/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))


;; -- Domino 5 - View Functions

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

;; home

(defn home-panel []
  [:div (str "This is the Home Page.")
   [:div [:a {:href (routes/url-for :about)} "go to About Page"]]])
 
;; about

(defn about-panel []
  [:div "This is the About Page."
   [:div [:a {:href (routes/url-for :home)} "go to Home Page"]]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (rf/subscribe [::active-panel])]
    [show-panel @active-panel]))

(defn ui []
  [:div
   [:h1 "Hello world, it is now"]
   [clock]
   [color-input]
   [main-panel]])

(defn render []
  (reagent/render [ui]
                  (js/document.getElementById "app")))

(defn ^:export init []
  (js/console.log "init")
  (routes/app-routes)
  (rf/dispatch-sync [:initialize])
  (render))
