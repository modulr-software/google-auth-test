(ns google-auth-test.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as response]
            [clj-oauth2.client :as oauth2]
            [clojure.data.json :as json]
            [compojure.route :as route]))

(def oauth-params {:authorization-uri   "https://accounts.google.com/o/oauth2/auth"
                   :access-token-uri    "https://oauth2.googleapis.com/token"
                   :redirect-uri        "http://localhost:3000/oauth2/google/callback"
                   :client-id           ""
                   :client-secret       ""
                   :access-query-param  :access_token
                   :scope               ["https://www.googleapis.com/auth/userinfo.email"]
                   :grant-type          "authorization_code"
                   :access-type         "online"
                   :approval_prompt     ""})

(def auth-req 
  (oauth2/make-auth-request oauth-params))

(defn- google-user-email [access-token]
  (let [response (oauth2/get "https://www.googleapis.com/oauth2/v1/userinfo" {:oauth access-token})]
    (get (json/read-str (:body response) {:key-fn keyword}) :email)))

(comment 
  (google-user-email "ya29.a0AW4XtxhtgXUuCrYSMG58FyM38Vl2p5EhLi6FAMBzxAKU8wOo1EY5wWx-Gz4nALEmQkascI1MRAOm2USGZFB9GkyHX2mq6s0QzDqvBhtO__qsP4vitY_Gnr
qUupQJrQmBwWkZEGgr9HoWwSuaP3WKwHOoGubMFOcMnhNEla-BaCgYKAUkSARYSFQHGX2MiZiqaUPz3KXxVwHnKTmJUdQ0175")
  )

(def home (GET "/" []
            {:status 200
             :body {:value "henlo"}
             :headers {"Content-Type" "application/json"}}))

(def google-login-success (GET "/gsuccess" req []
  (println (:session req))
  (let [tokens (-> req :oauth2/access-tokens :google)] 
    {:status 200 
     :body tokens
     :headers {"Content-Type" "application/json"}})))

(def google-launch (GET "/oauth2/google" [] 
  (response/redirect (:uri auth-req))))

(def google-redirect (GET "/oauth2/google/callback" req []
  (println (str "Request contents: \n" req))
  (let [token (oauth2/get-access-token oauth-params 
                                       (:params req)
                                       auth-req)]
    (println token))
  ))

(defroutes app 
  home
  google-launch
  google-redirect
  google-login-success
  (route/not-found "These are not the droids you're looking for..."))
