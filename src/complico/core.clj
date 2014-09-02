(ns complico.core
  (use compojure.core)
  (use compojure.route)
  (use ring.middleware.params)
  (:require [ring.adapter.jetty :as jetty]
            [clj-http.client :as client]))

; make a http request to url and download the body as string
(defn make-get-request [url] 
  (:body (client/get url)))

(def link-greasing-js 
"<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>
<script type=\"text/javascript\">
$(function(){

  var foundin = $('a').each(function(index){
  var link = $(this).attr('href');
  var newLink = 'http://localhost:3000/convert?url=' + link;
  $(this).attr('href', newLink); 
});
});
</script>")

(def start-page-html 
  "<a id=\"test_page_with_prices\" href=\"http://localhost:3000/test_page_with_prices\">Show prices</a>")
(def price-page-html
 "<div id=\"price\">£XXX</div>")

(defroutes my-handler
  (GET "/test_start_page" [] 
    start-page-html)
  (GET "/test_page_with_prices" []
    price-page-html)
  (GET "/convert" {params :query-params}
    (str (make-get-request (params "url")) link-greasing-js)))

; needed to gain access to query parameters in my-handler
(def app 
  (-> my-handler
    (wrap-params)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


