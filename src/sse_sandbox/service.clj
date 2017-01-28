(ns sse-sandbox.service
  (:require [clojure.core.async :as async]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.sse :as sse]
            [ring.util.response :as ring-resp]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))
(defn app-page
  [request]
  (ring-resp/content-type (ring-resp/resource-response "public/index.html")
                          "text/html"))

(defn home-page
  [request]
  (ring-resp/redirect "/index.html"))

(defn stream-ready
  [event-chan context]
  (dotimes [_ 20]
    (async/>!! event-chan {:name "foo" :data (str (System/currentTimeMillis))})
    (Thread/sleep 1000))
  (async/close! event-chan))

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors [(body-params/body-params) http/html-body])

;; Tabular routes
(def routes #{["/" :get (conj common-interceptors `home-page)]
              ["/app" :get (conj common-interceptors `app-page)]
              ["/about" :get (conj common-interceptors `about-page)]
              ["/events" :get (sse/start-event-stream stream-ready)]})

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])


;; Consumed by sse-sandbox.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8081
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false}})
