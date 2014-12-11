(ns complico.tests.core
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [complico.core :as complico]))

; needed to console.log works!
(enable-console-print!)

(deftest convert-price-case-1 
  (is (= (complico/convert-price-in-text "£3") "£XXX")))

(deftest convert-price-case-2
  (is (= (complico/convert-price-in-text "£300") "£XXX")))

(deftest convert-price-should-not-change-text-without-price 
  (is (= (complico/convert-price-in-text "text without price") "text without price")))
