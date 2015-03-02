(ns complico.external
  (:require [clj-http.client :as client]))

(defn get-location 
  [url]
  (-> url
    (client/get)
    (:trace-redirects)
    (last)))

; make a http request to url and download the body as string
(defn request-url-page 
  [url user-agent] 
  (:body (client/get url {:headers {"user-agent" user-agent}})))
