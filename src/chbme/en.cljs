(ns chbme.en
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
(defonce checked-once-initial (vec (map #(quote false) items)))

;; here we define the app data in an atom
(defonce app-state (atom 
  { :text "Self-awareness checklist"
    :items-prefix " I notice my "
    :list items
    :checked checked-initial
    :checked-once checked-once-initial
  }))


