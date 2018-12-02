1) Zda se mi, že se občas generování matice sousednosti zasekne v nekonečném cyklu.
	-generování jsem snad fixnul, bohužel jsem si to přepsal, ale znovu jsem to opravoval, tak snad správně

2) Je potřeba dodělat výstup vlákna simulace do externího okna a také zápis do souboru.
	-do externího okna je teď výpis snad všeho. Do souboru mám importy a export (trochu jsem ošetřil vstupy, ale asi by to chtělo ještě testnout,
		kdyby si se nudil). Jinak cheš abych udělal výstup i cest do souboru? 

3) Simulace nefunguje
	-tak něják trošičku jo, to je na tobě ;)
	
4) Kdyby si na tom začel pracovat tak mi dej vědět na čem budeš pracovat.   //TODO

5) Budeme potřebovat roztřídit ten bordel v package generation do více balíčků kvůli budoucí dokumentaci.
	-naprosto souhlasím
	

6) Bug na trucku. Občas hodí nakládání 0 objednávek. Občas nakládá o paletu víc než má. Některé trucky mají nesmyslné doby, kdy vyrážejí na cestu.
	(můj tip - někde tomu trucku v simulaci předáváš čas v s. a zapomněl si to převést na minuty). Možná spolu souvisí špatné množství palet a čas.
	Také se velmi často stává, že 1 truck veze třeba 4 objednávky po 1 do stejného sídla. Nevím jestli když sídlo chce objednat 4 tak objedná 4x1 nebo co,	
	ale asi by to chtělo se na to podívat.     ------- chyba nastava pri dni dva a více.. někde jsem na něco zapomněl  (zítra na to kouknu)

7) Když začíná den, tak má 0 objednávek. Z toho jak to chápu, tak na začátku dne, v 6 hodin už by měl znát X objednávek a v průběhu dne by jich měl dostávat čím dál míň.
  -------objednávky z minulých dní bych přenášel do dalších dní. A ano objednávky by měli postupně klesat podle času.

8) Když přeruším simulaci, vygeneruji nové data a spustím znovu simulaci, tak "pokračuje" v předchozí simulaci. Jestli se ti tuto nechce řešit, tak jednoduše udělám, že pokud si
	zastavil simulaci, tak aby si mohl pustit novou, tak musíš restartovat program. (Což bych asi zvolil, pokud nebudem mít moře času, s čímž moc nepočítam :D)
	
9) GUI je na dobré cestě, když tak si ho zkus proklikat, aby si otestoval ošetření vstupů, jestli jsem nějáké zapomněl..
	Stále na GUI jsou věci co vylepšovat, ale až dodělam ještě pár funkčností (hlavně menu, kde zatím fungují jen importy a exporty souborů), tak si od GUI dam pauzu,
	pokud budeš na GUI chtít něco změnit/ předělat tak buď napiš nebo se s tim můžeš zkusit potrápit sám
	
10) Když se naimportují data ze souborů, tak simulace negeneruje Trucky. Tuto si myslím, že vím proč je, takže když tak to udělám, kdyby si na to koukal, tak řekni.

*) To je asi všechno GL



27.11 

11) třeba dodělat ošetření startu simulaci bez vygenerovaných komponentů
12) umožnit zastavovani a nasledneho spousteni simulace. Hazi to chybu kdyz je otevřeno    viz bod 8).. neřekl bych že je chyba v zastavování
13) při zmáčknutí tlačítka exit je třeba vypnout pomocí System.close(0) nebo jak to je a zeptat se ho zda chce ulozit do souboru
14) a zacatku každeho dne uložit výsledky do souboru   (ptá se na to u odevzdavani)
15) ve třídě Truck jsou všechny statistiky potřebné jak je zadáno v zadání


