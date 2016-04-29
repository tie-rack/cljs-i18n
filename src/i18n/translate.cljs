(ns i18n.translate
  (:require-macros [i18n.translations :as translations]))

(def translations
  (translations/generate-translation-map))

(defn t [key language]
  (get-in translations [key language]))
