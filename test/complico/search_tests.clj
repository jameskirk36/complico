(ns complico.search-tests
	(:use 
		[clojure.test])
   (:require
		[complico.search :as search]))

(deftest should-get-external-location-integration-test
  (is (= "http://www.ebay.co.uk/" (search/get-external-location "ebay"))))

(deftest should-build-url-for-conversion
  (let [params {"search-term" "notused"}
        complico-host "http://localhost:3000"
        cookies {"test" :true}
        expected-url "http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_page_search_results.html"]
    (is (= expected-url (search/build-url-for-conversion params complico-host cookies)))))

