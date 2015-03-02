(ns complico.middleware)

(defn- create-host 
  [server port]
  (if (= server "localhost") 
    (str "http://" server ":" port)
  (str "http://" server)))

(defn wrap-complico-host 
  [handler]
  (fn [req] 
    (let [server (:server-name req)
          port (:server-port req)]
      (handler (assoc req :complico-host (create-host server port))))))

