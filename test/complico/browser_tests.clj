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

(def first-page "http://localhost:3000/first_test_page")

(deftest user-can-follow-greased-anchor-links
  (to first-page)
  (click "a#second_page")
  (is (= (title) "Second test page" )))

