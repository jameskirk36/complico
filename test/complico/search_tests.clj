(ns complico.search-tests
	(:use 
		[clojure.test])
   (:require
		[complico.search :as search]))

(deftest should-get-external-location-smoke-test
  (is (= "http://www.ebay.co.uk/" (search/get-external-location "ebay"))))

