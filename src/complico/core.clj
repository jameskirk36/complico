(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (use ring.middleware.params)
  (use [clojure.string :only [join split]])
)

; extract just the host from a URI
(defn extract-host [url]
  (join "" 
    [(join "/" 
      (take 3 
        (split url #"/"))) "/"])) 

(defroutes my-handler
  (GET "/" [] "Welcome")
  (GET "/convert" {params :query-params} 
    (str "<head><base href=\"" (extract-host (params "url"))  \""/></head>")))

(def app
  (wrap-params my-handler))
