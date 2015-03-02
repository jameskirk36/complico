(ns complico.convert
  (:require [complico.external :as external]
            [complico.view :as view]
            [complico.utils :as utils]))

(defn load-page-for-conversion 
  [{url "url"} complico-host {ua "user-agent"}]
  (let [original-page-html (external/request-url-page url ua)
        original-host (utils/extract-host url)
        title (view/create-title-html)
        base (view/create-base-html original-host)
        cljs (view/create-cljs-html original-host complico-host)]
    (str title base original-page-html cljs)))
