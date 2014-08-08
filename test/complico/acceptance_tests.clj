(ns complico.acceptance-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]
      [ring.util.serve]))

(def test-url-with-full-link "http://localhost:3000/test_full_link")
(def test-url-with-relative-link "http://localhost:3000/test_relative_link")

(deftest convert
  ; start the server, headless
  (serve-headless my-handler)

  (testing "should add host url as base element to request body"
    (let [response (app (request :get "/convert" {:url test-url-with-full-link} ))]
      (is (= (:status response) 200))
      (is (= (.contains (:body response) 
			"<head><base href=\"http://localhost:3000/\"/></head>Test Page") true))))

  (testing "should greasemonkey the full links on the page"
    (let [response (app (request :get "/convert" {:url test-url-with-full-link} ))] 
      (is (= (.contains (:body response) 
        "<a href=\"http://localhost:80/convert?url=http://somelink.com/\">") true))))

  (testing "should greasemonkey the relateive links on the page"
    (let [response (app (request :get "/convert" {:url test-url-with-relative-link} ))] 
      (is (= (.contains (:body response) 
        "<a href=\"http://localhost:80/convert?url=http://localhost:3000/duff\">") true))))

  ;stop the ring server
  (stop-server))


