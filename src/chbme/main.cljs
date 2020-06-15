(ns chbme.main
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [clojure.pprint :as pprint]
              [chbme.core :as core]
              [chbme.ru]
              [chbme.en]
    ))

(def menu-list [
      ["/"    "English"]
      ["/ru/" "по-русски"]
    ])

(defn build-menu-state [current]
  (atom { :list menu-list
          :current current
  }))

(defn menu-component [data owner]
  (reify om/IRender
    (render [_]
      (pprint/pprint data)
      (dom/div #js {:id "menu" :className "pure-menu pure-menu-horizontal"} 
        (dom/ul #js {:className "pure-menu-list"} 
          (dom/li #js {:className "pure-menu-item pure-menu-has-children pure-menu-allow-hover"}
            (let [cur (first (filter #(= (get % 0) (:current data)) (:list data)))
                  cur_link (get cur 0)
                  cur_name (get cur 1)]
                (println (str "current url " cur_link)) 
                (println (str "current name " cur_name)) 
                (dom/a #js {:href cur_link :className "pure-menu-link pure-menu-selected"} cur_name))
            (dom/ul #js {:className "pure-menu-children"} 
              (map-indexed 
                (fn [num item] 
                  (let [
                        current (if (= (:current data) (get item 0)) true false)
                        className (if current "pure-menu-selected pure-menu-item" 
                                              "pure-menu-item")]
                    (dom/li #js {:className className :key (str "k" num)} (get item 1))
                  )) 
                (:list data)))))))))


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
      menuTargetEl (. js/document (getElementById "menu-place"))
      lang     (. targetEl (getAttribute "lang"))
      state    (fetch-build-app-state
                 (if (= lang "en") chbme.en
                  (if (= lang "ru") chbme.ru)))]
  ;; (pprint/pprint targetEl)
  ;; (pprint/pprint lang)  

  ;; attach an om component to a DOM element
  (om/root core/checklist-app 
          state
          {:target targetEl})
  ;; attach menu
  (om/root menu-component 
          (build-menu-state "/") 
          {:target menuTargetEl})
  )