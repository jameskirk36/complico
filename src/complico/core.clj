(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [complico.external :as external]
            [complico.utils :as utils]
            [complico.view :as view]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [compojure.route :as route]))


(defn perform-search [search-term cookies server port]
  (let [search-url (external/get-search-url cookies search-term)
        complico-host (utils/create-host server port)]
    (->> search-url
      (codec/url-encode)
      (str complico-host "/convert?url=")
      (response/redirect))))

(defn load-page-for-conversion [url ua server port]
  (let [original-page-html (external/request-url-page url ua)
        original-host (utils/extract-host url)
        complico-host (utils/create-host server port)
        prefix (view/create-base-html original-host)
        suffix (view/create-cljs-html original-host complico-host)]
    (str prefix original-page-html suffix)))

(defroutes my-handler
  (GET "/" [] 
    view/home-page)

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


