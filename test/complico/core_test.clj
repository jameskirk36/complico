(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(def server-name "localhost")
(def server-port "3000")
(def host "http://somelink.com/")

(deftest build-link-replacements
  (def ungreased-html 
    "<link href=\"duff.css\"/>
    <a href=\"http://anotherhost.com/duff\"/>
    <a href=\"duff\"/>
    <a href=\"/duff\"/>")

  (def expected-link-replacements 
    [["a href=\"http://anotherhost.com/duff\"" "a href=\"http://localhost:3000/convert?url=http://anotherhost.com/duff\""]
      ["a href=\"duff\""                        "a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\""]
      ["a href=\"/duff\""                       "a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\""]])

  (testing "Greasing the new links"
    (is (= (build-link-replacement-map ungreased-html server-name server-port host) expected-link-replacements))))

