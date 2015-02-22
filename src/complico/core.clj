(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [complico.external :as external]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [clj-http.client :as client]
            [compojure.route :as route]
            [clojure.string :as string]
            [selmer.parser :as selmer]
            [hiccup.core :as hiccup]))

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

(defn create-host [server port]
  (if (= server "localhost") 
    (str "http://" server ":" port)
  (str "http://" server)))

(defn create-cljs-html [original-host complico-host]
  (hiccup/html
    [:script 
      {:id "complico_host_vars"
       :original_host_name original-host
       :complico_host_name complico-host
       :src (str complico-host "/js/complico-debug.js")}]))

(defn perform-search [search-term cookies server port]
  (let [search-url (external/get-search-url cookies search-term)
        complico-host (create-host server port)]
    (->> search-url
      (codec/url-encode)
      (str complico-host "/convert?url=")
      (response/redirect))))

(defn load-page [url ua]
  (request-url-page url ua))

(defn load-page-for-conversion [url ua server port]
  (let [original-page-html (load-page url ua)
        original-host (extract-host url)
        complico-host (create-host server port)
        prefix (create-base-html original-host)
        suffix (create-cljs-html original-host complico-host)]
    (str prefix original-page-html suffix)))

(defroutes my-handler
  (GET "/" [] 
    home-page)

  (GET "/search" {cookies :cookies params :query-params server :server-name port :server-port}
    (let [search-term (params "search-term")]
      (perform-search search-term cookies server port)))

  (GET "/convert" {params :query-params server :server-name port :server-port headers :headers}
    (let [url (params "url")
          ua (headers "user-agent")]
      (load-page-for-conversion url ua server port)))

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


