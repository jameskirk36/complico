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

(def first-page "http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_start_page")
(def link-to-prices-page "a#test_page_with_prices")
(def expected-price "£XXX")

(defn extract-price-from-page []
  (text (element "div#price")))

(deftest user-follows-link-sees-converted-prices
  (to first-page)
  (click link-to-prices-page)
  (is (= (extract-price-from-page) expected-price)))


