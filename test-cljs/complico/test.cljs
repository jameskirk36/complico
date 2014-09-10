(ns complico.test
  (:require [complico.test.core :as core]))

(def success 0)

(defn ^:export run []
  (.log js/console "Complico clojurescript unit tests started")
  (core/run)
  success)
