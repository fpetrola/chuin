# Plan: que se vea como Turbo Vision

Referencia: la captura de Quadra (una aplicación Turbo Vision) que pasó Fernando. Este plan lista lo que falta para llegar a ese aspecto, en pasos que se pueden hacer de a uno con TDD y cerco. Cada paso empieza con un test de aceptación, sigue con su propuesta de superficie si la necesita (la firma Fernando o cerco-tdd-97), queda en verde con `cerco:audit` y termina sumándose a la demo. Las decisiones que salgan van a `DECISIONS.md`.

## Lo que ya está

- Ventanas con marco simple, título y sombra `░`. Se mueven con las flechas, se cambian con F6 y se cierran con Ctrl+W.
- Botón, etiqueta, check box, campo de texto, lista con scroll bar y botones de opción.
- Barra de menú con F10, flechas, Enter y mouse.
- Mouse sobre todos los widgets.
- Cuatro tintes: desktop, ventana, foco y sombra.

## Lo que falta, por tema

| # | Qué | Cómo se ve en Quadra | Hoy |
|---|-----|----------------------|-----|
| 1 | Paleta ampliada | Cada widget tiene su color: campo blanco sobre azul, botón negro sobre verde, grupo de opciones sobre cian, letras resaltadas en amarillo o rojo | 4 tintes |
| 2 | Fondo del desktop con trama | `░` gris sobre azul | Azul liso |
| 3 | Sombra que oscurece | Lo que queda debajo de la sombra se sigue viendo, pero oscuro | Se escribe `░` encima |
| 4 | Marco doble en la ventana activa | `═` y `║` en la de arriba, simple en las demás | Simple en todas |
| 5 | Botón con fondo y sombra propia | Fondo verde, sombra `▄` a la derecha y `▀` debajo | `[ Ok ]` sin fondo |
| 6 | Campo de texto con fondo y cursor | Blanco sobre azul y cursor visible | Texto con relleno `_` |
| 7 | Línea de estado | Fila de abajo: `Esc Raus hier  F10 Menü  F9 Sortieren …`, con las teclas resaltadas y clickeables | No existe |
| 8 | Letras resaltadas | `~D~atei`, `~O~rt:`, `~N~eu`: una letra de otro color | No existe |
| 9 | Alt+letra y Esc suelto | Alt+O enfoca el campo de "Ort", Alt+D abre Datei, Esc cierra el menú | El teclado no distingue un Esc solo |
| 10 | Etiqueta asociada a un campo | La etiqueta "Ort:" enfoca su campo con un clic o con su letra | Las etiquetas son sueltas |
| 11 | Menús completos | Atajo a la derecha (`F3`, `Esc`), separadores `───`, menú `≡` a la izquierda | Solo el texto del ítem |
| 12 | Teclas globales | F3 ejecuta "Laden" desde cualquier lugar, igual que su ítem de menú y su entrada en la línea de estado | No existen |
| 13 | Grupo de opciones con marco | Fondo cian y una etiqueta arriba ("Land:", con su letra resaltada) | Opciones sueltas |
| 14 | Lista desplegable | `↓Ja↓`: un campo que abre una lista | No existe |

## Pasos

### Paso 1: paleta, fondo, sombra que oscurece y marco doble (1, 2, 3 y 4) — hecho

Hecho en `c39bce2`, `41ccc74`, `fe50ec0`, `4ee49d5` y el commit de la trama (DECISIONS 42 y 43). La paleta todavía no hizo falta ampliarla: los tintes nuevos van a entrar con los botones y los campos, en el paso 2.

Hay que decidir de nuevo cómo se guardan los tintes. Hoy son un `char` por celda (DECISIONS 14) con cuatro letras. Con unos doce tintes, las letras sueltas se vuelven difíciles de leer. Propongo dos cosas:
- Seguir con un `char` por celda, porque los tests leen los tintes fila por fila, y documentar la paleta entera en `Screen#tints`. La alternativa es un tipo `Tint`, y hay que decidirlo antes de empezar.
- Que la sombra sea un tinte que no escribe nada: la terminal muestra el mismo carácter, pero en gris sobre negro. No oscurece cada color por separado, sino que lo reemplaza por gris sobre negro, como hace Turbo Vision. Alcanza, y el test lo fija así.

