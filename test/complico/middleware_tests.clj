(ns complico.middleware-tests
  (:use [clojure.test])
  (:require [complico.middleware :as middleware]))

(defn handler [req] req)

(deftest wrap-complico-host-should-add-portless-host-when-localhost
  (let [req  {:server-name "localhost" :server-port "80"}
        resp ((middleware/wrap-complico-host handler) req)]
    (is (= "http://localhost:80" (:complico-host resp)))))

(deftest wrap-complico-host-should-add-complico-host
  (let [req  {:server-name "compli.co" :server-port "80"}
        resp ((middleware/wrap-complico-host handler) req)]
    (is (= "http://compli.co" (:complico-host resp)))))
