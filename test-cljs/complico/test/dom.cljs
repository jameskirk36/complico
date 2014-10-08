(ns complico.test.dom
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.dom :as complico]))

(defn correctly-finds-the-elems []
  (let [root-elem (node
                [:body
                  [:div
                    [:a]
                    [:div]]
                  [:p
                    [:span]]])
        found-elems (complico/find-elems root-elem "div,span")]
    (assert (= 3 (count found-elems)))))

(defn confirm-text-was-set-to [actual-text expected-text]
  (assert (= actual-text expected-text)))

(defn confirm-text-remains [actual-text expected-text]
  (assert (= actual-text expected-text)))


(defn setting-text-on-elem-changes-text [] 
    (-> (node 
          [:div#parent "original text"
            [:div#child "original text"]])
      (complico/replace-text! "modified text")
      (complico/get-text-from-node)
      (confirm-text-was-set-to "modified text")))
     
(defn setting-text-on-elem-does-not-alter-child-elem-text []
    (-> (node 
          [:div#parent "original text"
            [:div#child "original text"]])
      (complico/replace-text! "modified text")
      (.-lastChild)
      (complico/get-text-from-node)
      (confirm-text-remains "original text")))

(defn run []
  (correctly-finds-the-elems)
  (setting-text-on-elem-changes-text)
  (setting-text-on-elem-does-not-alter-child-elem-text))
