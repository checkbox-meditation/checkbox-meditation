;; this was initially generated with figwheel with --om

(ns chbme.core
    (:require-macros [cljs.core.async.macros :refer [go]])
    ;;(:use [clojure.core] :reload)
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              ;;[cljs.core.async :refer [thread put! chan <! timeout]]
              [clojure.pprint :as pprint]
              ;;[clojure.core.async :as ca]
              ))

(enable-console-print!)

;; https://coderwall.com/p/s3j4va/google-analytics-tracking-in-clojurescript
;; by Aleš Roubíček https://coderwall.com/rarous
;; modified for gtag api: https://developers.google.com/analytics/devguides/collection/gtagjs/events
(defn track-event [& more] 
    (when (aget js/window "gtag")
      (.. (aget js/window "gtag")
          (apply nil (clj->js more))))
 )


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
          ;; timeout in miliseconds
          (:timeout state)))
      ;; track in google analytics
      (track-event "event" "checkItem" #js { :event_category "engagement" :event_label (:text item) })
  ))

(defn sublist-expand [idx state]
  (let [item (get (:items state) idx)
        value (:sublist-open item)
        newval (not value)]
      (om/update! state [:items idx :sublist-open] newval)
  ))

(defn checklist-render [data]
      ;(prn "render-state: in")
      ;(pprint/pprint data)
      (dom/div #js {:className (:style data)}
        (if (:title data) (dom/h1 nil (:title data)))
        ;;(apply dom/form #js {:className "points"}
        (let [key (:key data)]
        (map-indexed 
          (fn [num val] 
            (let [id (:id val) 
                  text (:text val)
                  sublist (if (:sublist val) (:sublist val))
                  sublist-open (:sublist-open val)
                  disabled (:checked val)
                  checked (:checked val)
                  checked-once (:checked-once val)
                  className (str "item " (if checked "done" "") " " (if sublist-open "expanded" ""))]
              ;;(pprint/pprint (type val))
              (dom/div #js { :className className :key id } 
                (dom/input 
                  #js { :type "checkbox" 
                        :id id 
                        :onClick #(on-click num data) 
                        :disabled disabled
                        :checked (if checked 1 "")
                      }
                  )
                (dom/label #js {:htmlFor id} 
                                ;; https://www.compart.com/en/unicode/U+00A0
                                ;; non-break space (NBSP)
                                (str "\u00a0" text)) 
                (if checked-once 
                  (dom/span #js {:className "mark"}
                                ;; non-break space + check mark
                                ;; https://www.compart.com/en/unicode/U+2713
                    "\u00a0\u2713"))
                (if sublist 
                  (if (:sublist-open val)
                    (list
                     (dom/span #js { :className "list-collapse"
                                     :title "collapse the expanded"
                                     :onClick #(sublist-expand num data)} 
                               "(collapse)")
                     (dom/div #js {:className "list"}
                              (checklist-render sublist)))
                    (dom/span #js { :className "list-expand" 
                                    :title "expand a sublist"
                                    :onClick #(sublist-expand num data)}
                              "..." )) 
                )
            ))
          ) 
          (:items data)))))

(defn checklist-app [data owner]
  (reify om/IRender
    (render [_] (checklist-render data))))
