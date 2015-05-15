(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [complico.search :as search]
            [complico.convert :as convert]
            [complico.middleware :refer [wrap-complico-host]]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [compojure.route :as route]))

(defroutes my-handler
  (GET "/" 
    [] 
    (response/resource-response "index.html" {:root "public"}))

  (GET "/search" 
    {params :query-params complico-host :complico-host cookies :cookies}
    (-> (search/build-url-for-conversion params complico-host cookies)
      (response/redirect)))

  (GET "/convert" 
    {params :query-params complico-host :complico-host headers :headers}
    (convert/load-page-for-conversion params complico-host headers))

  (route/resources "/")
  (route/not-found "Page not found"))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)
    (wrap-cookies)
    (wrap-complico-host)))

; entry point for running on heroku
(defn -main 
  [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


