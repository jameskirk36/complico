(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (:require [ring.adapter.jetty :as jetty]))

(def link-to-second-page 
  "<a id=\"second_page\" href=\"http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Fsecond_test_page\">Second page link</a>")

(defroutes my-handler
  (GET "/first_test_page" [] 
    link-to-second-page)
  (GET "/convert" []
    "<title>Second test page</title>"))

; needed to gain access to query parameters in my-handler
(def app my-handler)

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


