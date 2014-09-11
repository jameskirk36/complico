(ns complico.core
  (use compojure.core)
  (use ring.middleware.params)
  (use ring.middleware.cookies)
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.util.codec :as codec]
            [clj-http.client :as client]
            [compojure.route :as route]
            [clojure.string :as string]))

; url of the external search engine to use
(def external-search-url "http://www.bing.com/search?q=")

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
  (GET "/" [] 
    "<form method='get' action='/search'>
       <input id='search' type='text' name='search-term'></input>
       <button type='submit'>Go</input>
     </form>")

  (GET "/search" {cookies :cookies params :query-params server :server-name port :server-port}
    (let [search-term (params "search-term")
          grease (create-grease server port)]
      (cond 
        (contains? cookies "test") "<a id='test_page_with_links' href='http://localhost:3000/convert?url=http%3A%2F%2Flocalhost%3A3000%2Ftest_page_with_links.html'>Test Link</a>"
        :else (response/redirect (str grease (codec/url-encode (str external-search-url search-term)))))))

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
    (wrap-params)
    (wrap-cookies)))

; entry point for running on heroku
(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port) :join? false}))


