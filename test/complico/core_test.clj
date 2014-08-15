(ns complico.core-test
  (:require [clojure.test :refer :all]
            [complico.core :refer :all]))


(def server-name "localhost")
(def server-port "3000")
(def host "http://somelink.com/")

(deftest build-link-replacements

  (testing "Dont grease the link elements"
    (is (= (let [html "<link href=\"duff.css\"/>"]
              (replace-all-in-string html 
                 (build-link-replacement-map html server-name server-port host)))
            "<link href=\"duff.css\"/>")))

  (testing "Grease the anchor (full link)"
    (is (= (let [html "<a href=\"http://anotherhost.com/duff\"/>"]
              (replace-all-in-string html 
                 (build-link-replacement-map html server-name server-port host)))
            "<a href=\"http://localhost:3000/convert?url=http://anotherhost.com/duff\"/>")))

  (testing "Grease the anchor (relative link no slash)"
    (is (= (let [html "<a href=\"duff\"/>"]
              (replace-all-in-string html 
                 (build-link-replacement-map html server-name server-port host)))
            "<a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\"/>")))

  (testing "Grease the anchor (relative link with slash)"
    (is (= (let [html "<a href=\"/duff\"/>"]
              (replace-all-in-string html 
                 (build-link-replacement-map html server-name server-port host)))
            "<a href=\"http://localhost:3000/convert?url=http://somelink.com/duff\"/>"))))
