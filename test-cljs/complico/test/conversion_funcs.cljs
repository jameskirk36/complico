(ns complico.tests.conversion-funcs
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest)])
  (:require
   [dommy.core :as dommy]
   [complico.conversion-funcs :as funcs]
   [cemerick.cljs.test :as t]))

(deftest convert-price-divide-by-two
  (is (= 
        "£19.98 / £2.00"
        (dommy/html (funcs/divide-by-two "£" 9.99)))))

(deftest convert-price-squared
  (is (= 
        "£3.16<sup style=\"font-size: 75%; line-height: 0; position: relative; vertical-align: baseline; top: -0.5em;\">2</sup>"
        (dommy/html (funcs/squared "£" 9.99)))))

(deftest convert-price-square-root
  (is (= 
        "<span style=\"white-space: nowrap; font-size:larger\">√<span style=\"text-decoration:overline;\">£25.00</span></span>"
        (dommy/html (funcs/square-root "£" 5.00)))))

(deftest convert-price-divide-times
  (is (= 
        "£6.04 / £5.99 x 9.9"
        (dommy/html (funcs/divide-times "£" 9.99)))))

(deftest convert-price-times-pi
  (is (= 
        "£3.18 x π"
        (dommy/html (funcs/times-pi "£" 9.99)))))
