(ns complico.browser-tests 
  (:use [clj-webdriver.taxi]
        [clojure.test])
  (:require [complico.core :as core]
            [ring.adapter.jetty :as jetty]))


(defonce server (jetty/run-jetty #'core/app {:port 3000 :join? false}))

(defn browser-up []
  (set-driver! {:browser :firefox}))

(defn browser-down [] 
  (quit))

(defn setup-teardown-fixtures [f]
  (.start server)
  (browser-up)
  (f)
  (browser-down)
  (.stop server))

(use-fixtures :once setup-teardown-fixtures)

(def test-page-with-prices "http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_page_with_prices")
(def test-page-with-links "http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_page_with_links")
(def expected-price "£XXX")
(def expected-link "http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_link")

(defn extract-price-from-page []
  (text (element "div#price")))
(defn extract-link-from-page []
  (attribute "a#test_link" :href))

(deftest user-should-see-converted-prices-when-visiting-pages
  (to test-page-with-prices)
  (is (= (extract-price-from-page) expected-price)))

(deftest links-should-be-greased-when-visiting-pages
  (to test-page-with-links)
  (is (= (extract-link-from-page) expected-link)))