Tests:
- El desktop vacío se llena de `░` con tinte de fondo.
- La sombra de una ventana deja ver lo que tapa, con tinte de sombra.
- La ventana de arriba tiene marco doble y las demás, simple.
- Un test de terminal muestra el color ANSI de cada tinte nuevo.

Superficie: probablemente ninguna, porque `Frame` y `Shadow` son de `chuin-text`, salvo que se elija el tipo `Tint`.

### Paso 2: botón y campo de texto con su aspecto (5 y 6)

Tests:
- Un botón se dibuja con fondo de botón, y su sombra con `▄` y `▀`. El botón con foco usa otro tinte.
- Un campo se dibuja con fondo de campo, sin `_`, y el campo con foco pone el cursor al final de su texto.

Superficie: la pantalla tiene que guardar dónde va el cursor (por ejemplo `Screen#cursor(Point)`), y la terminal lo posiciona y lo muestra. Va con propuesta.

### Paso 3: Esc suelto y Alt+letra (9)

Tests:
- `ESC` sin nada detrás es la tecla Esc.
- `ESC` seguido de una letra es Alt+letra.
- `ESC [` sigue siendo el comienzo de una secuencia.

Para distinguirlos, el teclado espera un momento corto después del `ESC` (entre 25 y 100 ms, como Turbo Vision y ncurses). Si no llega nada, es un Esc suelto. `InputStream#available` no alcanza: por SSH, o en una terminal lenta, el `ESC` y el `[` pueden llegar en lecturas separadas. Un test con un stream que demora fija los dos casos. Esc cierra el menú abierto.

Superficie: `KeyVisitor#escape()` y `KeyVisitor#alt(char)`. Va con propuesta.

### Paso 4: letras resaltadas (8, 10 y la parte de menús de 9)

Los textos marcan su letra con `~`, como en Turbo Vision: `"~D~atei"`. El texto se dibuja sin las `~`, y la letra marcada sale con el tinte de letra resaltada.

Tests:
- Los títulos de menú, los ítems, los botones y las etiquetas muestran su letra resaltada.
- Alt+letra abre el menú de esa letra.
- Con un menú abierto, la letra sola elige el ítem.
- Alt+letra aprieta el botón de esa letra.
- Alt+letra enfoca el campo asociado a una etiqueta.
- `~~` se escribe como un `~` literal.

Superficie: una etiqueta asociada a una vista, por ejemplo `Toolkit#label(String, View)`. Va con propuesta.

### Paso 5: teclas globales, línea de estado y menús completos (7, 11 y 12)

Una misma tecla (F3) tiene que poder ejecutar lo mismo desde el ítem del menú, desde la línea de estado y desde cualquier lugar del desktop. El orden:
1. Las teclas globales del desktop.
2. La línea de estado.
3. El atajo mostrado a la derecha en el menú.
4. Los separadores.
5. El menú `≡`.

Tests:
- F3 ejecuta su comando con cualquier ventana enfocada.
- La línea de estado se dibuja abajo con las teclas resaltadas, y un clic en una entrada la ejecuta.
- Un ítem muestra su atajo alineado a la derecha.
- Un separador se dibuja como `├───┤` y las flechas lo saltean.

Superficie: `Toolkit#statusLine()`, una forma de asociar una tecla a un comando (en el desktop o en la línea de estado), `Menu#add` con atajo y `Menu#separator()`. Va con varias propuestas.

### Paso 6: grupo con marco y lista desplegable (13 y 14)

Tests:
- El grupo de opciones se dibuja con fondo propio y su etiqueta arriba.
- Un campo con lista desplegable muestra `↓`, y Alt+↓ o un clic abre la lista debajo. Enter elige el ítem y lo copia al campo.

Superficie: la etiqueta del grupo, y `Toolkit#comboBox(...)` o como se llame. Va con propuesta.

### Paso 7: la demo como Quadra — hecho

Rearmar la demo para que se parezca a la captura:
- Una ventana "Adreßdatenbank" con etiquetas, campos, dos grupos de opciones y una fila de botones.
- La barra con `≡`, Datei y Aktionen.
- La línea de estado.

## Para decidir antes de empezar

