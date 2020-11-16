(ns dragdrop.column
  (:require
   [stylefy.core :refer [use-style]]
   [dragdrop.task :refer [task]])
)

(def container-style
  {:margin "8px"
   :border "1px solid lightgrey"
   :border-radius "2px"
   })

(def title-style
  {:padding "8px"}
)

(def task-list-style
  {:padding "8px"}
)

(defn- container [& child]
  [:div (use-style container-style)
    child])

(defn- title [text]
  [:h3 (use-style title-style) text]
)

(defn- task-list [tasks]
  [:div (use-style task-list-style)
   (map #(task {:key (:id %)
                 :tsk %})
        tasks)
   ]
)

(defn Column
  [{:keys [key column tasks]}]

   [container
  ^{:key "title"} [title (:title column)]
  ^{:key "task-list"} [task-list tasks]
  ]
)
