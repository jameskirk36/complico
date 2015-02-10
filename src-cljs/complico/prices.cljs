(ns complico.prices
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [hipo.interpreter :as hipo]
    [complico.conversion-funcs :as funcs]
    [complico.dom-helper :as dom-helper])
  (:use-macros
    [dommy.macros :only [sel sel1]]))

(def ^:private price-elem-selector "div,span,li,p,a")

(defn select-func [i funcs]
  (->> funcs
    (count)
    (mod i)
    (nth funcs)))

(defn find-prices [text]
  (re-seq #"([£|$])(.*)$" text))

(defn- convert-price-test [currency price]
  (str currency "XXX"))

(defn- is-test-price [price]
  (= price "0"))

(defn convert-price [currency price conv-funcs]
  (if (is-test-price price) 
    (convert-price-test currency price)
    (-> price 
      (js/parseInt)
      (- 1)
      (select-func conv-funcs)
      (apply currency price))))

(defn- convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (second %) (last %) funcs/conversion-functions))
    prices))

(defn find-elems [root-elem]
  (sel root-elem price-elem-selector))

(defn- build-new-price-text [text replacement-prices]
  (reduce 
    #(apply clojure.string/replace %1 %2)
    text
    replacement-prices))

(defn- convert-price-in-text [prices original-text]
  (->> prices 
    (convert-prices)
    (build-new-price-text original-text)))

(defn- replace-price-on-elem! [elem]
  (if-let [text (dom-helper/get-text-from-node elem)]
    (if-let [prices (find-prices text)]
      (->> text
          (convert-price-in-text prices)
          (dommy/set-html! elem)))))

(defn replace-prices-in-dom! [root-node]
  (let [elems (find-elems root-node)]
    (doall (map replace-price-on-elem! elems))))

