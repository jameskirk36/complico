(ns complico.test.dom
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.utils :as utils]
   [dommy.core :as dommy]
   [complico.dom :as dom]
   [complico.dom-helper :as dom-helper]
   [cemerick.cljs.test :as t]))


(deftest correctly-finds-multiple-elems
  (let [root-elem (node
                [:body
                  [:div
                    [:ol]
                    [:span]]])
        found-elems (dom/find-elems root-elem)]
    (is (= 2 (count found-elems)))))

(deftest correctly-finds-elem-div
  (let [root-elem (node [:body [:div]])
       found-elems (dom/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-span
  (let [root-elem (node [:body [:span]])
       found-elems (dom/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-li
  (let [root-elem (node [:body [:li]])
       found-elems (dom/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-a
  (let [root-elem (node [:body [:a]])
       found-elems (dom/find-elems root-elem)]
    (is (= 1 (count found-elems)))))

(deftest correctly-finds-elem-p
  (let [root-elem (node [:body [:p]])
       found-elems (dom/find-elems root-elem)]
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
    (dom/replace-prices-in-dom! root-elem)
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
    (dom/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (dom-helper/get-text-from-node)
        (confirm-text-remains "donttouchme"))))
