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

(defn setting-text-changes-text-on-elem [] 
  (-> (select-single-elem)
      (dommy/set-text! "some text")
      (dommy/text)
      (confirm-text-was-set-to "some text")))

(defn run []
  (correctly-finds-the-elems)
  (setting-text-changes-text-on-elem))
