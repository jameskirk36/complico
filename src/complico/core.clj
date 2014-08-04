(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (use ring.middleware.params)
  (use [clojure.string :only [join split]])
  (:require [clj-http.client :as client])
)

; extract just the host from a URI
(defn extract-host [url]
  (str  
    (join "/" 
      (take 3 
        (split url #"/"))) "/")) 

; create base html
(defn create-base-html [url]
  (str "<head><base href=\"" url \""/></head>"))

(defn read-request-body [url] 
  (:body (client/get url)))

(defroutes my-handler
  (GET "/" [] "Welcome")
  (GET "/test" [] "Test Page")
  (GET "/convert" {params :query-params} 
    (let [url (params "url")] 
      (str 
        (-> url
          extract-host
          create-base-html)
        (read-request-body url)))))

(def app
  (wrap-params my-handler))
