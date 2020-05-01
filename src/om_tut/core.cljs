(ns om-tut.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [cljs.core.async :refer [put! chan <!]]
              [clojure.pprint :as pprint]))

(enable-console-print!)

(println "This text is printed from src/om-tut/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom 
  { :text "Self-awareness"
    :list [
      "my breath",
      "my body",
      "my feelings"
    ]
    :checked [ false true false ]
    :counter 0
  }))

(defonce prefix " I notice ")

(defn click [idx]
  ;; #js console.log(e)
  (println (str "clicked " idx))
  ;;(om/transact! data :checked #( (:checked data)))
  (swap! app-state update :checked #(assoc % idx true))
  (pprint/pprint app-state)
  ;;(if (:checked (:relatedTarget e)) ()  )
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

                  (dom/p nil (str "Clicked: " (:counter data)))
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
)
