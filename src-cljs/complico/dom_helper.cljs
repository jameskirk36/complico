(ns complico.dom-helper)

(defn is-text-node [node] 
  (= (.-nodeType node) 3))

(defn get-child-nodes [node]
  (.-childNodes node))

(defn get-text-node [node]
  (first (filter #(is-text-node %) (get-child-nodes node))))

(defn get-text-from-node [node]
  (let [text-node (get-text-node node)]
    (if (= text-node nil)
      nil
      (.-nodeValue text-node))))

(defn set-text-on-node [node text]
  (set! (.-nodeValue node) text))

(defn replace-text-on-node! [elem text]
  (let [node (get-text-node elem)]
    (set-text-on-node node text))
  elem)

