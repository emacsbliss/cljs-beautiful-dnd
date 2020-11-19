(ns dragdrop.column
  (:require
   [reagent.core :as ra]
   [stylefy.core :refer [use-style]]
   [dragdrop.task :refer [task]]
   ["react-beautiful-dnd" :refer [Droppable]]
   )
)

(def container-style
  {:margin "8px"
   :border "1px solid lightgrey"
   :border-radius "2px"
   })

(def title-style
  {:padding "8px"}
)

(defn task-list-style [props]
  {:padding "8px"
   :transition "background-color 0.2s ease"
   :background-color (if (:is-dragging-over props) "skyblue" "white")
   }
)

(defn- container [& child]
  [:div (use-style container-style)
    child])

(defn- title [text]
  [:h3 (use-style title-style) text]
)

(defn- task-list [props tasks placeholder]
  (let [style (task-list-style props)
        new-props (dissoc props :is-dragging-over)]
  [:div (use-style style new-props)
   (map-indexed #(task {:key (:id %2)
                 :tsk %2 :index %1})
        tasks)

   placeholder]
))

(defn Column
  [{:keys [key column tasks]}]

   [container
  ^{:key "title"} [title (:title column)]
  ^{:key "task-list"}
    [:> Droppable
        {:droppableId key}

     (fn [provided snapshot]
       (let [inner-ref (.-innerRef provided)
             droppable-props (-> (.-droppableProps provided)
                                (js->clj :keywordize-keys true))
             placeholder (.-placeholder provided)
             is-dragging-over (.-isDraggingOver snapshot)]

       (ra/as-element
        [task-list (merge droppable-props {:ref inner-ref
                                           :is-dragging-over is-dragging-over})
         tasks placeholder])
       ))
     ]
  ]
)
