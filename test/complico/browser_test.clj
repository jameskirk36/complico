(ns complico.browser-test
  (:use [kerodon.core]
        [kerodon.test]
        [complico.core]
        [clojure.test])
  (:require [clojure.java.io :as io]))

(deftest open-test-page
  (-> (session app)
      (visit "/testpage.html")
		(within [:div#price]
		  (has (text? "£3.00")))
  )
)
