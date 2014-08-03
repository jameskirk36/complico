(ns complico.core
  (use compojure.core)
  (use compojure.route)
)

(defroutes app
  (GET "/convert" [url] "<head><base href=\"http://www.host.com\"/></head>")
)
