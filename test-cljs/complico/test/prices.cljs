(ns complico.tests.prices
  (:require-macros [cemerick.cljs.test
                    :refer (are is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1]])
  (:require
   [dommy.core :as dommy]
   [complico.prices :as prices]
   [complico.dom-helper :as dom-helper]
   [hipo.interpreter :as hipo]
   [cemerick.cljs.test :as t]))

; needed to console.log works!
(enable-console-print!)

(deftest find-prices-british-pounds
  (is (= (second (first (prices/find-prices "£3"))) "£")))

(deftest find-prices-american-dollars
  (is (= (second (first (prices/find-prices "$3"))) "$")))

(deftest find-prices-case-single 
  (is (= (last (first (prices/find-prices "£3"))) "3")))

(deftest find-prices-case-hundreds
  (is (= (last (first (prices/find-prices "£300"))) "300")))

(deftest find-prices-case-with-decimal
  (is (= (last (first (prices/find-prices "£300.00"))) "300.00")))

(deftest correctly-finds-multiple-elems
  (let [root-elem (hipo/create
                [:body
                  [:div
                    [:ol]
                    [:span]]])
        found-elems (prices/find-elems root-elem)]
    (is (= 2 (count found-elems)))))

(defn create-test-dom [elem]
  (hipo/create [:body [elem]]))

(deftest finds-allowed-price-elems
  (are [elem] (-> elem (create-test-dom) (prices/find-elems) (count) (= 1))
   :div :span :li :a :p :em :td :strong))

(defn confirm-text-was-set-to [actual-text expected-text]
  (is (= actual-text expected-text)))

(defn confirm-text-remains [actual-text expected-text]
  (is (= actual-text expected-text)))

(deftest replace-price-in-dom-should-correctly-replace-price
  (let [root-elem (hipo/create 
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
  (let [root-elem (hipo/create 
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

