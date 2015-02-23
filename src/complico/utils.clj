(ns complico.utils
  (:require [clojure.string :as string]))

(defn extract-host [url]
  (str  
    (string/join "/" 
      (take 3 
        (string/split url #"/"))) "/"))
