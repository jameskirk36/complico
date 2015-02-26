(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [complico.external :as external]
            [complico.search :as search]
            [complico.utils :as utils]
            [complico.view :as view]
            [complico.middleware :refer [wrap-complico-host]]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [compojure.route :as route]))

(defn- load-page-for-conversion [{url "url"} complico-host {ua "user-agent"}]
  (let [original-page-html (external/request-url-page url ua)
        original-host (utils/extract-host url)
        prefix (view/create-base-html original-host)
        suffix (view/create-cljs-html original-host complico-host)]
    (str prefix original-page-html suffix)))

(defroutes my-handler
  (GET "/" [] 
    view/home-page)

  (GET "/search" {params :query-params complico-host :complico-host cookies :cookies}
      (->> (search/get-url params complico-host cookies)
        (codec/url-encode)
        (str complico-host "/convert?url=")
        (response/redirect)))

  (GET "/convert" {params :query-params complico-host :complico-host headers :headers}
    (load-page-for-conversion params complico-host headers))

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


