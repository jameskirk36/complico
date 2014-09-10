(ns complico.test.core
  (:use [complico.core :only [grease-the-link]]))

(def ungreased-full-link "http://someurl.com/page")
(def ungreased-relative-link-with-slash "/page")
(def ungreased-relative-link-no-slash "page")

(def expected-greased-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")


(def grease "http://localhost:3000/convert?url=")
(def host "http://someurl.com/")

; needed to console.log works!
(enable-console-print!)

(defn run []
  (assert (= (grease-the-link host grease ungreased-full-link) expected-greased-link))
  (assert (= (grease-the-link host grease ungreased-relative-link-with-slash) expected-greased-link))
  (assert (= (grease-the-link host grease ungreased-relative-link-no-slash) expected-greased-link)))


