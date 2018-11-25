Zda se mi, že se občas generování matice sousednosti zasekne v nekonečném cyklu. - nemyslím si, ale generování ještě trošku pozměním
Je potřeba dodělat výstup vlákna simulace do externího okna a také zápis do souboru. - do souboru se budou dělat zápisi akorát statistik,
	log konzole/ informace o trucku bych tam asi nedělal
Simulace nefunguje - tak něják už jo ne? :D každopodáně to asi nechám na tobě 
Kdyby si na tom začel pracovat tak mi dej vědět na čem budeš pracovat. - neboj dávam xD

Budeme potřebovat roztřídit ten bordel v package generation do více balíčků kvůli budoucí dokumentaci.
	-to souhlasím, problém je že se to pak rozesere na gitu... :D

Bug na trucku. Občas hodí nakládání 0 objednávek. Občas nakládá o paletu víc než má. Některé trucky mají nesmyslné doby, kdy vyrážejí na cestu.
(můj tip - někde tomu trucku v simulaci předáváš čas v s. a zapomněl si to převést na minuty). Možná spolu souvisí špatné množství palet a čas.
Také velmi často se stává, že 1 truck veze třeba 4 objednávky po 1 do stejného sídla. Nevím jestli když sídlo chce objednat 4 tak objedná 4x1 nebo co
, ale asi by to chtělo se na to podívat.