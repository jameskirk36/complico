(ns complico.core
  (use compojure.core)
  (use compojure.route)
)

(defroutes app
  (GET "/" [] "Hello World")
  (resources "/")
)
