(ns complico.prices
  (:require 
    [dommy.core :as dommy]
    [hipo.interpreter :as hipo]
    [complico.conversion-funcs :as funcs]
    [complico.dom-helper :as dom-helper])
  (:use-macros
    [dommy.macros :only [sel sel1]]))

(def ^:private price-elem-selector "div,span,li,p,a,em,td,strong")

(defn select-conv-func [price conv-funcs]
  (if (is-test-price price) 
    convert-price-test
    (let [i (-> price 
              (js/parseInt)
              (- 1))]
     (->> conv-funcs
       (count)
       (mod i)
       (nth conv-funcs)))))

(defn find-prices [text]
  (re-seq #"([£|$])([0-9]+(\.[0-9]{2})?)" text))

(defn- convert-price-test [currency price & args]
  (str currency "XXX"))

(defn- is-test-price [price]
  (= price "0"))

(defn convert-price [currency price select-conv-func conv-funcs]
  (let [conv-func (select-conv-func price conv-funcs)]
    (apply conv-func currency price nil)))

(defn- build-replacement [price-parts]
  (let [original-price (first price-parts)
        currency (second price-parts)
        price (nth price-parts 2)
        replacement-price (convert-price currency price select-conv-func funcs/conversion-functions)] 
  (vector original-price replacement-price))) 

(defn- convert-prices [prices]
  (map build-replacement prices))

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

(defn replace-price-on-elem! [elem]
  (if-let [text (dom-helper/get-text-from-node elem)]
    (if-let [prices (find-prices text)]
      (->> text
          (convert-price-in-text prices)
          (dommy/set-html! elem)))))

(defn replace-prices-in-dom! [root-node]
  (let [elems (find-elems root-node)]
    (doall (map replace-price-on-elem! elems))))

