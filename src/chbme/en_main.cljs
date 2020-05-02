(ns chbme.en_main
    (:require [chbme.core :as core]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]))


;; define your app data so that it doesn't get over-written on reload

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
(defonce app-state (atom 
  { :text "Self-awareness checklist"
    :list items
    :checked checked-initial
  }))

(om/root core/checklist-app app-state {:target (. js/document (getElementById "app"))})

