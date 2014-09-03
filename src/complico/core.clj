(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (:require [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]
            [compojure.route :as route]))

; make a http request to url and download the body as string
(defn request-url-page [url] 
  (:body (client/get url)))

(defn create-js [server port]
  (str "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>
  <script type=\"text/javascript\">
  $(function(){
    var greaseLinks = function(){ 
      var foundin = $('a').each(function(index){
        var link = $(this).attr('href');
        var newLink = 'http://" server ":" port "/convert?url=' + encodeURIComponent(link);
        $(this).attr('href', newLink); 
      });
    };
    var replacePrices = function(){
      var found = $('div').each(function(index){
        $(this).text('£XXX');
      });
    };
    greaseLinks();
    replacePrices();
   }); 
  </script>"))

(defroutes my-handler
  (GET "/convert" {params :query-params server :server-name port :server-port}
    (str (request-url-page (params "url")) (create-js server port)))
  (route/resources "/")
  (route/not-found "Page not found"))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


