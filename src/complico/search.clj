(ns complico.search
  (:require [complico.external :as external]))

; url of the external search engine to use
(def ^:private external-search-url "https://www.google.co.uk/search?btnI=1&q=")
(def ^:private test-location "/test_page_search_results.html")

(defn- get-test-location [host]
  (str host test-location))

(defn- build-search-url [search-term]
  (str external-search-url search-term))

(defn get-external-location [search-term]
  (->> search-term 
    (build-search-url)
    (external/get-location)))

(defn get-url [search-term host is-test]
  (if is-test
    (get-test-location host)
    (get-external-location search-term)))
