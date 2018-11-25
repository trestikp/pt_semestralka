Zda se mi, že se občas generování matice sousednosti zasekne v nekonečném cyklu.
Je potřeba dodělat výstup vlákna simulace do externího okna a také zápis do souboru.
Simulace nefunguje
Kdyby si na tom začel pracovat tak mi dej vědět na čem budeš pracovat.

Budeme potřebovat roztřídit ten bordel v package generation do více balíčků kvůli budoucí dokumentaci.
Navrhuju přidat (ke vstupu z příkazové řádky) i dotaz uvnitř programu, aby uživatel zadal požadovanou matici, nebo si ji nechal vygenerovat. Zde bude nastavat problem pokud budeme spouštět program přes "baťák".       7
Taky potřebujeme main trošku rozházet do více metod, ať nemá tolik řádek.