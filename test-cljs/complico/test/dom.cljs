(ns complico.test.dom
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.dom :as complico]
   [cemerick.cljs.test :as t]))

(deftest correctly-finds-the-elems 
  (let [root-elem (node
                [:body
                  [:div
                    [:a]
                    [:div]]
                  [:p
                    [:span]]])
        found-elems (complico/find-elems root-elem "div,span")]
    (is (= 3 (count found-elems)))))

(defn confirm-text-was-set-to [actual-text expected-text]
  (is (= actual-text expected-text)))

(defn confirm-text-remains [actual-text expected-text]
  (is (= actual-text expected-text)))


(deftest setting-text-on-elem-changes-text
    (-> (node 
          [:div#parent "original text"
            [:div#child "original text"]])
      (complico/replace-text! "modified text")
      (complico/get-text-from-node)
      (confirm-text-was-set-to "modified text")))
     
(deftest setting-text-on-elem-does-not-alter-child-elem-text 
    (-> (node 
          [:div#parent "original text"
            [:div#child "original text"]])
      (complico/replace-text! "modified text")
      (.-lastChild)
      (complico/get-text-from-node)
      (confirm-text-remains "original text")))

(deftest replace-price-case-1 
  (is (= (complico/replace-price "£3") "£XXX")))

(deftest replace-price-case-2
  (is (= (complico/replace-price "£300") "£XXX")))

(deftest replace-price-should-not-change-text-without-price 
  (is (= (complico/replace-price "text without price") "text without price")))

