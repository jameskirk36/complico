(ns complico.tests.core
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t])
  (:use [complico.core :only [grease-the-link]]))

(def ungreased-full-link "http://someurl.com/page")
(def ungreased-relative-link-with-slash "/page")
(def ungreased-relative-link-no-slash "page")

(def expected-greased-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")


(def grease "http://localhost:3000/convert?url=")
(def host "http://someurl.com/")

; needed to console.log works!
(enable-console-print!)

(deftest grease-the-link-case1
  (is (= (grease-the-link host grease ungreased-full-link) expected-greased-link)))

(deftest grease-the-link-case2
  (is (= (grease-the-link host grease ungreased-relative-link-with-slash) expected-greased-link)))

(deftest grease-the-link-case3
  (is (= (grease-the-link host grease ungreased-relative-link-no-slash) expected-greased-link)))
