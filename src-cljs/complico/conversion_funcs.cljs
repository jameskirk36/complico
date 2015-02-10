; All the price conversion functions
(ns complico.conversion-funcs
  (:require 
    [dommy.core :as dommy]
    [hipo.interpreter :as hipo]
    [goog.string :as gstring]
    [goog.string.format]))

(defn- apply-price-formatting [price]
  (gstring/format "%.2f" price))

(defn divide-by-two [currency price] 
  (let [new-price (-> price
                    (js/parseFloat)
                    (* 2.0)
                    (apply-price-formatting))]
    (dommy/html (hipo/create [:div (str currency new-price " / " currency "2.00")]))))

(defn squared [currency price] 
  (let [new-price (-> price
                    (js/parseFloat)
                    (Math/sqrt)
                    (apply-price-formatting))]
    (dommy/html (hipo/create [:div (str currency new-price) [:sup "2"]]))))

(def conversion-functions [divide-by-two squared])
