(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (:require [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [compojure.route :as route]
            [clojure.string :as string]))

; extract just the host from a URI
(defn extract-host [url]
  (str  
    (string/join "/" 
      (take 3 
        (string/split url #"/"))) "/"))

; make a http request to url and download the body as string
(defn request-url-page [url] 
  (:body (client/get url)))

(defn create-grease [server port]
  (str "http://" server ":" port "/convert?url="))

(defn create-js [host grease]
  (str "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>
  <script src=\"js/complico-debug.js\"></script>
  <script type=\"text/javascript\">
  $(function(){
    var greaseLinks = function(){ 
      var foundin = $('a').each(function(index){
        var link = $(this).attr('href');
        if(link !== undefined){
          var newLink = complico.core.grease_the_link('" host "', '" grease "', link);
          $(this).attr('href', newLink); 
        }
      });
    };
    var replacePrices = function(){
      var found = $('*:not(:has(*)):contains(\"\u00A3\")').each(function(index){
        $(this).text('\u00A3XXX');
      });
    };
    greaseLinks();
    replacePrices();
   }); 
  </script>"))

(defroutes my-handler
  (GET "/convert" {params :query-params server :server-name port :server-port}
    (let [url (params "url")
          host (extract-host url)
          grease (create-grease server port)]
      (str (request-url-page url) (create-js host grease))))
  (route/resources "/")
  (route/not-found "Page not found"))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


