(defproject checkbox-meditation "0.2.1"
  :description "A little online self-awareness tool"
  :url "https://github.com/checkbox-meditation/checkbox-meditation"
  :license {:name "MIT"}

  :min-lein-version "2.9.1"

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async "1.1.587"]
                 [cljsjs/react "15.6.1-1"]
                 [cljsjs/react-dom "15.6.1-1"]
                 [sablono "0.8.3"]
                 [org.omcljs/om "1.0.0-beta4"]]

  :plugins [[lein-figwheel "0.5.19"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]

                ;; The presence of a :figwheel configuration here
                ;; will cause figwheel to inject the figwheel client
                ;; into your build
                :figwheel { ;; :on-jsload "chbme.core/on-js-reload"
                           ;; :open-urls will pop open your application
                           ;; in the default browser once Figwheel has
                           ;; started and compiled your application.
                           ;; Comment this out once it no longer serves you.
                           :open-urls ["http://localhost:3449/index.html"]}

                :compiler ;; previous version, now dated
                            {:main chebome.lib
                            :asset-path "/js/compiled/out"
                            :output-to "resources/public/js/compiled/chebome.js"
                            :output-dir "resources/public/js/compiled/out"
                            :source-map-timestamp true
                            ;; To console.log CLJS data-structures make sure you enable devtools in Chrome
                            ;; https://github.com/binaryage/cljs-devtools
                            :preloads [devtools.preload]}
               }

               ;; This build is a compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once chebome-min
               {:id "chebome-min"
                :source-paths ["src"]
                :compiler 
                            {:main chebome.lib
                             :output-to "resources/public/js/compiled/chebome.js"
                             :output-dir "resources/public/js/compiled/out.lib"
                             :optimizations :advanced
                             :pretty-print false
                           }
               }

              ]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server :) (see lein-ring)

             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

             ;; to pipe all the output to the repl
             ;; :server-logfile false
             }

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [figwheel-sidecar "0.5.19"]]
                   ;; need to add dev source path here to get user.clj loaded
                   :source-paths ["src" "dev"]
                   ;; need to add the compliled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
