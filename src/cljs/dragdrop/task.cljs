(ns dragdrop.task
  (:require
   [reagent.core :as ra]
   ["react-beautiful-dnd" :refer [Draggable]]
   [stylefy.core :refer [use-style]])
)

(defn container-style [state]
  {:margin-bottom "8px"
   :border "1px solid lightgrey"
   :border-radius "2px"
   :padding "8px"
   :background-color (if (:is-dragging state) "lightgreen" "white")
})

(defn- container [props & child]
  ;; NOTE: have to separate style with other things in pros
  ;; otherwise will get error:
  ;; Assert failed: HTML attribute :style is not supported in options map
  ;; we also need the :style from props along with the other
  ;; style properties we have defined, otherwise drag won't work
  (let [style-props (:style props)
        merged-style (merge style-props (container-style props))
        other-props (-> props
                        (dissoc :style)
                        ;; NOTE: without removing this key, will get warning:
                        ;; Warning: React does not recognize the `isDragging` prop on a DOM element.
                        (dissoc :is-dragging)
                        )]
  [:div (use-style merged-style other-props)
    child])
)

(defn task [{:keys [key index tsk]}]
  ^{:key key} [:> Draggable
   {:draggableId key
    :index index}

    ;; NOTE: this has to be a function otherwise will get error
    (fn [provided snapshot]
       (let [inner-ref (.-innerRef provided)
             draggable-props (-> (.-draggableProps provided)
                                (js->clj :keywordize-keys true))
             drag-hanlder-props (-> (.-dragHandleProps provided)
                                (js->clj :keywordize-keys true))
             is-dragging (.-isDragging snapshot)]

       (ra/as-element
        [container (-> {:ref inner-ref
                        :is-dragging is-dragging}
                           (merge draggable-props)
                           (merge drag-hanlder-props))
        (:content tsk)])
       ))
  ]
)
