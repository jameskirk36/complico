; All the price conversion functions
(ns complico.conversion-funcs
  (:require 
    [hipo.interpreter :as hipo]
    [goog.string :as gstring]
    [goog.string.format]))

(defn- apply-price-formatting 
  [price]
  (gstring/format "%.2f" price))

(defn divide-by-two 
  [currency price] 
  (let [new-price (-> price
                    (* 2.0)
                    (apply-price-formatting))]
    (hipo/create [:div (str currency new-price " / " currency "2.00")])))

(defn squared 
  [currency price] 
  (let [new-price (-> price
                    (Math/sqrt)
                    (apply-price-formatting))]
    (hipo/create [:div (str currency new-price) 
                   [:sup {:style "font-size: 75%; line-height: 0; position: relative; vertical-align: baseline; top: -0.5em;"} "2"]])))

(defn square-root 
  [currency price] 
  (let [new-price (-> price
                    (* price)
                    (apply-price-formatting))]
    (hipo/create [:div 
                   [:span {:style "white-space: nowrap; font-size:larger"} (gstring/unescapeEntities "&radic;")
                     [:span {:style "text-decoration:overline;"} (str currency new-price)]]])))

(defn divide-times 
 [currency price]
 (let [new-price (-> price
                    (/ 9.9)
                    (* 5.99)
                    (apply-price-formatting))]
    (hipo/create [:div (str currency new-price " / " currency "5.99 x 9.9")])))

(def conversion-functions [divide-by-two squared square-root divide-times])
