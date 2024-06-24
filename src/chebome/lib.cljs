;; contents of this file adapted from src/chbme/main.cljs
(ns chebome.lib
  (:require [om.core :as om :include-macros true]
            [clojure.pprint :as pprint]
            [chbme.core :as core]))

;; default timeout for a checkbox recovery after being clicked 
;; and disabled, in milliseconds. Can be overriden
(def default-timeout 5000)

;; from https://gist.github.com/rboyd/5053955
(defn rand-str [len]
  (apply str
         (for [i (range len)]
           (char (+ (rand 26) 65)))))

(defn item-state [prefix item]
  (if (= (type item) js/String)
    {:text (str prefix item)
     :id (str "id" (rand-str 6))
     :checked false
     :checked-once false}
    (let [sublist (nth item 1)
          item (nth item 0)]
      (assoc (item-state prefix item)
             :sublist (sublist-state sublist)
             :sublist-open false))))

(defn sublist-state [_list]
  (let [title (:title _list)
        prefix (:items-prefix _list)
        rawitems (:items _list)
        items (vec (map (fn [i] (item-state prefix i)) rawitems))]
    {:key (:key _list)
     :prefix prefix
     :title title
     :style (:style _list)
     :items items}))

(defn ^:export init-app-state [from]
  (let [title    (:title from)
        prefix   (:items-prefix from)
        rawitems (:items from)
        items    (vec (map (fn [i] (item-state prefix i)) rawitems))]
    (atom {:key (:key from)
           :title title
           :items items
           :style (:style from) 
           :timeout (:timeout from default-timeout) ;; in milliseconds
           })))

(defn ^:export attach-app [target init]
    ;; attach an om component to a DOM element
    ;;(pprint/pprint state-init)
  (om/root core/checklist-app
           (init-app-state (js->clj init :keywordize-keys true))
           {:target target}))
