(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(deftest greasing-the-full-links
  (testing "Greasing the fully qualified links"
    (is (= (grease-the-full-links "a href=\"http://somelink.com/\"" "localhost" "3000" ) "a href=\"http://localhost:3000/convert?url=http://somelink.com/\""))))

(deftest greasing-the-relative-links
  (testing "Greasing the relative links"
    (is (= (grease-the-relative-links "a href=\"/en\"" "localhost" "3000" "http://somelink.com/") "a href=\"http://localhost:3000/convert?url=http://somelink.com/en\""))))
