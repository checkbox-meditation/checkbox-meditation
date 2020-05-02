(ns chbme.ru_main
    (:require [chbme.core :as core]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]))


(defonce items [
      "physical environment",
      "breath",
      "body",
      "body weight",
      "body sensations",
      "feelings",
      "mental state",
      "thoughts"
])
(defonce checked-initial (vec (map #(quote false) items)))

;; here we define the app data in an atom
(defonce app-state (atom 
  { :text "Само-наблюдение, вопросы"
    :list items
    :checked checked-initial
  }))

;; attach an om component to a DOM element
(om/root core/checklist-app app-state {:target (. js/document (getElementById "app"))})

