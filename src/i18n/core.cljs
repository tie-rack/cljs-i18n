(ns i18n.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [i18n.translate :as t]))

(defonce app-state (atom {:language :en
                          :step :home}))

(def supported-languages
  [:en :es :pig-latin])

(defn next-language [lang]
  (->> supported-languages
       cycle
       (drop-while (partial not= lang))
       second))

(defn cycle-languages [data]
  (dom/button #js {:onClick #(om/transact! data
                                           :language
                                           next-language)}
              (t/t :cycle-languages (:language data))))

(defn button-to [button-type]
  (fn [step data]
    (dom/button #js {:onClick #(om/update! data
                                           :step
                                           step)}
                (t/t button-type (:language data)))))

(def next-button
  (button-to :next-button))

(def prev-button
  (button-to :prev-button))

(defn home [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (dom/h1 nil (t/t :greeting (:language data)))
               (dom/p nil (t/t :about (:language data)))
               (next-button :color data)
               (cycle-languages data)))))

(def colors
  [[:red :color-red]
   [:yellow :color-yellow]
   [:green :color-green]
   [:blue :color-blue]])

(defn color-chooser [language]
  (dom/select nil
              (map (fn [[color translation-key]]
                     (dom/option #js {:value color}
                                 (t/t translation-key language)))
                   colors)))

(defn color-step [data owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (color-chooser (:language data))
               (prev-button :home data)
               (cycle-languages data)))))

(defn route [data owner]
  (reify
    om/IRender
    (render [this]
      (om/build (condp = (:step data)
                  :color color-step
                  home)
                data))))

(om/root
 route
 app-state
 {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
