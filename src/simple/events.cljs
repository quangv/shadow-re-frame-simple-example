(ns simple.events
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

