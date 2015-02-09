(ns complico.tests.links
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [node]])
  (:require [cemerick.cljs.test :as t]
            [complico.links :as links]))

(def ungreased-full-link "http://someurl.com/page")
(def ungreased-relative-link-with-slash "/page")
(def ungreased-relative-link-no-slash "page")

(def expected-greased-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")


(def grease "http://localhost:3000/convert?url=")
(def host "http://someurl.com/")

; needed to console.log works!
(enable-console-print!)

(deftest grease-the-link-case1
  (is (= (links/grease-the-link host grease ungreased-full-link) expected-greased-link)))

(deftest grease-the-link-case2
  (is (= (links/grease-the-link host grease ungreased-relative-link-with-slash) expected-greased-link)))

(deftest grease-the-link-case3
  (is (= (links/grease-the-link host grease ungreased-relative-link-no-slash) expected-greased-link)))

(deftest correctly-finds-elem-a
  (let [root-elem (node [:body [:a {:href "somelink"}]])
       found-elems (links/find-links root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-area
  (let [root-elem (node [:body [:area {:href "somelink"}]])
       found-elems (links/find-links root-elem)]
    (is (= 1 (count found-elems)))))
