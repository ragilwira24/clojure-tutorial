(ns rest-postgresql-clojure.configuration.database-config)

(def pg-db {:classname "org.postgresql.Driver"
            :subprotocol "postgresql"
            :subname "//localhost:5432/clojure_tutorial"
            :user "{your_username}"
            :password "{your_password}"})
