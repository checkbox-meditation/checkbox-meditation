;; contents of this file adapted from src/chbme/main.cljs
(ns chebome.lib
  (:require [om.core :as om :include-macros true]
            [clojure.pprint :as pprint]
            [chbme.core :as core]))

;; initial app state structure:
;;   :title text
;;   :key string, optional
;;   :items list
;;   :items-prefix text
;;   :style string, optional
;;   :timeout, integer, optional
;;
;; and it is infinitely recursive, since each item in :items may be one of two things:
;; - a text string
;; - a vector containing a text string and a list object, meaning a sub-list; and 
;;   the list object in this case has the same structure as the initial app structure 
;;

;; default timeout for a checkbox recovery after being clicked 
;; and disabled, in milliseconds. Can be overriden
(def default-timeout 5000)

;; from https://gist.github.com/rboyd/5053955
(defn rand-str [len]
  (apply str
         (for [i (range len)]
           (char (+ (rand 26) 65)))))

(defn item-state [prefix item appstate]
  (if (= (type item) js/String)
    {:text (str prefix item)
     :id (str "id" (rand-str 6))
     :checked false
     :checked-once false}
    (let [sublist (nth item 1)
          item (nth item 0)]
      (assoc (item-state prefix item appstate)
             :sublist (list-state sublist appstate)
             :sublist-open false))))

(defn list-state [_list appstate]
  (let [title (:title _list)
        prefix (:items-prefix _list)
        rawitems (:items _list)
        rootstate (if appstate appstate _list)
        items (vec (map (fn [i] (item-state prefix i rootstate)) rawitems))]
    {:key (:key _list (str "key" (rand-str 6)))
     :prefix prefix
     :title title
     :style (:style _list)
     :timeout (:timeout _list (:timeout rootstate default-timeout)) ;; in milliseconds
     :items items}))

(defn ^:export init-app-state [from]
  (list-state from))

(defn ^:export attach-app [target init]
    ;; attach an om component to a DOM element
    ;;(pprint/pprint state-init)
  (om/root core/checklist-app
           (init-app-state (js->clj init :keywordize-keys true))
           {:target target}))
