(ns rest-postgresql-clojure.core
  (:require [compojure.core :as core]
            [ring.middleware.defaults :refer :all]
            [org.httpkit.server :as server]
            [rest-postgresql-clojure.handler.request-handler :as request-handler])
  (:gen-class))

(core/defroutes app-routes
           (core/GET "/name/:name" [name] (request-handler/name-handler name))
           (core/POST "/add-name/" request (request-handler/add-name-handler request)))

(defn -main
  "Main Program"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running the server at http://localhost:" port "/"))))
