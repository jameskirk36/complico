(ns complico.test.dom
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.dom :as dom]))

(def body js/document.body)

(defn correctly-finds-the-elems []
  (let [elems (dom/find-elems "div,span")]
    (assert (= 4 (count elems)))))

(defn select-single-elem []
  (sel1 :#parent-elem))

(defn confirm-text-was-set-to [actual-text expected-text]
  (assert (= actual-text expected-text)))

(defn setting-text-on-elem-changes-text [] 
  (-> (select-single-elem)
      (replace-text! "modified text")
      (dommy/text)
      (confirm-text-was-set-to "modified text")))

(defn select-single-elem-with-children [] 
  (sel1 :#parent-elem))

(defn confirm-text-remains [actual-text expected-text]
  (assert (= actual-text expected-text)))

(defn setting-text-on-elem-does-not-alter-child-elem-text []
  (-> (select-single-elem-with-children)
      (replace-text! "modified text")
      (.-firstChild)
      (dommy/text)    
      (confirm-text-remains "original text")))

(defn run []
  (correctly-finds-the-elems)
  (setting-text-on-elem-changes-text)
  (setting-text-on-elem-does-not-alter-child-elem-text))
