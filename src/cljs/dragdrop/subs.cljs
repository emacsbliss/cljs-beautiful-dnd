(ns dragdrop.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::columns
 (fn [db]
   (:columns db)))

(re-frame/reg-sub
 ::tasks
 (fn [db]
   (:tasks db)))


(re-frame/reg-sub
 ::column-order
 (fn [db]
   (:column-order db)))
