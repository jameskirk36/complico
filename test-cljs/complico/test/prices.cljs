(ns complico.tests.prices
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.prices :as prices]
   [complico.dom-helper :as dom-helper]
   [cemerick.cljs.test :as t]))

; needed to console.log works!
(enable-console-print!)

(deftest convert-price-case-1 
  (is (= (prices/convert-price-in-text "£3") "£XXX")))

(deftest convert-price-case-2
  (is (= (prices/convert-price-in-text "£300") "£XXX")))

(deftest convert-price-should-not-change-text-without-price 
  (is (= (prices/convert-price-in-text "text without price") "text without price")))

(deftest correctly-finds-multiple-elems
  (let [root-elem (node
                [:body
                  [:div
                    [:ol]
                    [:span]]])
        found-elems (prices/find-elems root-elem)]
    (is (= 2 (count found-elems)))))

(deftest correctly-finds-elem-div
  (let [root-elem (node [:body [:div]])
       found-elems (prices/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-span
  (let [root-elem (node [:body [:span]])
       found-elems (prices/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-li
  (let [root-elem (node [:body [:li]])
       found-elems (prices/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-a
  (let [root-elem (node [:body [:a]])
       found-elems (prices/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-p
  (let [root-elem (node [:body [:p]])
       found-elems (prices/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(defn confirm-text-was-set-to [actual-text expected-text]
  (is (= actual-text expected-text)))

(defn confirm-text-remains [actual-text expected-text]
  (is (= actual-text expected-text)))

(deftest replace-price-in-dom-should-correctly-replace-price
  (let [root-elem (node 
          [:body
            [:div
              [:div "£300"]]])]
    (prices/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (dom-helper/get-text-from-node)
        (confirm-text-was-set-to "£XXX"))))

(deftest replace-price-in-dom-should-leave-text-not-containing-prices-untouched
  (let [root-elem (node 
          [:body
            [:div "£300"
              [:div "donttouchme"]]])]
    (prices/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (dom-helper/get-text-from-node)
        (confirm-text-remains "donttouchme"))))
