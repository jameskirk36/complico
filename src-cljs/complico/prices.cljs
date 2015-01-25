(ns complico.prices
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [complico.dom-helper :as dom-helper]
    [goog.string :as gstring]
    [goog.string.format])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(def price-elem-selector "div,span,li,p,a")

(defn apply-price-formatting [price]
  (gstring/format "%.2f" price))

(defn divide-by-two [price] 
  (-> price
    (js/parseFloat)
    (* 2.0)
    (apply-price-formatting)
    (str " / 2")))

(defn find-prices [text]
  (re-seq #"£(.*)$" text))

(defn convert-price [price]
  "£XXX")

(defn convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (last %)))
    prices))

(defn find-elems [root-elem]
  (sel root-elem price-elem-selector))

(defn convert-price-in-text [text]
  (let [prices (find-prices text)
        replacement-prices (convert-prices prices)]
    (reduce 
      #(apply clojure.string/replace %1 %2)
      text
      replacement-prices))) 

(defn replace-price-on-elem! [elem]
  (if-let [text (dom-helper/get-text-from-node elem)]
    (->> text
      (convert-price-in-text)
      (dom-helper/replace-text-on-node! elem))))

(defn replace-prices-in-dom! [root-node]
  (let [elems (find-elems root-node)]
    (doall (map replace-price-on-elem! elems))))

