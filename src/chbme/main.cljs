(ns chbme.main
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [clojure.pprint :as pprint]
              [chbme.core :as core]
              [chbme.ru]
              [chbme.en]
    ))

(defn rand-str [len]
  (apply str
         (for [i (range len)]
           (char (+ (rand 26) 65)))))

(defn item-state [prefix item]
  { :text (str prefix item)
    :id (str "id" (rand-str 6))
    :checked false
    :checked-once false })

(defn fetch-build-app-state [from] 
  (let [title  from/title
        prefix from/items-prefix
        list   from/items
        items (vec (map (fn [i] (item-state prefix i)) list))]
    (atom {
        :key from/_key
        :title title
        :items items
        :index (hash-map (map-indexed (fn [n v] (list (:id v) n)) items))
    })))

(defn attach-app [target]
   (let [lang     (. target (getAttribute "lang"))
         state    (fetch-build-app-state
                    (if (= lang "en") chbme.en
                    (if (= lang "ru") chbme.ru)))]
  ;;(pprint/pprint target)
  ;;(pprint/pprint lang)
  ;; attach an om component to a DOM element
  (om/root core/checklist-app 
          state
          {:target target})))

;; main code run
(let [targetEls (array-seq (.getElementsByClassName js/document "chbme"))]
  ;;(pprint/pprint targetEls)
  (doseq [t targetEls] (attach-app t))
)