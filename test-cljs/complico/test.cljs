(ns complico.test
  (:require [complico.test.core :as core]
            [complico.test.dom :as dom]))

(def success 0)

(defn ^:export run []
  (.log js/console "Complico clojurescript unit tests started")
  (core/run)
  (dom/run)
  success)
