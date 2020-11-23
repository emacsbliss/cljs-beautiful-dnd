(ns dragdrop.views
  (:require
   [re-frame.core :as rf]
   [dragdrop.subs :as subs]
   [goog.string :as gstr]
   [dragdrop.events :as events]
   [dragdrop.column :refer [Column]]
   ["react-beautiful-dnd" :refer [DragDropContext]]

   [clojure.string :as str]))

(defn on-drag-start []
  (set! (.. js/document -body -style -color) "orange")
  (set! (.. js/document -body -style -transition) "background-color 0.2s ease")
)

(defn on-drag-update [tasks update]
  (let [{:keys [destination]} (js->clj update :keywordize-keys true)
        opacity (if destination
                  (/ (:index destination) (count tasks))
                  0)]
    (set! (.. js/document -body -style -backgroundColor) (gstr/format "rgba(153,141,217," opacity ")"))
))

(defn on-drag-end [result]
 (let [columns (rf/subscribe [::subs/columns])
       {:keys [destination source draggableId]}
        (js->clj result :keywordize-keys true)
        src-droppable-id (:droppableId source)
        dest-droppable-id (:droppableId destination)
        src-index (:index source)
        dest-index (:index destination)]
    (set! (.. js/document -body -style -color) "inherit")
    (when (and destination
               (or (not= src-droppable-id dest-droppable-id)
                   (not= src-index dest-index)))
      (let [column ((keyword src-droppable-id) @columns)]
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
     {:onDragEnd on-drag-end
      :onDragUpdate #(on-drag-update db-tasks %)
      :onDragStart on-drag-start
     }

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
