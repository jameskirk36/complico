(ns complico.external
  (:require [clj-http.client :as client]))

; url of the external search engine to use
(def external-search-url "https://www.google.co.uk/search?btnI=1&q=")

(defn- get-search-url-from-cookie [cookies]
  (get (cookies "test") :value))

(defn- build-search-url [search-term]
  (str external-search-url search-term))

(defn- get-redirected-location [url]
  (-> url
    (client/head)
    (:trace-redirects)
    (last)))

(defn get-search-url [cookies search-term]
  (if (contains? cookies "test") 
    (get-search-url-from-cookie cookies)
    (-> search-term 
      (build-search-url)
      (get-redirected-location))))

; make a http request to url and download the body as string
(defn request-url-page [url user-agent] 
  (:body (client/get url {:headers {"user-agent" user-agent}})))
