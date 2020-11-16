(ns dragdrop.task
  (:require
   [stylefy.core :refer [use-style]])
)

(def container-style
  {:margin-bottom "8px"
   :border "1px solid lightgrey"
   :border-radius "2px"
   :padding "8px"
})

(defn- container [& child]
  [:div (use-style container-style)
    child])

(defn task [{:keys [key tsk]}]
  ^{:key key} [container
  (:content tsk)]
)
