(ns chbme.main
    (:require [chbme.core :as core]
              [chbme.ru]
              [chbme.en]
              [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [clojure.pprint :as pprint]
    ))

;; attach an om component to a DOM element

(let [targetEl (. js/document (getElementById "app"))
      lang     (. targetEl (getAttribute "lang"))]
      (pprint/pprint targetEl)
      (pprint/pprint lang)  

(om/root  core/checklist-app 
          (if (= lang "en") 
            chbme.en/app-state
            (if (= lang "ru")
              chbme.ru/app-state)) 
          {:target targetEl}))