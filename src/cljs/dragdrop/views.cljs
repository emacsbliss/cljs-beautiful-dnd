(ns dragdrop.views
  (:require
   [re-frame.core :as rf]
   [dragdrop.subs :as subs]
   [dragdrop.events :as events]
   [dragdrop.column :refer [Column]]
   ["react-beautiful-dnd" :refer [DragDropContext]]
   ))

(defn on-drag-end [result]
 (let [columns (rf/subscribe [::subs/columns])
  ;; (let [{:keys [destination source draggableId]}
       {:keys [destination source draggableId]}
        (js->clj result :keywordize-keys true)
        src-droppable-id (:droppableId source)
        dest-droppable-id (:droppableId destination)
        src-index (:index source)
        dest-index (:index destination)]
    (println "destination: " destination)
    (println "dest-droppable-id: " dest-droppable-id)
    (println "src-droppable-id: " src-droppable-id)
    (println "src-index: " src-index)
    (println "dest-index: " dest-index)
    (when (and destination
               (or (not= src-droppable-id dest-droppable-id)
                   (not= src-index dest-index)))
      (println "let us do some work")
      (let [column ((keyword src-droppable-id) @columns)]
        (println "column here: " column)
        (rf/dispatch [::events/swap-task
                      src-droppable-id src-index dest-index])
      )
    )
  )
)
  ;; result is an object with following important properties
  ;; draggableId
  ;; source
  ;; destination
  ;;  {:draggableId task-4 :type DEFAULT
  ;;  :source
  ;;  {:index 3 :droppableId column-1}
  ;;  :destination {:droppableId column-1 :index 1}
  ;;  :combine nil
  ;;  :reason DROP :mode FLUID}

(defn main []
  (let [columns @(rf/subscribe [::subs/columns])
        db-tasks @(rf/subscribe [::subs/tasks])
        col-order @(rf/subscribe [::subs/column-order])]

    [:> DragDropContext
    {:onDragEnd on-drag-end}

      (map (fn [col-id]
            (println "col-id: " col-id)
            (let [column (get columns col-id)
                task-ids (:taskIds column)
                tasks (map #((keyword %) db-tasks) task-ids)]

            [Column {:key (:id column)
                        :column column
                        :tasks tasks}]))

           col-order
      )])
)

(defn main-panel []
  [:div [main]]
)
