(ns complico.ring-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]
      [ring.util.serve]))

(def test-url "http://localhost:3000/test_page_with_links.html")
(def expected-base-html "<head><base href=\"http://localhost:3000/\" /></head><a id=\"test_page_with_prices\"")

(deftest ring-tests
  ; start the server, headless
  (serve-headless my-handler)

  ; sadly clj-webdriver doesnt detect the base element, hence why we have to test it here
  (testing "should add base element to request body to preserve style"
    (let [response (app (request :get "/convert" {:url test-url} ))]
      (is (= (:status response) 200))
      (is (.contains 
              (:body response) 
              expected-base-html))))
  ;stop the ring server
  (stop-server))


