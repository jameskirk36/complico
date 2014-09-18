(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs]
    [complico.core :as complico])

  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn grease-the-links-new [original-host complico-host]
  (doseq [elem (sel :a)]
    (let [greased-link (complico/grease-the-link original-host (str complico-host "/convert?url=") (attrs/attr elem :href))]
      (.log js/console greased-link)
      (attrs/set-attr! elem :href greased-link))))

(defn extract-original-host []
  (attrs/attr (sel1 :#complico_host_vars) :original_host_name))

(defn extract-complico-host []
  (attrs/attr (sel1 :#complico_host_vars) :complico_host_name))

(defn init []
  (.log js/console "Finished loading page")
  (let [original-host (extract-original-host)
        complico-host (extract-complico-host)]
    (grease-the-links-new original-host complico-host)))

;call init on window.onload
(set! (.-onload js/window) init)
