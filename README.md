# i18n

CSV-backed translations in ClojureScript.

The translations live in translations.csv. A macro reads that file at
compile time and provides a map for i18n.translate/t to find
translations based on a key for the text and the language to use.

Live at [kilsosecond.com/i18n](http://kilosecond.com/i18n/)
