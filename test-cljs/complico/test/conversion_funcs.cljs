(ns complico.tests.conversion-funcs
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require
   [dommy.core :as dommy]
   [complico.conversion-funcs :as funcs]
   [cemerick.cljs.test :as t]))

(deftest convert-price-divide-by-two
  (is (= (dommy/html (funcs/divide-by-two "£" 9.99)) "£19.98 / £2.00")))

(deftest convert-price-squared
  (is (= (dommy/html (funcs/squared "£" 9.99)) "£3.16<sup>2</sup>")))
