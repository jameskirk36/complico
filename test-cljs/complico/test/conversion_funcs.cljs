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

(deftest convert-price-divide-times
  (is (= 
        "£6.04 / £5.99 x 9.9"
        (dommy/html (funcs/divide-times "£" 9.99)))))
