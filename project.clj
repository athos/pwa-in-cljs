(defproject pwa-in-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/core.async  "0.3.443"]
                 [kitchen-async "0.1.0-SNAPSHOT"]
                 [reagent "0.8.0-alpha2"]]

  :plugins [[lein-figwheel "0.5.15"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src/app"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/app"]
                :figwheel {:on-jsload "pwa-in-cljs.core/on-js-reload"
                           :open-urls ["http://localhost:3449/index.html"]}
                :compiler {:main pwa-in-cljs.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/app.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src/app"]
                :compiler {:output-to "resources/public/js/compiled/app.js"
                           :main pwa-in-cljs.core
                           :optimizations :advanced
                           :pretty-print false}}

               {:id "sw-dev"
                :source-paths ["src/worker"]
                :compiler {:main pwa-in-cljs.service-worker
                           :output-to "resources/public/js/compiled/sw.js"
                           :optimizations :whitespace
                           :pretty-print true
                           :language-in :es-2015}}]}

  :figwheel {:css-dirs ["resources/public/css"] }
  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.15" :exclusions [[http-kit]]]
                                  [http-kit "2.3.0-beta2"]
                                  [com.cemerick/piggieback "0.2.2"]]
                   :source-paths ["src/app" "dev"]
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}})
