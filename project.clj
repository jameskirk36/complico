(defproject complico "0.1.0-SNAPSHOT"
  :description "Complico"
  :url "http://www.example.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src"]
  :dependencies [
                  [org.clojure/clojure "1.6.0"]
                  [ring/ring-core "1.1.8"]
                  [ring/ring-jetty-adapter "1.1.8"]
                  [compojure "1.1.5"]
                  [clj-http "0.9.2"]
						[clj-webdriver "0.6.1"]
                  [org.clojure/clojurescript "0.0-2322"]]
  
  :uberjar-name "complico.jar"
  :plugins  [[lein-ring "0.8.3"]
             [lein-cljsbuild "1.0.3"]]
  :ring  {:handler complico.core/app}
  :hooks [leiningen.cljsbuild]
  ; clojurescript settings
  :cljsbuild {
    :test-commands
      ; Test command for running the unit tests in "test-cljs" (see below).
      ;     $ lein cljsbuild test
      {"unit" ["phantomjs"
               "phantom/unit-test.js"
               "resources/private/html/unit-test.html"]}
    :builds {
      ; This build has the lowest level of optimizations, so it is
      ; useful when debugging the app.
      :dev
      {:source-paths ["src-cljs"]
       :jar true
       :compiler {:output-to "resources/public/js/complico-debug.js"
                  :optimizations :whitespace
                  :pretty-print true}}
      ; This build has the highest level of optimizations, so it is
      ; efficient when running the app in production.
      :prod
      {:source-paths ["src-cljs"]
       :compiler {:output-to "resources/public/js/complico.js"
                  :optimizations :advanced
                  :pretty-print false}}
      ; This build is for the ClojureScript unit tests that will
      ; be run via PhantomJS.  See the phantom/unit-test.js file
      ; for details on how it's run.
      :test
      {:source-paths ["src-cljs" "test-cljs"]
       :compiler {:output-to "resources/private/js/unit-test.js"
                  :optimizations :whitespace
                  :pretty-print true}}}}
  :min-lein-version "2.0.0")
