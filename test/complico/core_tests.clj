(ns complico.core-tests
	(:use 
		[clojure.test])
   (:require
		[complico.core :as complico]))

(deftest create-host-should-combine-host-and-port-when-localhost
  (is (= (complico/create-host "localhost" "80") "//localhost:80")))

(deftest create-host-should-not-add-port-when-not-localhost 
  (is (= (complico/create-host "compli.co" "80") "//compli.co")))

