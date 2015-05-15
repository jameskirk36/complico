(ns complico.search
  (:require [complico.external :as external]
            [ring.util.codec :as codec]))

; url of the external search engine to use
(def ^:private external-search-url "https://www.google.co.uk/search?btnI=1&q=")
(def ^:private test-location "/test_page_search_results.html")

(defn- get-test-location 
  [host]
  (str host test-location))

(defn get-external-location 
  [search-term]
  (->> search-term 
    (str external-search-url)
    (external/get-location)))

(defn- get-search-url 
  [search-term host is-test]
  (if (not (nil? is-test))
    (str host test-location)
    (get-external-location search-term)))

(defn build-url-for-conversion
  [{search-term "search-term"} host {is-test "test"}]
  (->> (get-search-url search-term host is-test)
       (codec/url-encode)
       (str host "/convert?url=")))
