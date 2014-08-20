(ns complico.acceptance-tests
	(:use 
		[clojure.test]
		[ring.mock.request]
		[complico.core]
      [ring.util.serve]))

(def test-url-with-full-link "http://localhost:3000/test_full_link")

(def test-url-with-relative-link "http://localhost:3000/test_relative_link")

(def test-url-with-prices "http://localhost:3000/test_prices")

(def test-url-with-form "http://localhost:3000/test_form")

(def expected-base-html "<head><base href=\"http://localhost:3000/\"/></head>Test Page")

(def expected-full-link "<a href=\"http://localhost:80/convert?url=http://somelink.com/\">")

(def expected-relative-link "<a href=\"http://localhost:80/convert?url=http://localhost:3000/duff\">")

(def expected-prices "£XXX £XXX £NOTTHIS")

(def expected-form 
"<form class=\"\" action=\"http://localhost:3000/convert\">
<input type=\"hidden\" name=\"url\" value=\"http://anotherhost.com/duff?test=test\"/>
<input name=\"name\" value=\"value\"/>
</form>")

(deftest convert
  ; start the server, headless
  (serve-headless my-handler)

  (testing "should add host url as base element to request body"
    (let [response (app (request :get "/convert" {:url test-url-with-full-link} ))]
      (is (= (:status response) 200))
      (is (.contains 
              (:body response) 
              expected-base-html))))

  (testing "should greasemonkey the full links on the page"
    (let [response (app (request :get "/convert" {:url test-url-with-full-link} ))] 
      (is (.contains 
            (:body response) 
            expected-full-link))))

  (testing "should greasemonkey the relateive links on the page"
    (let [response (app (request :get "/convert" {:url test-url-with-relative-link} ))] 
      (is (.contains 
            (:body response) 
            expected-relative-link))))

  (testing "should complicate the prices on the page"
    (let [response (app (request :get "/convert" {:url test-url-with-prices} ))] 
      (is (.contains 
            (:body response) 
            expected-prices))))

  (testing "should greasemonkey the forms"
    (let [response (app (request :get "/convert" {:url test-url-with-form} ))] 
      (is (.contains 
            (:body response) 
            expected-form))))

  ;stop the ring server
  (stop-server))


