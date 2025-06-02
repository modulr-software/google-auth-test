(ns google-auth-test.routes
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :as response]
            [clj-oauth2.client :as oauth2]
            [clojure.data.json :as json]
            [google-auth-test.environment :as env]
            [compojure.route :as route]))

(defn authorized-request
  ([token] (authorized-request {} token))
  ([req token] 
   (let [headers (:headers req)]
     (assoc req :headers 
            (assoc headers 
                   "Authorization" (str "Bearer " token))))))

(def google-oauth-params (env/read :oauth2 :google))

(def auth-req 
  (oauth2/make-auth-request google-oauth-params))

(defn- google-user-email [access-token]
  (let [response (oauth2/get "https://www.googleapis.com/oauth2/v1/userinfo" 
                             (authorized-request access-token))]
    (get (json/read-str (:body response) {:key-fn keyword}) :email)))


(def home (GET "/" []
            {:status 200
             :body {:value "henlo"}
             :headers {"Content-Type" "application/json"}}))


(def google-login-success (GET "/gsuccess" req []
  (println (:session req))
  (let [tokens (-> req :oauth2/access-tokens :google)] 
    {:status 200 
     :body {"value" "henlo m8 this here is a successful login m8 lookin real noice no cap"}
     :headers {"Content-Type" "application/json"}})))


(def google-launch (GET "/oauth2/google" [] 
  (response/redirect (:uri auth-req))))


(def google-redirect (GET "/oauth2/google/callback" req []
  (println (str "Request contents: \n" req))
  (let [token (oauth2/get-access-token google-oauth-params 
                                       (:params req)
                                       auth-req)]
    (println (google-user-email (:access-token token)))
    (response/redirect "/gsuccess"))
  ))


(defroutes app 
  home
  google-launch
  google-redirect
  google-login-success
  (route/not-found "These are not the droids you're looking for..."))


(comment 
  (def oauth-params (env/read :oauth2 :google))
  (oauth2/make-auth-request oauth-params)
  )
