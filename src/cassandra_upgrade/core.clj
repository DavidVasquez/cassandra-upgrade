(ns cassandra-upgrade.core
  (:gen-class)
  (:require [clj-yaml.core :as yaml]))

(defn parse-yaml
  "Reads and parses the input yaml file"
  [file]
  (yaml/parse-string (slurp file)))

(defn convert-config
  ""
  [current upgrade]
  (reduce (fn [acc [opt-key opt-val]]
            (let [upgrade-option (get upgrade opt-key)]
              ;(println opt-key opt-val upgrade-option)
              (if (not (nil? upgrade-option))
                (assoc acc opt-key opt-val)
                acc)))
          {}
          current))

(defn upgrade-files
  "Upgrades a given cassandra config file to the options in the second file"
  []
  (let [cfg-current (parse-yaml "cass13.yaml")
        cfg-upgrade (parse-yaml "cass14.yaml")
        config (convert-config cfg-current cfg-upgrade)]
         (spit "cass.yaml" (nth (yaml/generate-string config) 1))))
         ;(spit "cass.yaml")))

(defn -main
  "Kicks off the cassandra config upgrade process"
  [& args]
  (upgrade-files))
