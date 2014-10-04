(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs]
    [complico.core :as complico])

  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn replace-the-links! [complico-host original-host]
  (doseq [elem (sel :a)]
    (let [initial-link (attrs/attr elem :href)
          grease (str complico-host "/convert?url=")
          new-link (complico/grease-the-link original-host grease initial-link)]
      (attrs/set-attr! elem :href new-link))))

(defn extract-host-from-dom [host]
  (attrs/attr (sel1 :#complico_host_vars) (keyword host)))

(defn find-elems [elems] 
  (sel elems))

(defn add-ribbon-link! [complico-host]
  (dommy/append! (sel1 :body) 
    (node 
      [:a
        {:id "complico-ribbon-link"
         :href complico-host}
        [:img 
          {:style "position: absolute; top: 0; right: 0; border: 0; z-index: 9000;"
           :src (str complico-host "/images/ribbon.png")}]])))

(defn adjust-page []
  (let [original-host (extract-host-from-dom "original_host_name")
        complico-host (extract-host-from-dom "complico_host_name")]
    (replace-the-links! complico-host original-host)
    (add-ribbon-link! complico-host)))

;call function on window.onload
(set! (.-onload js/window) adjust-page)
