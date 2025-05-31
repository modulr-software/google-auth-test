(ns dev
  (:require [google-auth-test.server :as server]))

(defn after-ns-reload []
  (server/restart-server))

(comment 
  (server/start-server)
  (server/stop-server)
  (server/running?)
  (server/restart-server))
