(ns webapp-scratch.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer (wrap-resource)]
            [compojure.core :as compojure :refer [defroutes GET]]
            [compojure.route :refer [resources not-found]]
            [hiccup.page :refer :all]
            [hiccup.core :refer :all]
            [bultitude.core :as b]
            [hiccup.element :as e]
            [webapp-scratch.misc :as misc]
            [clojure.test :refer :all]))

(deftest namespaces-on-classpathfn
  (testing "bultitude testing"
    (is (= "abc" (b/namespaces-on-classpath :prefix misc/ns-prefix)))))

(defn vars
  [ns]
  {:namespace ns
   :ns-name (ns-name ns)
   :name (-> ns meta :name)
   :route-prefix (misc/ns->context ns)
   :page (ns-resolve ns 'page)})

(deftest varsfn2
  (testing "demo vars testing 2"
    (is (= "abc" (vars (first (b/namespaces-on-classpath :prefix misc/ns-prefix)))))
    ))


(def list-of-maps (->> (b/namespaces-on-classpath :prefix misc/ns-prefix)
                       distinct
                       (map #(do (require %) (the-ns %)))
                       (map vars)
                       (filter #(:page %))
                       (sort-by :ns-name)))

(deftest namespace
  (testing "namespace generation"
    (is (= "abc" (map #(do (require %) (the-ns %)) (b/namespaces-on-classpath :prefix misc/ns-prefix)))))
  (testing "vars gen"
    (is (= "abc" (map vars (map #(do (require %) (the-ns %)) (b/namespaces-on-classpath :prefix misc/ns-prefix)))))))

(deftest filtersortby
  (testing "filter page"
    (is (= "abc" (filter (#(:page %) (map vars (map #(do (require %) (the-ns %)) (b/namespaces-on-classpath :prefix misc/ns-prefix))))))))
  (testing "sort-by ns-name"
    (is (= "abc" (sort-by :ns-name (filter (#(:page %) (map vars (map #(do (require %) (the-ns %)) (b/namespaces-on-classpath :prefix misc/ns-prefix))))))))))

(defroutes landing
  (GET "/" req (html5 [:ul
                       (for [{:keys [name route-prefix]} list-of-maps]
                         [:li (e/link-to (str route-prefix "/") name)])])))

(defn- wrap-app-metadata
  [h app-metadata]
  (fn [req] (h (assoc req :demo app-metadata))))

(def site (apply compojure/routes
            landing
            (for [{:keys [app page route-prefix] :as metadata} list-of-maps]
              (compojure/context route-prefix []
                (wrap-app-metadata (compojure/routes (or page (fn [_]))) metadata)
                                 ))))

(defn -main
  [port-number]
  (jetty/run-jetty (wrap-reload #'site)
                   {:port (Integer. port-number)}))


