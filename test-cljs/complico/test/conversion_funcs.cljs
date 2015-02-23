(ns complico.tests.conversion-funcs
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest)])
  (:require
   [dommy.core :as dommy]
   [complico.conversion-funcs :as funcs]
   [cemerick.cljs.test :as t]))

(deftest convert-price-divide-by-two
  (is (= (dommy/html (funcs/divide-by-two "£" 9.99)) "£19.98 / £2.00")))

(deftest convert-price-squared
  (is (= (dommy/html (funcs/squared "£" 9.99)) "£3.16<sup style=\"font-size: 75%; line-height: 0; position: relative; vertical-align: baseline; top: -0.5em;\">2</sup>")))

(deftest convert-price-square-root
  (is (= (dommy/html (funcs/square-root "£" 5.00)) "<span style=\"white-space: nowrap; font-size:larger\">√<span style=\"text-decoration:overline;\">£25.00</span></span>")))
