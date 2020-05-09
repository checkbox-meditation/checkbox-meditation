(ns chbme.main
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [clojure.pprint :as pprint]
              [chbme.core :as core]
              [chbme.ru]
              [chbme.en]
    ))

(defn fetch-build-app-state [from] 
  (let [title  from/title
        prefix from/items-prefix
        list   from/items]
    (atom {
    :title title
    :items-prefix prefix
    :list list
    :checked (vec (map #(quote false) list))
    :checked-once (vec (map #(quote false) list))
    })))

(let [targetEl (. js/document (getElementById "app"))
      lang     (. targetEl (getAttribute "lang"))
      state    (fetch-build-app-state
                 (if (= lang "en") chbme.en
                  (if (= lang "ru") chbme.ru)))]
  ;; (pprint/pprint targetEl)
  ;; (pprint/pprint lang)  

  ;; attach an om component to a DOM element
  (om/root  core/checklist-app 
          state
          {:target targetEl}))