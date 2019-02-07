(ns webapp-scratch.misc
  (:require [clojure.string :as str])
  (:import java.net.URI))


(def ns-prefix "webapp-scratch")

(defn ns->context
  [ns]
  (str "/" (-> ns ns-name name (subs (inc (count ns-prefix))))))
