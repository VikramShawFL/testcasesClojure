(ns ^{:name "About"}
  webapp-scratch.about
  (:use [hiccup.page :refer :all]
        [hiccup.core :refer :all]
        [compojure.core :as compojure :refer [defroutes GET]]))

(defn about-page
  [req]
  (html5 [:body "About content"]))

(defroutes page
  (GET "/" req
  (about-page req)))
