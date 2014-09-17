(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn init []
  (.log js/console "Finished loading page"))

;call init on window.onload
(set! (.-onload js/window) init)