1. ¿Los tintes siguen siendo un `char` por celda, con la paleta documentada, o pasan a ser un tipo? Recomiendo seguir con `char`, por cómo leen los tests.
2. ¿Usamos `~` para marcar la letra resaltada, como Turbo Vision? Recomiendo que sí.
3. ¿El orden de los pasos está bien, o hay algo que quieras ver antes? Por ejemplo, la línea de estado no depende de los pasos 2 a 4 y se podría adelantar.

## Revisión del modelo: abstraer, unificar, sacar duplicados

Sale de una revisión de chuin-api y chuin-text. Las reglas son las de siempre:
- Cada paso es un commit (o pocos) que compila solo y no cambia lo que se ve.
- Lo que haga crecer una superficie sellada va con propuesta y firma.
- Los tests que hay hoy cuidan el comportamiento. Si un concepto nuevo promete algo, lleva su test de contrato.

### Etapa A: duplicación chica, sin tocar chuin-api

**A1. La barra de la lista es una barra como las otras.** Hoy el clic está dos veces (`ClickFocused` repite el autómata de `BarClick`: `pastThumb`, `walked++`) y el dibujo también (`RowPainter` repite los glifos `░ ■` de `BarCells`).
- `ClickFocused`, cuando el clic cae en la columna de la barra, delega en `BarClick`.
- `RowPainter` delega los `visitScroll*` en `BarCells`.
- Los glifos quedan en un solo lugar.

**A2. El ancho de una entrada, una sola vez.** `TextCaption` y `KeyCaption` repiten `shown().length() + 2`.
- Opción 1: un `default` en `Caption`.
- Opción 2: que lo diga quien pone entradas en fila.

Se decide en la propuesta.

**A3. Colores por rol.** Hoy son letras sueltas (`'f'`, `'h'`, `'m'`, `'.'`) en cada clase que dibuja y en `DosPalette`.
- Se juntan como constantes con nombre de rol (`FOCUS`, `HOTKEY`, `MENU`, `BARE`…) en un solo lugar, que también usa la paleta.
- Siguen siendo `char`, como se decidió al principio, porque los tests leen tintes como texto.

### Etapa B: conceptos que faltan, todavía en chuin-text

**B1. FocusOrder.** Qué vista es la n-ésima enfocable lo deciden hoy `CollectFocusable`, `AtFocus` y `Painter`, cada uno con su `seen++`. En `Painter` ya hay un parche, `seen - 1 == focused`.
- Una sola pieza dice si la vista visitada es enfocable y si tiene el foco.
- `AtFocus` y `Painter` se apoyan en ella.

**B2. MenuBox y el borde de un marco.** El menú abierto se dibuja en `2 + fila` (`MenuPainter`) y se clickea en `fila - 2` (`TextPlacedTitle`), y los dos arman la misma lista de filas.
- Un `MenuBox` con el área del menú, sus filas y la fila de cada ítem, que comparten dibujo y clic.
- `Frame.inside(area)` / `Frame.around(area)` reemplazan los `+1/+2/-2` sueltos en `Painter`, `TextWindow`, `DropDownList`, `MenuPainter` y `Frame`.

**B3. La entrada ubicada.** La línea de estado y la barra de menú recalculan dónde empieza cada entrada, y solo la barra tiene `PlacedTitle`.
- Una entrada ubicada común (caption, columna y `covers`) para títulos y entradas de estado.
- `StatusClick` y `MenuDesktop.placed()` la usan.

**B4. Geometría con nombre.** "El área contiene la celda" está copiado en 6 lugares, y la intersección (`Painter.within`) y el punto relativo están hechos a mano.
- Primero en chuin-text. Pasa a `Area` (chuin-api, con propuesta) solo si hace falta.

**B5. Cursor.** El índice acotado y "desde qué fila se ve" se repiten en `TextListBox`, `TextOptionGroup`, el menú abierto, `TextDropDown` y `PanelAxis`.
- Un `Cursor` con `previous`, `next`, `page` y `moveTo`, que todos componen.

### Etapa C: partir las clases grandes

Va después de B, porque B ya les saca partes.

**C1. `TextDesktop`.**
- `WindowStack`: cada ventana junto con su foco, en vez de las dos listas paralelas `opened` y `remembered`.
- `atFocus(acción, si no)`: reemplaza el patrón Settle + AtFocus que hoy se repite 5 veces.
- `WindowDrag`: mover y agrandar una ventana.

