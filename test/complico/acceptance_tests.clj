(ns complico.acceptance-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]
      [ring.util.serve]))

(deftest convert
  (testing "convert should add url as base element"
    (serve my-handler)
    (let [response (app (request :get "/convert" {:url "http://localhost:3000/test"} ))]
      (is (= (:status response) 200))
		;(println (:body response))
      (is (= (.contains (:body response) 
			"<head><base href=\"http://localhost:3000/\"/></head>Test Page") true)))
    (stop-server))
)

