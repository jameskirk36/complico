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

; find a sequence of anchor/area links in a given body
(defn find-links [body]
  (re-seq #"<(a.*?)href=\"(.+?)\"" body))

; create the prefix to add to all links
(defn create-grease [server port]
  (str "http://" server ":" port "/convert?url="))

; apply grease to different kinds of links
(defn grease-the-link [element link grease host]
  (cond
    (.startsWith link "http") (str "<" element "href=\"" grease link "\"")
    (.startsWith link "/") (str "<" element "href=\"" grease (apply str (drop-last 1 host)) link "\"") 
    :else (str "<" element "href=\"" grease host link "\""))) 

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
         (second %)
         (last %)
         grease
         host))
    links))

; find all links and build a map of links and their greased replacement
(defn build-link-replacement-map [body server port host]
  (let [links (find-links body)
        grease (create-grease server port)]
        (build-greased-link-map links grease host)))

(defn find-prices [body]
  (re-seq #"£(\d+\.?\d*)" body))


(defn convert-price [price]
  "£XXX")

(defn convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (last %)))
    prices))
  

(defn build-price-replacement-map [body]
  (let [prices (find-prices body)]
    (convert-prices prices)))

(defroutes my-handler
  (GET "/" [] "Welcome")
  ; routes for testing only
  (GET "/test_full_link" [] "Test Page<a href=\"http://somelink.com/\">")
  (GET "/test_relative_link" [] "Test Page<a href=\"/duff\">")
  (GET "/test_prices" [] "Test Page £2 £3.00 £NOTTHIS")
  ; entry point for complicos complicated prices!
  (GET "/convert" {params :query-params server :server-name port :server-port} 
    (let [url (params "url")
          host (extract-host url)] 
      (str 
        (create-base-html host)
        (let [body (read-request-body url)
              host (extract-host url)]
          (let [replacement-link-map (build-link-replacement-map body server port host)
                replacement-prices-map (build-price-replacement-map body)]
            (replace-all-in-string body (into [] (concat replacement-link-map replacement-prices-map)))))))))

; needed to gain access to query parameters in my-handler
(def app
  (wrap-params my-handler))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))
