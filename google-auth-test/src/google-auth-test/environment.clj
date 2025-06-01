(ns google-auth-test.environment
  (:require [aero.core :refer [read-config]]))

(defn read [& keys]
  (let [vars (read-config "config.edn")]
    (get-in vars (vec keys))))

(comment 
  (read :oauth2 :google)
  )
