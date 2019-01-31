(ns webapp-scratch.core-test
  (:require [clojure.test :refer :all]
            [webapp-scratch.core :refer :all]
            [bultitude.core :as b]))

(deftest nscontext
  (testing "nscontext testing" 
    (is (= 14 (count "webapp-scratch")))
    (is (= 15 (inc (count "webapp-scratch")))))
  (testing "-> ns ns-name name"
    (is (= "webapp-scratch.home" (name :webapp-scratch.home)))
    (is (= "home" (subs "webapp-scratch.home" 14)))))



;(deftest namespaces-on-classpathfn
;  (testing "bultitude testing"
;    (is (= "abc" (b/namespaces-on-classpath :prefix misc/ns-prefix))));)
