(ns complico.utils
  (:require [clojure.string :as string]))

(defn extract-host [url]
  (str  
    (string/join "/" 
      (take 3 
        (string/split url #"/"))) "/"))

(defn create-host [server port]
  (if (= server "localhost") 
    (str "http://" server ":" port)
  (str "http://" server)))

