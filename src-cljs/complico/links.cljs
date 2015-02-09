(ns complico.links
  (:require 
    [dommy.utils :as utils]
    [dommy.core :as dommy]
    [dommy.attrs :as attrs])

  (:use-macros
    [dommy.macros :only [node sel sel1]]))

(defn- is-full-link [link] 
  (= (subs link 0 4) "http"))

(defn- is-relative-link [link]
  (= (subs link 0 1) "/"))

(defn- host-minus-trailing-slash [host]
  (apply str (drop-last 1 host)))

(defn grease-the-link [host grease link]
  (str grease 
    (js/encodeURIComponent
      (cond
        (is-full-link link) link
        (is-relative-link link) (str (host-minus-trailing-slash host) link)
        :else (str host link)))))

(defn find-links [elem]
  (sel elem "a,area"))

(defn replace-the-links! [root-elem complico-host original-host]
  (doseq [elem (find-links root-elem)]
    (if-let [initial-link (attrs/attr elem :href)]
        (let [grease (str complico-host "/convert?url=")
              new-link (grease-the-link original-host grease initial-link)]
             (attrs/set-attr! elem :href new-link)))))

