(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [complico.external :as external]
            [complico.utils :as utils]
            [complico.view :as view]
            [complico.middleware :refer [wrap-complico-host]]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [compojure.route :as route]))


(defn perform-search [search-term cookies complico-host]
  (let [search-url (external/get-search-url cookies search-term)]
    (->> search-url
      (codec/url-encode)
      (str complico-host "/convert?url=")
      (response/redirect))))

(defn load-page-for-conversion [url ua complico-host]
  (let [original-page-html (external/request-url-page url ua)
        original-host (utils/extract-host url)
        prefix (view/create-base-html original-host)
        suffix (view/create-cljs-html original-host complico-host)]
    (str prefix original-page-html suffix)))

(defroutes my-handler
  (GET "/" [] 
    view/home-page)

  (GET "/search" {params :query-params complico-host :complico-host cookies :cookies}
    (let [search-term (params "search-term")]
      (perform-search search-term cookies complico-host)))

  (GET "/convert" {params :query-params complico-host :complico-host headers :headers}
    (let [url (params "url")
          ua (headers "user-agent")]
      (load-page-for-conversion url ua complico-host)))

  (route/resources "/")
  (route/not-found "Page not found"))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)
    (wrap-cookies)
    (wrap-complico-host)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


