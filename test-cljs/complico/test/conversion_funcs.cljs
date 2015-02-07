(ns complico.tests.conversion-funcs
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require
   [complico.conversion-funcs :as funcs]
   [cemerick.cljs.test :as t]))

(deftest convert-price-divide-by-two
  (is (= (funcs/divide-by-two "£" "9.99") "£19.98 / £2.00")))

(deftest convert-price-squared
  (is (= (funcs/squared "£" "9.99") "£3.16<sup>2</sup>")))
