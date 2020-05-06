(ns chbme.ru
    (:require [chbme.core :as core]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]))


(defonce items [
      ", где я нахожусь"
      " своё дыхание"
      " вес своего тела"
      " телесные ощущения"
      " свои чувства и эмоции"
      " состояние своего ума"
      " свои мысли"
])
(defonce checked-initial (vec (map #(quote false) items)))

;; here we define the app data in an atom
(defonce app-state (atom 
  { :text "Осознавание себя. Чеклист"
    :items-prefix " Я замечаю"
    :list items
    :checked checked-initial
  }))


