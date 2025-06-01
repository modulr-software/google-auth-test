# The Most Amazing OAuth2 Flow in the Whole World
## Bite me.

This repository exists as a prototype implementation of an OAuth2 Flow with Clojure, http-kit and compojure.
We are using DerGuteMoritz/clj-oauth2 as a wrapper over http APIs for Google OAuth2, but this flow can still be done using 
compojure and http-kit only.

## Technical Architecture Considerations

This works pretty well for what Modulr is trying to use as a HTTP stack, however, a specific choice that influenced the 
implementation of the prototype was the ability to control sessions without centralisation using JWT. Specifically, the 
ability for our HTTP API to be the issuer of sessions to the client i.e. once Google has validated the Google user, the 
API can decide whether to create its own session as well as determine if the user exists or needs to be registered AKA 
sync up the user table.

## Setup and Development

1. Register an OAuth2 Client on Google's console (or the provider of your choice).
2. Ensure you have routes implemented for redirect-uri and success.
3. You need an environment file (.env) that matches the variables required in config.edn. All of this should be straightforward 
from the values given to you on Google Console.
4. Start the nrepl using the nrepl script provided.
5. Open the frontend in the browser and complete the flow.

