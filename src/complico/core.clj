(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [clj-http.client :as client]
            [compojure.route :as route]
            [clojure.string :as string]
            [selmer.parser :as selmer]))

; url of the external search engine to use
(def external-search-url "http://www.bing.com/search?q=")

(def home-page 
  (selmer/render-file "templates/index.html" {}))

; create base html
(defn create-base-html [url]
  (str "<head><base href=\"" url "\"/></head>"))

; extract just the host from a URI
(defn extract-host [url]
  (str  
    (string/join "/" 
      (take 3 
        (string/split url #"/"))) "/"))

; make a http request to url and download the body as string
(defn request-url-page [url user-agent] 
  (:body (client/get url {:headers {"user-agent" user-agent}})))

(defn create-grease [host]
  (str host "/convert?url="))

(defn create-host [server port]
  (str "//" server ":" port))

(defn create-script-html [original-host complico-host]
  (selmer/render-file "templates/script.html"
    {:complico-host complico-host 
     :original-host original-host}))

(defn mock-search-results-page []
  (str "<body><a id='test_page_with_links' href='http://localhost:3000/test_page_with_links.html'>Test Link</a></body>"
   (create-script-html "" "http://localhost:3000")))

(defroutes my-handler
  (GET "/" [] 
    home-page)

  (GET "/search" {cookies :cookies params :query-params server :server-name port :server-port}
    (if (contains? cookies "test") (mock-search-results-page)
       (let [search-term (params "search-term")
          grease (create-grease (create-host server port))]
            (response/redirect (str grease (codec/url-encode (str external-search-url search-term)))))))

  (GET "/convert" {params :query-params server :server-name port :server-port headers :headers}
    (let [url (params "url")
          original-host (extract-host url)
          base-element (create-base-html original-host)
          original-page-html (request-url-page url (headers "user-agent"))
          complico-host (create-host server port)
          script-html (create-script-html original-host complico-host)]
      (str base-element original-page-html script-html)))
  (route/resources "/")
  (route/not-found "Page not found"))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)
    (wrap-cookies)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


