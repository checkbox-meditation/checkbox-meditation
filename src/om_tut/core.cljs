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
  }))

(defonce prefix " I notice ")

(defn checked [idx]
  ;; #js console.log(e)
  (println (str "clicked " idx))
  ;;(pprint/pprint (:relatedTarget e))
  (if (:checked (:relatedTarget e)) ()
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
                    (let [id (str "item" num)]
                      (dom/p nil
                        (dom/input #js {:type "checkbox" :id id :onClick #(checked num)} )
                        (dom/label #js {:htmlFor id} (str prefix text))))) 
                    (:list data)))

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
