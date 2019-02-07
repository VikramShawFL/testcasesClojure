<<<<<<< HEAD
(ns webapp-scratch.misc
  (:require [clojure.string :as str])
  (:import java.net.URI))


(def ns-prefix "webapp-scratch")

(defn ns->context
  [ns]
  (str "/" (-> ns ns-name name (subs (inc (count ns-prefix))))))
=======
(ns webapp-scratch.misc
  (:require [clojure.string :as str])
  (:import java.net.URI))


(def ns-prefix "webapp-scratch")

(defn ns->context
  [ns]
  (str "/" (-> ns ns-name name (subs (inc (count ns-prefix))))))
>>>>>>> 40fba55deb1b0600ecdf8e632fb65082bd9dfe45
