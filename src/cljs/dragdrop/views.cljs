(ns dragdrop.views
  (:require
   [re-frame.core :as re-frame]
   [dragdrop.subs :as subs]
   [dragdrop.column :refer [Column]]
   ["react-beautiful-dnd" :refer [DragDropContext]]
   ))

(defonce initial-data
  {
   :tasks
   {
    :task-1 {:id "task-1" :content "take out garbage"}
    :task-2 {:id "task-2" :content "watch my favortiles"}
    :task-3 {:id "task-3" :content "change my phone"}
    :task-4 {:id "task-4" :content "cook dinner"}
   }
   :columns
   {
    :column-1
    {
     :id "column-1"
     :title "todo"
     :taskIds [:task-1 :task-2 :task-3 :task-4]
     }

    }
   :column-order [:column-1]
   }
)

(defn main []

  [:> DragDropContext
   {:onDragEnd #()}

  ;; [:div
  (map (fn [col-id]
         (println "col-id: " col-id)
         (let [column (get (:columns initial-data) col-id)
               task-ids (:taskIds column)
               tasks (map #((keyword %) (:tasks initial-data)) task-ids)]

           [Column {:key (:id column)
                    :column column
                    :tasks tasks}]
         ))
       (:column-order initial-data)
   )
   ;; ]
 ]
)

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [main]
     ]
))
