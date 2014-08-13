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

; find a sequence of anchor links in a given body
(defn find-links [body]
  (re-seq #"a href=\"(.+?)\"" body))

; create the prefix to add to all anchor links
(defn create-grease [server port]
  (str "a href=\"http://" server ":" port "/convert?url="))

; apply grease to different kinds of links
(defn grease-the-link [link grease host]
  (cond
    (.startsWith link "http") (str grease link "\"")
    (.startsWith link "/") (str grease (apply str (drop-last 1 host)) link "\"") 
    :else (str grease host link "\""))) 

; replace all the occurances of a find-replace map in a string
(defn replace-all-in-string [string replacement-map]
  (str
    (reduce 
      #(apply string/replace %1 %2)
      string
      replacement-map
     )))


; build a map of links and their greased replacements
(defn build-greased-link-map [links grease host]
  (map 
    #(vector 
       (first %)
       (grease-the-link 
         (last %)
         grease
         host))
       links))

; find all links and build a map of links and their greased replacement
(defn build-link-replacement-map [body server port host]
  (let [links (find-links body)
        grease (create-grease server port)]
        (build-greased-link-map links grease host)))

(defroutes my-handler
  (GET "/" [] "Welcome")
  ; routes for testing only
  (GET "/test_full_link" [] "Test Page<a href=\"http://somelink.com/\">")
  (GET "/test_relative_link" [] "Test Page<a href=\"/duff\">")
  ; entry point for complicos complicated prices!
  (GET "/convert" {params :query-params server :server-name port :server-port} 
    (let [url (params "url")
          host (extract-host url)] 
      (str 
        (create-base-html host)
        (let [body (read-request-body url)
              host (extract-host url)]
          (let [replacement-link-map (build-link-replacement-map body server port host)]
            (replace-all-in-string body replacement-link-map)))))))

; needed to gain access to query parameters in my-handler
(def app
  (wrap-params my-handler))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))
