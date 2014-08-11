(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(deftest greasing-the-full-links
  (testing "Greasing the fully qualified links"
    (is (= (grease-the-full-links "a href=\"http://somelink.com/\"" "localhost" "3000" ) "a href=\"http://localhost:3000/convert?url=http://somelink.com/\""))))

(deftest greasing-the-relative-links
  (testing "Greasing the relative links"
    (is (= (grease-the-relative-links "a href=\"/en\"" "localhost" "3000" "http://somelink.com/") "a href=\"http://localhost:3000/convert?url=http://somelink.com/en\""))))

;(def server-name "localhost")
;(def server-port "3000")
;(def host "http://somelink.com/")
;
;(deftest greasing-the-links-new
;  (def ungreased-html 
;    "<link href=\"duff.css\"/>
;    <a href=\"http://anotherhost.com/duff\"/>
;    <a href=\"duff\"/>
;    <a href=\"/duff\"/>")
;
;  (def greased-html 
;    "<link href=\"duff.css\"/>
;    <a href=\"http://localhost:3000/convert?url=http://anotherhost.com/duff\"/>
;    <a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\"/>
;    <a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\"/>")
;
;  (testing "Greasing the new links"
;    (is (= (grease-the-links-new ungreased-html server-name server-port host) greased-html))))

