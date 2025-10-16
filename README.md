# Amőba NxM Játék

Parancssoros amőba (gomoku) játék implementáció Java nyelven.

## A játékról

Az amőba egy kétszemélyes stratégiai táblajáték, ahol a játékos (X) a gép (O) ellen játszik egy NxM-es táblán. A cél, hogy 4 jelet rakj ki sorban - vízszintesen, függőlegesen vagy átlósan.

## Játékszabályok

- A tábla mérete 5-25 sor és 5-25 oszlop között állítható be
- Az oszlopok betűkkel (a, b, c...), a sorok számokkal (1, 2, 3...) vannak jelölve
- A játékos (X) mindig kezd, majd a gép (O) következik
- Az első lépést a tábla közepére kell tenni
- Minden további lépésnek szomszédosnak kell lennie egy már elfoglalt mezővel (vízszintesen, függőlegesen vagy átlósan)
- Az nyer, aki 4 jelet kirak egymás mellé

## Parancsok

- `a5` - Lépés az 'a' oszlopba, 5. sorba (példa)
- `save` - Játék mentése fájlba
- `exit` - Kilépés a játékból

## Technológiák

- Java 17
- Maven
- JUnit 5 - tesztelés
- Mockito - mockolás
- Logback - naplózás
- JaCoCo - kód lefedettség mérés
- Checkstyle - kód minőség ellenőrzés

## Futtatás

```bash
mvn clean install
java -jar target/amoba-game-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Tesztelés

```bash
mvn test
```

## Projekt célja

Ez a projekt a Programozási technológiák tárgy beadandó feladataként készült.
