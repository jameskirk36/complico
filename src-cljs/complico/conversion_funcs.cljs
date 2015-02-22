; All the price conversion functions
(ns complico.conversion-funcs
  (:require 
    [hipo.interpreter :as hipo]
    [goog.string :as gstring]
    [goog.string.format]))

(defn- apply-price-formatting [price]
  (gstring/format "%.2f" price))

(defn divide-by-two [currency price & args] 
  (let [new-price (-> price
                    (* 2.0)
                    (apply-price-formatting))]
    (hipo/create [:div (str currency new-price " / " currency "2.00")])))

(defn squared [currency price & args] 
  (let [new-price (-> price
                    (Math/sqrt)
                    (apply-price-formatting))]
    (hipo/create [:div (str currency new-price) [:sup "2"]])))

(def conversion-functions [divide-by-two squared])
