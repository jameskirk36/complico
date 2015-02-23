(ns complico.external-tests
	(:use 
		[clojure.test])
   (:require
		[complico.external :as external]))

(deftest should-get-search-url-from-external-source
  (is (= (external/get-search-url {} "ebay") "http://www.ebay.co.uk/")))

