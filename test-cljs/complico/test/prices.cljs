(ns complico.tests.prices
  (:require-macros [cemerick.cljs.test
                    :refer (are is deftest with-test run-tests testing test-var)])
  (:use-macros
   [dommy.macros :only [sel sel1]])
  (:require
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
  (is (= (nth (first (prices/find-prices "£3")) 2) "3")))

(deftest find-prices-case-hundreds
  (is (= (nth (first (prices/find-prices "£300")) 2) "300")))

(deftest find-prices-case-with-decimal
  (is (= (nth (first (prices/find-prices "£300.00")) 2) "300.00")))

(defn create-test-dom [elem]
  (hipo/create [:body [elem]]))

(deftest finds-allowed-price-elems
  (are [elem] (-> elem (create-test-dom) (prices/find-elems) (count) (= 1))
   :div :span :li :a :p :em :td :strong))

(defn dom-with-two-price-elems []
  (hipo/create [:body [:div [:ol] [:span]]]))

(deftest finds-multiple-allowed-price-elems
  (is (= 2 (count (prices/find-elems (dom-with-two-price-elems))))))

(defn confirm-text-was-set-to [actual-text expected-text]
  (is (= actual-text expected-text)))

(defn confirm-text-remains [actual-text expected-text]
  (is (= actual-text expected-text)))

(defn complex-dom-with-price []
  (hipo/create [:body [:div [:div#with-price "£0"]]]))

(deftest replace-price-in-complex-dom-should-find-and-replace-price
  (-> (complex-dom-with-price)
    (prices/replace-prices-in-dom!)
    (sel1 "#with-price")
    (dom-helper/get-text-from-node)
    (confirm-text-was-set-to "£XXX")))

(defn dom-with-price-but-higher-text-node []
  (hipo/create [:body [:div "   " [:div#with-price "£0"]]]))

(deftest replace-price-in-dom-should-not-overwrite-innerhtml-on-higher-empty-text-nodes
  (-> (dom-with-price-but-higher-text-node)
    (prices/replace-prices-in-dom!)
    (sel1 "#with-price")
    (dom-helper/get-text-from-node)
    (confirm-text-was-set-to "£XXX")))

(deftest convert-price-should-select-from-conversion-function-list-using-price-value-as-list-index-with-modulus
  (def mock-price-conversion-funcs [(fn [_ _] "0") (fn [_ _] "1") (fn [_ _] "2")])
  (is (= (prices/convert-price "£" "1.99" mock-price-conversion-funcs) "0"))  
  (is (= (prices/convert-price "£" "2.99" mock-price-conversion-funcs) "1"))  
  (is (= (prices/convert-price "£" "3.99" mock-price-conversion-funcs) "2")))

(defn create-elem-with-text [text]
  (hipo/create [:div text]))

(deftest should-replace-price-on-elem
  (-> (create-elem-with-text "£0")
    (prices/replace-price-on-elem!)
    (dom-helper/get-text-from-node)
    (confirm-text-was-set-to "£XXX")))

(deftest should-replace-price-on-elem-and-leave-trailing-text
  (-> (create-elem-with-text "£0 trailing text")
    (prices/replace-price-on-elem!)
    (dom-helper/get-text-from-node)
    (confirm-text-was-set-to "£XXX trailing text")))

(deftest should-replace-multiple-prices-on-elem
  (-> (create-elem-with-text "£0 £0")
    (prices/replace-price-on-elem!)
    (dom-helper/get-text-from-node)
    (confirm-text-was-set-to "£XXX £XXX")))
