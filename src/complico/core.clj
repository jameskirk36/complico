(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (use ring.middleware.params)
)

(defroutes my-handler
  (GET "/" [] "Welcome")
  (GET "/convert" {params :query-params} 
    (str "<head><base href=\"" (params "url")  \""/></head>")))

(def app
  (wrap-params my-handler))
