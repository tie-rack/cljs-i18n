(ns i18n.translations
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(defn row->map [row headers]
  (let [[key & translations] row]
    {(keyword key) (zipmap (rest headers) translations)}))

(defmacro generate-translation-map []
  (let [translation-file (io/resource "translations.csv")]
    (with-open [r (io/reader translation-file)]
      (let [headers (->> r
                         .readLine
                         csv/read-csv
                         first
                         (map keyword))
            rows (csv/read-csv r)]
        (reduce (fn [m row]
                  (merge m (row->map row headers)))
                {} rows)))))
