(ns complico.prices
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [complico.dom-helper :as dom-helper]
    [goog.string :as gstring]
    [goog.string.format])
  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(def ^:private price-elem-selector "div,span,li,p,a")

(defn- apply-price-formatting [price]
  (gstring/format "%.2f" price))

(defn- divide-by-two [currency price] 
  (let [new-price (-> price
                    (js/parseFloat)
                    (* 2.0)
                    (apply-price-formatting))]
    (str currency new-price " / " currency "2.00")))

(def conversion-functions [divide-by-two])

(defn select-func [i funcs]
  (->> funcs
    (count)
    (mod i)
    (nth funcs)))

(defn find-prices [text]
  (re-seq #"(£)(.*)$" text))

(defn- convert-price-test [currency price]
  (str currency "XXX"))

(defn- is-test-price [price]
  (= price "0"))

(defn convert-price [currency price conv-funcs]
  (if (is-test-price price) 
    (convert-price-test currency price)
    (-> price 
      (js/parseInt)
      (select-func conv-funcs)
      (apply currency price))))

(defn- convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (second %) (last %) conversion-functions))
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

