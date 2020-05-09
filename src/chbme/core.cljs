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
  (let [value (get (:checked state) idx)
        newval (not value)]
      (println (str "clicked line " idx ", which was " (if value "checked" "unchecked")))
      (om/update! state [:checked idx] newval)
      (if (not (get (:checked-once state) idx))
          (om/update! state [:checked-once idx] true))
      ;; here, we schedule an auto-reset the checked item, after a little wait
      ;; https://clojuredocs.org/clojure.core.async/go
      ;; another resource: https://purelyfunctional.tv/guide/clojure-concurrency/#core.async
      (if newval 
        (ca/go (<! (ca/timeout restore-delay))
              (om/update! state [:checked idx] false)))
      ;; track in google analytics
      (gtag "event" "checkItem" #js { :event_category "engagement" :event_label (get (:list state) idx) })
      (pprint/pprint (:checked state))
  ))

(defn checklist-app [data owner]
  (reify om/IRender
    (render [_]
      ;(prn "render-state: in")
      ;(pprint/pprint data)
      (dom/div nil
        (dom/h1 nil (:title data))
        ;; (dom/p nil "Edit this and watch it change!")
        (apply dom/form #js {:className "points"}
        (map-indexed 
          (fn [num text] 
          (let [id (str "item" num)
                disabled (get (:checked data) num)
                checked (get (:checked data) num)
                checked-once (get (:checked-once data) num)
                className (if checked "done" "")]
            (dom/p nil
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
                              (str " " (:items-prefix data) text))
              (if checked-once 
                (dom/span #js {:className "mark"}
                  " \u2713"))
                                ))) 
          (:list data)))

        #_(dom/p nil (str "Clicked: " (:counter data)))
        ;; (dom/form nil 
        ;;  (dom/input #js {:type "checkbox" :name "breath"} )
        ;;  (dom/label #js {:htmlFor "breath"} " I notice my breath."))
        ))))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  ;;(pprint/pprint (:checked @app-state))
)
