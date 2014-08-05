(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(deftest greasing-the-links
  (testing "Greasing the links"
    (is (= (grease-the-links "a href=\"http://somelink.com/\"" ) "a href=\"http://localhost:3000/convert?url=http://somelink.com/\""))))
