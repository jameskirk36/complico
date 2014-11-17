(ns complico.tests.core
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [complico.core :as complico]))

(def ungreased-full-link "http://someurl.com/page")
(def ungreased-relative-link-with-slash "/page")
(def ungreased-relative-link-no-slash "page")

(def expected-greased-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")


(def grease "http://localhost:3000/convert?url=")
(def host "http://someurl.com/")

; needed to console.log works!
(enable-console-print!)

(deftest grease-the-link-case1
  (is (= (complico/grease-the-link host grease ungreased-full-link) expected-greased-link)))

(deftest grease-the-link-case2
  (is (= (complico/grease-the-link host grease ungreased-relative-link-with-slash) expected-greased-link)))

(deftest grease-the-link-case3
  (is (= (complico/grease-the-link host grease ungreased-relative-link-no-slash) expected-greased-link)))

(deftest convert-price-case-1 
  (is (= (complico/convert-price-in-text "£3") "£XXX")))

(deftest convert-price-case-2
  (is (= (complico/convert-price-in-text "£300") "£XXX")))

(deftest convert-price-should-not-change-text-without-price 
  (is (= (complico/convert-price-in-text "text without price") "text without price")))

