(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (:require [ring.adapter.jetty :as jetty]))

(def link-to-prices-page 
  "<a id=\"test_page_with_prices\" href=\"http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_page_with_prices\">Show prices</a>")

(defroutes my-handler
  (GET "/test_start_page" [] 
    link-to-prices-page)
  (GET "/convert" []
    "<div id=\"price\">£XXX</div>"))

; needed to gain access to query parameters in my-handler
(def app my-handler)

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


