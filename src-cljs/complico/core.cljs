(ns complico.core)

(defn is-full-link [link] 
  (= (subs link 0 4) "http"))

(defn is-relative-link [link]
  (= (subs link 0 1) "/"))

(defn host-minus-trailing-slash [host]
  (apply str (drop-last 1 host)))

(defn grease-the-link [host grease link]
  (str grease 
    (js/encodeURIComponent
      (cond
        (is-full-link link) link
        (is-relative-link link) (str (host-minus-trailing-slash host) link)
        :else (str host link)))))

(defn find-prices [text]
  (re-seq #"£(\d+)" text))

(defn convert-price [price]
  "£XXX")

(defn convert-prices [prices]
  (map
    #(vector
      (first %)
      (convert-price (last %)))
    prices))

(defn convert-price-in-text [text]
  (let [prices (find-prices text)
        replacement-prices (convert-prices prices)]
    (reduce 
      #(apply clojure.string/replace %1 %2)
      text
      replacement-prices))) 
