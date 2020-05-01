(ns om-tut.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    ;;(:use [clojure.core] :reload)
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [cljs.core.async :refer [put! chan <!]]
              [clojure.pprint :as pprint]
              ))

(enable-console-print!)

(println "This text is printed from src/om-tut/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce items [
      "my breath",
      "my body",
      "my feelings"
])
(defonce checked-initial (vec (map #(quote false) items)))
(defonce app-state (atom 
  { :text "Self-awareness"
    :list items
    :checked checked-initial
    :counter 0
  }))

(defonce prefix " I notice ")

(defn click [idx]
  ;; #js console.log(e)
  ;;(om/transact! data :checked #( (:checked data)))
  (pprint/pprint (:checked @app-state))
  (let [value (get (:checked @app-state) idx)
        newval (if value false true)]
      (println (str "clicked line " idx ", which was " (if value "checked" "unchecked")))
      (swap! app-state update :checked #(assoc % idx newval))
  )
)

(om/root
  (fn [data owner]
    (reify om/IRender
      (render [_]
        (dom/div nil
                 (dom/h1 nil (:text data))
                 ;; (dom/p nil "Edit this and watch it change!")
                 (apply dom/form #js {:className "points"}
                  (map-indexed 
                    (fn [num text] 
                    (let [id (str "item" num)
                          disabled (get (:disabled data) num)
                          className (if disabled "disabled" "")
                          checked (get (:checked data) num)]
                      (dom/p nil
                        (dom/input 
                          #js { :type "checkbox" 
                                :id id 
                                :onClick #(click num) 
                                :disabled disabled
                                :className className
                                :checked (if checked 1 "")
                              }
                          )
                        (dom/label #js {:htmlFor id} (str prefix text))))) 
                    (:list data)))

                  #_(dom/p nil (str "Clicked: " (:counter data)))
                  ;; (dom/form nil 
                  ;;  (dom/input #js {:type "checkbox" :name "breath"} )
                  ;;  (dom/label #js {:htmlFor "breath"} " I notice my breath."))
                  ))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (pprint/pprint (:checked @app-state))
)
