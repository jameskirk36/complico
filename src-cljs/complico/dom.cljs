(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs]
    [goog.dom :as gdom]
    [complico.prices :as prices]
    [complico.links :as links]
    [complico.dom-helper :as dom-helper]
    [hipo.interpreter :as hipo])

  (:use-macros
    [dommy.macros :only [sel sel1]]))

; this is needed to make nodelist iseqable!
(extend-type js/NodeList
  ISeqable
  (-seq [array] (array-seq array 0)))

(defn- extract-host-from-dom [host]
  (attrs/attr (sel1 :#complico_host_vars) (keyword host)))

(defn- add-ribbon-link! [complico-host]
  (dommy/append! (sel1 :body) 
    (hipo/create 
      [:a#complico-ribbon-link
        {:href complico-host}
        [:img 
          {:style "position: fixed; top: 0; right: 0; border: 0; z-index: 9000;"
           :src (str complico-host "/images/ribbon.png")}]])))

(defn hide-existing-form-element! [root-node]
  (if-let [form (sel1 root-node :form)]
    (attrs/set-attr! form :style "display: none;")))

(defn adjust-page []
  (let [original-host (extract-host-from-dom "original_host_name")
        complico-host (extract-host-from-dom "complico_host_name")
        body (sel1 :body)]
    (hide-existing-form-element! body) 
    (links/replace-the-links! body complico-host original-host)
    (add-ribbon-link! complico-host)
    (prices/replace-prices-in-dom! body)))

;call function on window.onload
(set! (.-onload js/window) adjust-page)
