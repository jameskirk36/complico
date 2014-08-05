(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(deftest greasing-the-links
  (testing "Greasing the links"
    (is (= (grease-the-links "href=\"http://somelink.com/\"" ) "href=\"http://localhost:3000/convert?url=http://somelink.com/\""))))
