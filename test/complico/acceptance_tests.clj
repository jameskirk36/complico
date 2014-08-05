(ns complico.acceptance-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]
      [ring.util.serve]))

(deftest convert
  ; start the server, headless
  (serve-headless my-handler)

  (testing "should add host url as base element to request body"
    (let [response (app (request :get "/convert" {:url "http://localhost:3000/test"} ))]
      (is (= (:status response) 200))
		;(println (:body response))
      (is (= (.contains (:body response) 
			"<head><base href=\"http://localhost:3000/\"/></head>Test Page") true))))

  (testing "should greasemonkey the links on the page"
    (let [response (app (request :get "/convert" {:url "http://localhost:3000/test"} ))] 
      (is (= (.contains (:body response) 
        "<a href=\"http://localhost:3000/convert?url=http://somelink.com/\">") true))))

  ;stop the ring server
  (stop-server))


