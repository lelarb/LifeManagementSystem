## Life Management System

Life Management System je desktop aplikacija razvijena u Java Swing tehnologiji, čiji je cilj da korisnicima omogući jednostavno i pregledno upravljanje svakodnevnim aktivnostima kroz više povezanih funkcionalnih modula. Aplikacija objedinjuje upravljanje korisničkim profilom, finansijama i ličnim navikama, te korisniku pruža centralizovano mjesto za praćenje svakodnevnih obaveza i statistika.

Projekat je razvijen kao studentski projekat, sa posebnim fokusom na praktičnu primjenu objektno-orijentisanog programiranja, modularni dizajn aplikacije, rad sa NoSQL bazom podataka i upravljanje stanjem aplikacije po korisniku. Tokom razvoja, aplikacija je postepeno nadograđivana, uz kontinuirano unapređenje strukture koda i korisničkog interfejsa.

### Tehnologije korištene u projektu

Aplikacija je razvijena korištenjem Java programskog jezika, uz Swing biblioteku za izradu grafičkog korisničkog interfejsa. Za skladištenje podataka koristi se MongoDB NoSQL baza podataka, uz MongoDB Java Driver za komunikaciju između aplikacije i baze. Razvojno okruženje je IntelliJ IDEA, dok su Git i GitHub korišteni za verzionisanje koda i praćenje kompletne historije razvoja projekta.

### Setup i pokretanje projekta

Projekat se preuzima sa GitHub repozitorija pomoću sljedeće komande:

git clone https://github.com/lelabr/LifeManagementSystem


Nakon preuzimanja, projekat je potrebno otvoriti u IntelliJ IDEA okruženju. Prije pokretanja aplikacije, potrebno je osigurati da je MongoDB servis pokrenut lokalno. Aplikacija se pokreće pokretanjem glavne klase (Main), nakon čega je spremna za korištenje.

### Autentifikacija korisnika (Login & Register)

Aplikacija omogućava registraciju novih korisnika unosom korisničkog imena i lozinke. Nakon uspješne registracije, korisnik se može prijaviti u sistem. Svaki korisnik u aplikaciji ima potpuno odvojene podatke, uključujući profil, finansije i sve trackere, što osigurava privatnost i personalizovano iskustvo korištenja.

### View Profile

U sekciji profila korisnik ima mogućnost pregleda i uređivanja svojih osnovnih podataka. Moguće je unijeti ime i email adresu, dok je korisničko ime prikazano kao informativni podatak. Posebna funkcionalnost profila je izbor teme aplikacije, gdje korisnik može promijeniti boje interfejsa. Odabrana tema se automatski primjenjuje na cijelu aplikaciju. Također, korisnik se može odjaviti iz aplikacije ili u potpunosti obrisati svoj korisnički nalog.

### Finance Tracker

Finance Tracker modul omogućava vođenje lične evidencije prihoda i rashoda. Korisnik može dodavati nove transakcije, odabrati vrstu transakcije, kategoriju, opis i iznos. Sistem automatski prikazuje ukupne prihode, rashode i trenutno stanje. Postojeće transakcije se mogu ažurirati ili obrisati, a kompletan finansijski izvještaj je moguće eksportovati u .txt fajl. Svi finansijski podaci su vezani isključivo za trenutno prijavljenog korisnika.

### Water Tracker

Water Tracker omogućava praćenje dnevne količine popijene vode. Korisnik unosi količinu vode po datumu, a aplikacija prikazuje prosječnu količinu i streak, odnosno broj uzastopnih dana unosa. Datumi se u aplikaciji prikazuju u formatu dd.MM.yyyy, čime je osigurana preglednost i konzistentnost prikaza podataka.

### Sleep Tracker

Sleep Tracker služi za praćenje navika spavanja. Korisnik unosi vrijeme odlaska na spavanje i vrijeme buđenja, a aplikacija automatski izračunava ukupan broj sati sna. Također je moguće unijeti subjektivnu ocjenu kvaliteta sna. Sistem ispravno obračunava san koji prelazi preko ponoći, te prikazuje prosjek sna, prosjek kvaliteta i streak uzastopnih dana unosa.

### Habit Tracker

Habit Tracker je zamišljen kao najjednostavniji modul u aplikaciji, sa fokusom na brz i jednostavan unos navika. Ne koristi kompletan CRUD sistem, već je orijentisan na osnovno praćenje i pregled, čime se korisniku omogućava jednostavno evidentiranje navika bez dodatne kompleksnosti.

### Napomena

Projekat je razvijan iterativno, uz postepeno dodavanje novih funkcionalnosti i kontinuirano refaktorisanje postojećeg koda. Poseban fokus je stavljen na razdvajanje logike aplikacije, ponovnu upotrebu komponenti i pravilno upravljanje podacima po korisniku. Aplikacija je zamišljena kao fleksibilna osnova koja se u budućnosti može dodatno proširivati novim trackerima i funkcionalnostima.

### Struktura projekta

Struktura projekta je organizovana na način da jasno razdvaja funkcionalne cjeline aplikacije. Paket app sadrži glavnu aplikaciju i navigaciju između ekrana. Paket auth obuhvata login i registraciju korisnika. U paketu db se nalazi MongoDB konekcija, dok financeapp sadrži implementaciju Finance Trackera. Paket trackers obuhvata sve ostale trackere, uključujući water, sleep i habit tracker.

### Autor

Ime i prezime: Lejla Huseinbašić
Akademska godina: 2025/2026