(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs]
    [goog.dom :as gdom]
    [complico.core :as complico]
    [complico.dom-helper :as dom-helper])

  (:use-macros
    [dommy.macros :only [node sel sel1]]))

; this is needed to make nodelist iseqable!
(extend-type js/NodeList
  ISeqable
  (-seq [array] (array-seq array 0)))

(def price-elem-selector "div,span,li")

(defn replace-the-links! [complico-host original-host]
  (doseq [elem (sel :a)]
    (let [initial-link (attrs/attr elem :href)]
       (if (not (nil? initial-link))
          (let [grease (str complico-host "/convert?url=")
                new-link (complico/grease-the-link original-host grease initial-link)]
               (attrs/set-attr! elem :href new-link))))))

(defn extract-host-from-dom [host]
  (attrs/attr (sel1 :#complico_host_vars) (keyword host)))

(defn find-elems [root-elem] 
  (sel root-elem price-elem-selector))

(defn find-prices [text]
  (re-seq #"£(\d+)" text))

(defn convert-price [price]
  "£XXX")

(defn convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (last %)))
    prices))

(defn convert-price-in-text [text]
  (let [prices (find-prices text)
        replacement-prices (convert-prices prices)]
    (reduce 
      #(apply clojure.string/replace %1 %2)
      text
      replacement-prices))) 

(defn replace-price-on-elem! [elem]
  (let [text (dom-helper/get-text-from-node elem)]
    (if-not (= text nil) 
      (->> text
        (convert-price-in-text)
        (dom-helper/replace-text-on-node! elem)))))

(defn replace-prices-in-dom! [root-node]
  (let [elems (find-elems root-node)]
    (doall (map replace-price-on-elem! elems))))

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
    (add-ribbon-link! complico-host)
    (replace-prices-in-dom! (sel1 :body))))

;call function on window.onload
(set! (.-onload js/window) adjust-page)
