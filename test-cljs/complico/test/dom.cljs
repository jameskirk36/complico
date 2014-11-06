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

(deftest convert-price-case-1 
  (is (= (complico/convert-price-in-text "£3") "£XXX")))

(deftest convert-price-case-2
  (is (= (complico/convert-price-in-text "£300") "£XXX")))

(deftest convert-price-should-not-change-text-without-price 
  (is (= (complico/convert-price-in-text "text without price") "text without price")))

(deftest replace-price-in-dom-should-correctly-replace-price
  (let [root-elem (node 
          [:body
            [:div
              [:div "£300"]]])]
    (complico/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (complico/get-text-from-node)
        (confirm-text-was-set-to "£XXX"))))

(deftest replace-price-in-dom-should-leave-text-not-containing-prices-untouched
  (let [root-elem (node 
          [:body
            [:div "£300"
              [:div "donttouchme"]]])]
    (complico/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (complico/get-text-from-node)
        (confirm-text-remains "donttouchme"))))
