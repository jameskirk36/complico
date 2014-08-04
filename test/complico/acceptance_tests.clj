(ns complico.acceptance-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]))

(deftest convert
  (testing "convert should add url as base element"
    (let [response (app (request :get "/convert" {:url "http://www.host.com/page.html"} ))]
      (is (= (:status response) 200))
		(println (:body response))
      (is (= (.contains (:body response) "<head><base href=\"http://www.host.com/\"/></head>") true))))
)

