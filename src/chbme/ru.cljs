(ns chbme.ru
    (:require [chbme.core :as core]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]))


(defonce items [
      ", где я нахожусь и что вокруг"
      " своё дыхание"
      " вес своего тела и телесные ощущения"
      " свои чувства и эмоции"
      " состояние своего ума и мысли"
      " своё присутствие"
])
(defonce checked-initial (vec (map #(quote false) items)))
(defonce checked-once-initial (vec (map #(quote false) items)))

;; here we define the app data in an atom
(defonce app-state (atom 
  { :title "Осознавание себя. Чеклист"
    :items-prefix " я замечаю"
    :list items
    :checked checked-initial
    :checked-once checked-once-initial
  }))


