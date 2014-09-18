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


(defn add-ribbon [complico-host]
  (dommy/append! (sel1 :body) 
    (node 
      [:a
        {:id "complico-ribbon-link"
         :href complico-host}
        [:img 
          {:style "position: absolute; top: 0; right: 0; border: 0; z-index: 9000;"
           :src (str complico-host "/images/ribbon.png")}]])))

(defn init []
  (let [original-host (extract-original-host)
        complico-host (extract-complico-host)]
    (grease-the-links-new original-host complico-host)
    (add-ribbon complico-host)))

;call init on window.onload
(set! (.-onload js/window) init)
