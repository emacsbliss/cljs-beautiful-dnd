(ns dragdrop.events
  (:require
   [re-frame.core :as re-frame]
   [dragdrop.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


(defn swap [v i1 i2]
   (assoc v i2 (v i1) i1 (v i2)))

(re-frame/reg-event-db
 ::swap-task
 (fn [db [_ col-id src-index dest-index]]
   (let [column ((keyword col-id) (:columns db))
         taskIds (:taskIds column)]

     (assoc-in db [:columns (keyword col-id) :taskIds]
            (swap taskIds src-index dest-index))
   )
))
