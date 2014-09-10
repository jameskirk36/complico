(ns complico.test.core
  (:use [complico.core :only [grease-the-link]]))

(def ungreased-full-link "http://someurl.com/page")
(def expected-greased-full-link "http://localhost:3000/convert?url=http%3A%2F%2Fsomeurl.com%2Fpage")

(def server "http://localhost")
(def port "3000")

(defn run []
  (assert (= (grease-the-link server port ungreased-full-link) expected-greased-full-link)))


