(ns complico.tests.links
  (:require-macros [cemerick.cljs.test
                    :refer (is are deftest)])
  (:require [cemerick.cljs.test :as t]
            [hipo.interpreter :as hipo]
            [complico.links :as links]))

(def grease "http://localhost:3000/convert?url=")
(def host "http://someurl.com/")
(def expected-greased-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")

; needed to console.log works!
(enable-console-print!)

(deftest grease-the-link-full
  (let [link "http://someurl.com/page"]
    (is (= expected-greased-link (links/grease-the-link host grease link)))))

(deftest grease-the-link-relative-with-slash
  (let [link "/page"]
    (is (= expected-greased-link (links/grease-the-link host grease link)))))

(deftest grease-the-link-relative-no-slash
  (let [link "page"]
    (is (= expected-greased-link (links/grease-the-link host grease link)))))

(defn create-test-dom 
  [elem]
  (hipo/create [:body [elem {:href "somelink"}]]))

(deftest finds-allowed-links-elems
  (are [elem] (-> elem (create-test-dom) (links/find-links) (count) (= 1))
   :a :area))
