(ns ^{:name "Home"}
  webapp-scratch.home
  (:use [hiccup.page :refer :all]
        [hiccup.core :refer :all]
        [compojure.core :as compojure :refer [defroutes GET]]))

(defn home-page
  [req]
  (html5 [:body "Home content"]))

(defroutes page
  (GET "/" req
  (home-page req)))
