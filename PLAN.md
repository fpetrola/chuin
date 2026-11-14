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

Superficie: probablemente ninguna, porque `Frame` y `Shadow` son de `tui-text`, salvo que se elija el tipo `Tint`.

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
