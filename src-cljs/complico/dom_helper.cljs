(ns complico.dom-helper)

(defn is-text-node 
  [node] 
  (-> node
    (.-nodeType)
    (= 3)))

(defn get-child-nodes 
  [node]
  (.-childNodes node))

(defn get-text-node 
  [node]
  (->> node 
    (get-child-nodes)
    (filter is-text-node)
    (first)))

(defn get-text-from-node 
  [node]
  (if-let [text-node (get-text-node node)]
    (.-nodeValue text-node)))

(defn set-text-on-node 
  [node text]
  (-> node
    (.-nodeValue)
    (set! text)))

(defn replace-text-on-node! 
  [elem text]
  (let [node (get-text-node elem)]
    (set-text-on-node node text)))
