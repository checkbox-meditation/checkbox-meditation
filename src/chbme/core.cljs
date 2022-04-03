;; this was initially generated with figwheel with --om

(ns chbme.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    ;;(:use [clojure.core] :reload)
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              ;;[cljs.core.async :refer [thread put! chan <! timeout]]
              [clojure.pprint :as pprint]
              [clojure.core.async :as ca]
              ))

(enable-console-print!)

;; https://coderwall.com/p/s3j4va/google-analytics-tracking-in-clojurescript
;; by Aleš Roubíček https://coderwall.com/rarous
;; modified for gtag api: https://developers.google.com/analytics/devguides/collection/gtagjs/events
(defn gtag [& more]
  (when js/gtag
    (.. (aget js/window "gtag")
        (apply nil (clj->js more)))))

;; miliseconds
(def restore-delay 5000)

(defn on-click [idx state]
  ;; #js console.log(e)
  ;;(om/transact! data :checked #( (:checked data)))
  ;(prn "on-click: in")
  ;(pprint/pprint state)
  (let [item (get (:items state) idx)
        value (:checked item)
        newval (not value)]
      (println (str "clicked line " idx ", which was " (if value "checked" "unchecked")))
      (om/update! state [:items idx :checked] newval)
      (if (not (:checked-once item))
          (om/update! state [:items idx :checked-once] true))
      ;; here, we schedule an auto-reset the checked item, after a little wait
      ;; https://clojuredocs.org/clojure.core.async/go
      ;; another resource: https://purelyfunctional.tv/guide/clojure-concurrency/#core.async
      (if newval 
        (js/setTimeout ; https://yogthos.net/posts/2017-03-26-ReagentReactView.html
          #(om/update! state [:items idx :checked] false)
          restore-delay))
      ;; track in google analytics
      (gtag "event" "checkItem" #js { :event_category "engagement" :event_label (:text item) })
  ))

(defn checklist-render [data]
      ;(prn "render-state: in")
      ;(pprint/pprint data)
      (dom/div nil
        (if (:title data) (dom/h1 nil (:title data)))
        ;;(apply dom/form #js {:className "points"}
        (let [key (:key data)]
        (map-indexed 
          (fn [num val] 
          (let [id (:id val) 
                text (:text val)
                sublist (if (:sublist val) (:sublist val))
                disabled (:checked val)
                checked (:checked val)
                checked-once (:checked-once val)
                className (if checked "done" "")]
            ;;(pprint/pprint (type val))
            (dom/div #js { :className "item" :key id } 
              (dom/input 
                #js { :type "checkbox" 
                      :id id 
                      :onClick #(on-click num data) 
                      :disabled disabled
                      :className className
                      :checked (if checked 1 "")
                    }
                )
              (dom/label #js {:htmlFor id 
                              :className className} 
                              (str " " text))
              (if checked-once 
                (dom/span #js {:className "mark"}
                  " \u2713"))
              (if sublist 
                (dom/div #js {:className "list"}   
                         (checklist-render sublist)
                         ))
                                ))) 
          (:items data)))))

(defn checklist-app [data owner]
  (reify om/IRender
    (render [_] (checklist-render data))))

;;        #_(dom/p nil (str "Clicked: " (:counter data)))
        ;; (dom/form nil 
        ;;  (dom/input #js {:type "checkbox" :name "breath"} )
        ;;  (dom/label #js {:htmlFor "breath"} " I notice my breath."))
;;;        ))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  ;;(pprint/pprint (:checked @app-state))
)
