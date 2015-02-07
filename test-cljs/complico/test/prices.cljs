(ns complico.tests.prices
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1 node]])
  (:require
   [dommy.core :as dommy]
   [complico.prices :as prices]
   [complico.dom-helper :as dom-helper]
   [cemerick.cljs.test :as t]))

; needed to console.log works!
(enable-console-print!)

(deftest find-prices-case-pounds 
  (is (= (last (first (prices/find-prices "£3"))) "3")))

(deftest find-prices-case-pounds-multiple
  (is (= (last (first (prices/find-prices "£300"))) "300")))

(deftest find-prices-case-pounds-and-pence
  (is (= (last (first (prices/find-prices "£300.00"))) "300.00")))

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
              [:div "£0"]]])]
    (prices/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (dom-helper/get-text-from-node)
        (confirm-text-was-set-to "£XXX"))))

(deftest replace-price-in-dom-should-not-overwrite-html-on-empty-text-nodes
  (let [root-elem (node 
          [:body
            [:div "   "
              [:div "£0"]]])]
    (prices/replace-prices-in-dom! root-elem)
    (-> root-elem
        (.-lastChild)
        (.-lastChild)
        (dom-helper/get-text-from-node)
        (confirm-text-was-set-to "£XXX"))))

(deftest convert-price-should-select-from-conversion-function-list-using-price-value-as-list-index-with-modulus
  (def mock-price-conversion-funcs [(fn [_ _] "0") (fn [_ _] "1") (fn [_ _] "2")])
  (is (= (prices/convert-price "£" "1.99" mock-price-conversion-funcs) "0"))  
  (is (= (prices/convert-price "£" "2.99" mock-price-conversion-funcs) "1"))  
  (is (= (prices/convert-price "£" "3.99" mock-price-conversion-funcs) "2")))

