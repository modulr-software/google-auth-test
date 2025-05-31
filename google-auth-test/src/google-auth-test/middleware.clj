(ns google-auth-test.middleware
  (:require [ring.middleware.json :as ring]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cookies :as cookies]))

(defn apply-generic [app]
  (-> app
      (wrap-cors :access-control-allow-origin [#"http://127.0.0.1:5500"]
                 :access-control-allow-methods [:get :put :post :delete])
      (wrap-params)
      (wrap-defaults (-> site-defaults (assoc-in [:session :cookie-attrs :same-site] :lax)))
      (ring/wrap-json-response)
      (ring/wrap-json-body {:keywords? true})
      (cookies/wrap-cookies)))
