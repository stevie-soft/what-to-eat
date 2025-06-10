# "Mit együnk ma?" app

## Cél
Az alkalmazás célja, hogy a szokásos "Mit együnk ma?" kérdésre a felhasználó ízlésének megfelelő választ tudjon adni / generálni.

## Komponensek
Az alkalmazás három fő komponensből épül fel:
- a felhasználó által kedvelt ételek listájának adminisztrációja
- a felhasználó által elfogyasztott ételek listájának adminisztrációja
- a felhasználó számára kedvező étel ajánlása

## Funkciók
A felhasználónak az alkalmazás grafikus felületén lehetősége van:
- új elemet rögzíteni az általa kedvelt ételek listájára bizonyos paraméterekkel (étel neve, típusa, kategóriája, átlagos költsége, preferált fogyasztási gyakorisága)
- módosítani az általa kedvelt ételek listáján szereplő elemek paramétereit
- törölni az általa kedvelt ételek listáján szereplő elemeket
- új elemet rögzíteni az elfogyasztott ételek listájára (az általa kedvelt ételek listájáról kiválasztva, dátummal összekapcsolva)
- törölni az elfogyasztott ételek listáján szereplő elemeket
- véletlenszerűen generálni egy ételt, amely az általa megszabott paramétereknek (típus, kategória, maximális költség, legutóbbi fogyasztás óta eltelt idő)

## Adattárolás
Az alkalmazás lokálisan, JSON állományokban tárolja az elvárt működéséhez szükséges adatait és a felhasználó az alkalmazás segítségével tudja ezeket módosítani.