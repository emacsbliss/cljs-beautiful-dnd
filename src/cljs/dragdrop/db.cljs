(ns dragdrop.db)

(def default-db
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
   })
