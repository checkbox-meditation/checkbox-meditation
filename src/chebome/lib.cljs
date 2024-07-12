;; contents of this file adapted from src/chbme/main.cljs
(ns chebome.lib
  (:require [om.core :as om :include-macros true]
            [clojure.pprint :as pprint]
            [chbme.core :as core]
            [cljs.pprint :as pprint]))

;; initial app state structure:
;;   :title text
;;   :key string, optional
;;   :items list
;;   :items-prefix text
;;   :style string, optional
;;   :timeout, integer, optional
;;   :state
;;   :options
;;
;; and it is infinitely recursive, since each item in :items may be one of two things:
;; - a text string, or
;; - a vector containing:
;;   1) a text string and 
;;   2) an item-options object, optional.
;;   3) a sub-list object, optional. 
;;      Meaning an expandable sub-list below this item; and 
;;      the sub-list object in this case has the same structure as 
;;      the initial app structure.
;;
;; The following is unfinished, yet unimplemented thinking:
;; 
;; Item state :state could be:
;;   "hidden"
;;   "live"
;;   "checked-disabled"
;;   "checked-live"
;;   "disabled"

;; ...or, if we track the checked count as a separate piece of state, it could be:
;;   "hidden", "live", "disabled"

;; The :sublist-state in an item-options could be:
;;   "hidden"
;;   "collapsed"
;;   "open"

;; default timeout for a checkbox recovery after being clicked 
;; and disabled, in milliseconds. Can be overriden
(def default-timeout 5000)

;; from https://gist.github.com/rboyd/5053955
(defn rand-str [len]
  (apply str
         (for [i (range len)]
           (char (+ (rand 26) 65)))))

(defn item-state [prefix item appstate & [opt]]
  (if (= (type item) js/String)
    (do 
;;      (if opt (pprint/pprint opt))
      (assoc (if opt opt {})
             :text (str prefix item)
             :id (str "item" (rand-str 6))
             :checked false
             :checked-once false
             ))
    (let [name    (nth item 0) 
          o       (nth item 1 opt)
          sublist (nth item 2 nil)
          additional (if sublist 
                        {:sublist (list-state sublist appstate)
                         :sublist-state (if (and o
                                                (:sublist-state o))
                                            (:sublist-state o)
                                            (:sublist-state appstate))
                        } 
                        nil)]
      (merge (item-state prefix name appstate o)
            additional 
      ))))


(defn list-state [_list & [appstate]]
  (let [title (:title _list)
        prefix (:items-prefix _list)
        rawitems (:items _list)
        options (:options _list)
        state (:state _list)
        rootstate (if appstate appstate _list)
        items (vec (map (fn [i] (item-state prefix i rootstate)) rawitems))]
    {:key (:key _list (str "list" (rand-str 6)))
     :prefix prefix
     :title title
     :style (:style _list)
     :timeout (:timeout _list (:timeout rootstate default-timeout)) ;; in milliseconds
     :state state
     :options options 
     :items items}))

(defn ^:export init-app-state [from]
  (pprint/pprint (list-state from))
  (list-state from))

(defn ^:export attach-app [target init]
    ;; attach an om component to a DOM element
  (om/root core/checklist-app
           (init-app-state (js->clj init :keywordize-keys true))
           {:target target}))