**C2. `Painter`.**
- `Viewport`: el recorte, el origen y la pila de paneles.
- `HitMap`: las áreas dibujadas y los blancos de clic.
- `ButtonFace`: la cara, la sombra y el botón apretado.
- `Painter` queda como el despacho por widget.

### Etapa D: toca chuin-api, más discutible

**D1. Las filas dicen su índice.** `ListItem` y `Option` exponen su índice, o se unifican en una fila de una vista de filas. Así desaparecen los contadores `x++ == row` de `ClickFocused`, `RowPainter` y `MenuPainter`.

**D2. `KeyId`.** Un valor con igualdad en vez del string de `KeyName` como clave de los atajos y de `"escape"`.

**D3. Nombres.**
- `ToWindows` y `PointerWindows` por algo que diga lo que hacen.
- Los campos `list`, `rows` y `place` de los painters.

### Lo que no se toca

- `Scrollable`, `PanelAxis` y `TextScrollBar`.
- La composición `Frame`, `Shadow` y `RaisedFrame`.
- `Settle`/`PendingAction` y los estados `Closed`/`Opened` del menú.

### Para decidir

1. ¿El orden A → B → C → D está bien? Recomiendo que sí: lo chico y seguro primero, y lo que toca la API al final.
2. ¿D entra, o se deja para más adelante? Recomiendo decidirlo al terminar C, viendo cuánto quedó de los contadores.
3. En B4, ¿la geometría sube a `Area` en chuin-api, o queda en chuin-text? Recomiendo empezar en chuin-text.

## Paquetes de chuin-text

Las 94 clases de chuin-text estaban todas en un solo paquete. Ahora se reparten en paquetes debajo de `com.fpetrola.chuin.text`, separados de la API para que ningún paquete quede partido entre dos módulos (nota del reviewer: así JPMS no lo impide). Se hace un paso por paquete, de abajo hacia arriba, sin ciclos entre paquetes, y cada paso lleva su propuesta, su firma, el audit, su entrada en DECISIONS y su commit.

1. `text.geometry`: `TextArea`, `TextPoint`, `TextSize`, `Extent`, `TextExtent`.
2. `text.screen`: `TextScreen`, `ClippedScreen`, `AnsiTerminal`, `Palette`, `DosPalette`, `Tint`, `TintRole`.
3. `text.input`: `Arrow`, `KeyName`. `StreamKeyboard` espera al último paso: implementa `Keyboard` y usa `Toolkit` y `Desktop`, que son de la raíz de la API, y mientras la raíz tenga también clases de chuin-text (`MenuDesktop` usa `KeyName`), moverlo ahora arma un ciclo `com.fpetrola.chuin` <-> `text.input`.
4. `text.view`: los modelos de las vistas, `Cursor`, `Group`, `Caption`, `PanelAxis`, `Scrollable`.
5. `text.pointer`: `ClickTarget`, `Grab`, `Dragging`, `BarTarget`, `BarClick`, `BarLine`/`TextBarLine`, `Settle`/`PendingAction`.
6. `text.paint`: `Painter`, `RowPainter`, `BarCells`, `FocusOrder`, `Border`, `Line`, `Frame`, `RaisedFrame`, `Shadow`, `Drawing`, `DropDownList`.
7. `text.desktop`: `TextDesktop`, `Windows`, `ToWindows`, `PointerWindows`, `AtFocus`, `CollectFocusable`, las acciones sobre el foco, `MoveWindow`. `ShownDesktop` y `TextConsole` esperan al último paso: igual que `StreamKeyboard`, implementan o usan `Desktop`, `Keyboard` y `Console`, que están en la raíz de la API.
8. `text.menu`: la barra, el menú, la línea de estado, `EntryPlacer`/`EntryRow`/`PlacedEntry`/`PlacedTitle`, `KeyLabel`, `KeyCaption`, `MenuDesktop` y los painters de menú y estado.
9. `TextToolkit` pasa a `com.fpetrola.chuin.text`, con los imports de la demo, y `StreamKeyboard` pasa a `text.input`. `ShownDesktop` y `TextConsole` pasan a `text.desktop`. Así la raíz `com.fpetrola.chuin` queda solo para la API y las dependencias van en un solo sentido, de `text.*` hacia la API. Mover `TextToolkit` antes no corta el ciclo, porque `MenuDesktop` y las demás clases que todavía están en la raíz siguen ahí.
