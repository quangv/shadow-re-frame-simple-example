;; authored by J. PABLO FERNÁNDEZ - https://pupeno.com/2015/08/26/no-hashes-bidirectional-routing-in-re-frame-with-bidi-and-pushy/

(ns simple.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [simple.events :as events]))

(def routes ["/" {""      :home
                  "about" :about}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route)) "-panel"))]
    (re-frame/dispatch [::events/set-active-panel panel-name])))

(defn app-routes[]
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial bidi/path-for routes))
