(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (use ring.middleware.params)
  (:require [clj-http.client :as client])
  (:require [clojure.string :as string])
  (:require [ring.adapter.jetty :as jetty]))

; extract just the host from a URI
(defn extract-host [url]
  (str  
    (string/join "/" 
      (take 3 
        (string/split url #"/"))) "/")) 

; create base html
(defn create-base-html [url]
  (str "<head><base href=\"" url \""/></head>"))

; make a http request to url and download the body as string
(defn read-request-body [url] 
  (:body (client/get url)))

; prefix all the links in the body with host url convert link
(defn grease-the-links [body host port]
  (string/replace body #"a href=\"(http)"
    #(str "a href=\"http://" host ":" port "/convert?url=" (%1 1))))

(defroutes my-handler
  (GET "/" [] "Welcome")
  ; route for testing only
  (GET "/test_full_link" [] "Test Page<a href=\"http://somelink.com/\">")
  (GET "/test_relative_link" [] "Test Page<a href=\"/\">")
  (GET "/convert" {params :query-params host :server-name port :server-port} 
    (let [url (params "url")] 
      (str 
        (-> url
          extract-host
          create-base-html)
        (-> url
          read-request-body
          (grease-the-links host port))
        ))))

(def app
  (wrap-params my-handler))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))
