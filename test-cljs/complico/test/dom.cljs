(ns complico.test.dom
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.dom :as dom]
   [hipo.interpreter :as hipo]
   [complico.dom-helper :as dom-helper]
   [cemerick.cljs.test :as t]))

(defn confirm-text-was-set-to [actual-text expected-text]
  (is (= actual-text expected-text)))

(deftest should-hide-existing-form-element
  (let [root-elem (hipo/create [:body [:form]])]
    (dom/hide-existing-form-element! root-elem)
    (-> (sel1 root-elem :form)
      (dommy/attr "style")
      (confirm-text-was-set-to "display: none;"))))
      
