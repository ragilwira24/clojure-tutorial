(ns rest-postgresql-clojure.handler.request-handler
  (:require [cheshire.core :as chesire-json]
            [clojure.java.jdbc :as jdbc]
            ; Number 1
            [rest-postgresql-clojure.configuration.database-config :as db])
  (:import (java.util UUID)))


(defn name-handler "name-handler"
  [name]
  ; Number 2
  (jdbc/with-db-connection [conn db/pg-db]
     ; Number 3
     (let [users (jdbc/query conn ["SELECT * FROM users WHERE name=?" name])]
       ;Number 4
       (let [response-data
             ;Number 5
             (if (empty? users)
               (chesire-json/generate-string
                 {:status 200 :message "Not Found users"})
               (chesire-json/generate-string users {:status 200})
               )
             ]
         {:status (:status response-data) :body response-data}
         )
       )))

(defn uuid [] (.replace (.toString (UUID/randomUUID)) "-" ""))

(defn add-name-handler "add-name-handler"
  [request]
  (let [user-data (chesire-json/parse-string (slurp (:body request)) true)]
    (let [response-data
          (try (jdbc/with-db-connection [conn db/pg-db]
                  (let [id (uuid)]
                      (jdbc/insert! conn :users {
                                                 :id    id
                                                 :name  (:name user-data)
                                                 :email (:email user-data)
                                                 })
                      (chesire-json/generate-string
                        {:status 200
                         :body {:users {
                                        :id    id
                                        :name  (:name user-data)
                                        :email (:email user-data)
                                        }}
                         })

                    ))
                   (catch Exception e
                     (println :message e)
                     (chesire-json/generate-string
                       {:status 400 :message "Saving user failed"}))
           )
          ]
          {:status (:status response-data) :body response-data}
      )


    ))


