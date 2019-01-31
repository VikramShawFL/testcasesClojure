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

(deftest varsfn
  (testing "demo vars testing"
    (is (= "abc" (first (b/namespaces-on-classpath :prefix misc/ns-prefix))))
;    (is (= "abc" (vars (first (b/namespaces-on-classpath :prefix misc;/ns-prefix)))))
    ))

(deftest varsfn2
  (testing "demo vars testing 2"
    (is (= "abc" (vars (first (b/namespaces-on-classpath :prefix misc/ns-prefix))))))


(defn vars
  [ns]
  {:namespace ns
   :ns-name (ns-name ns)
   :name (-> ns meta :name)
   :route-prefix (misc/ns->context ns)
   :page (ns-resolve ns 'page)})

(def list-of-maps (->> (b/namespaces-on-classpath :prefix misc/ns-prefix)
                       distinct
                       (map #(do (require %) (the-ns %)))
                       (map vars)
                       (filter #(:page %))
                       (sort-by :ns-name)))

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


