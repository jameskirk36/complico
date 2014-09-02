(defproject complico "0.1.0-SNAPSHOT"
  :description "Complico"
  :url "http://www.example.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                  [org.clojure/clojure "1.5.1"]
                  [ring/ring-core "1.1.8"]
                  [ring/ring-jetty-adapter "1.1.8"]
                  [compojure "1.1.5"]
                  [clj-http "0.9.2"]
						[clj-webdriver "0.6.1"]]
  
  :uberjar-name "complico.jar"
  :plugins  [[lein-ring "0.8.3"]]
  :ring  {:handler complico.core/app}
  :min-lein-version "2.0.0")
