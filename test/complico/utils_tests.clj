(ns complico.utils-tests
	(:use 
		[clojure.test])
   (:require
		[complico.utils :as utils]))

(deftest create-host-should-combine-host-and-port-when-localhost
  (is (= (utils/create-host "localhost" "80") "http://localhost:80")))

(deftest create-host-should-not-add-port-when-not-localhost 
  (is (= (utils/create-host "compli.co" "80") "http://compli.co")))
