(ns complico.dom 
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs]
    [goog.dom :as gdom]
    [complico.core :as complico])

  (:use-macros
    [dommy.macros :only [node sel sel1]]))

; this is needed to make nodelist iseqable!
(extend-type js/NodeList
  ISeqable
  (-seq [array] (array-seq array 0)))

(def price-elem-selector "div,span")

(defn replace-the-links! [complico-host original-host]
  (doseq [elem (sel :a)]
    (let [initial-link (attrs/attr elem :href)
          grease (str complico-host "/convert?url=")
          new-link (complico/grease-the-link original-host grease initial-link)]
      (attrs/set-attr! elem :href new-link))))

(defn extract-host-from-dom [host]
  (attrs/attr (sel1 :#complico_host_vars) (keyword host)))

(defn find-elems [root-elem selector-string] 
  (sel root-elem selector-string))

(defn is-text-node [node] 
  (= (.-nodeType node) 3))

(defn get-child-nodes [node]
  (.-childNodes node))

(defn get-text-node [node]
  (first (filter #(is-text-node %) (get-child-nodes node))))

(defn get-text-from-node [node]
  (let [text-node (get-text-node node)]
    (if (= text-node nil)
      nil
      (.-nodeValue text-node))))

(defn set-text-on-node [node text]
  (set! (.-nodeValue node) text))

(defn replace-text-on-node! [elem text]
  (let [node (get-text-node elem)]
    (set-text-on-node node text))
  elem)

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
  (let [text (get-text-from-node elem)]
    (if-not (= text nil) 
      (->> text
        (convert-price-in-text)
        (replace-text-on-node! elem)))))

(defn replace-prices-in-dom! [root-node]
  (let [elems (find-elems root-node price-elem-selector)]
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
    (add-ribbon-link! complico-host)))

;call function on window.onload
(set! (.-onload js/window) adjust-page)
