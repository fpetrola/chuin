# Decisiones de diseño

Registro corto de por qué el diseño es como es. Una entrada por decisión, la más nueva abajo.

1. **Visitors en vez de casts.** Vistas (`ViewVisitor`), teclas (`KeyVisitor`) y marcas (`MarkVisitor`) se distinguen visitándolas; nadie pregunta el tipo.
2. **El estado se visita, no se ramifica.** Un check box entrega un `Mark` visitable en vez de un `boolean` (regla `flag-argument` y pedido de Fernando).
3. **El foco es un índice en `TextDesktop`.** Cada acción sobre el foco es un visitor que cuenta vistas enfocables (`PressFocused`, `ToggleFocused`, `TypeIntoFocused`).
4. **Un solo paquete, tres módulos.** `tui-api` (lo sellado), `tui-text` (implementación), `tui-demo`.
5. **Retroceso (0x7F) como tecla propia.** `KeyVisitor#backspace()`; la línea de texto entrega un `Runnable erase` junto al `Consumer<Character> type`, simétrico a como el check box entrega `toggle`. Propuesta 0014.
6. **`AtFocus` cuenta, las acciones actúan.** El conteo de vistas enfocables vive en un solo visitor que reenvía solo la vista con foco a una acción (`PressFocused`, `ToggleFocused`, `TypeIntoFocused`, `EraseFocused`). Sale de `no-cloned-methods`: cada acción repetía el mismo `seen++`.
7. **Borrar en una línea vacía no hace nada.** Pedido por cerco-tdd-97 al revisar 0014; lo fija `ErasingWithBackspaceTest`.
8. **Las flechas mueven la ventana de arriba.** Una celda por tecla, con `KeyVisitor#up/down/left/right`. Se mueve con `Window#place`, que ya estaba sellado, así que `Window` no crece. Cuando haya widgets que usen flechas (listas, scroll), habrá que decidir un modo "mover ventana" como el Ctrl+F5 de Turbo Vision. Propuesta 0015.
9. **El teclado lee `ESC [ A..D`.** Por ahora, un ESC suelto espera el byte siguiente. Si hace falta un ESC solo, habrá que leerlo con un timeout.
10. **Una ventana puede salirse de la pantalla y la pantalla la recorta.** No se la frena en el borde: el desktop no conoce el tamaño de la pantalla, y Turbo Vision también deja ventanas a medio salir. `TextScreen#write` recorta lo que cae afuera, en vez de reventar. Lo pidió cerco-tdd-97 al revisar 0015.
11. **Las secuencias de escape desconocidas no escriben nada.** El teclado lee `ESC [`, luego dígitos y `;`, y termina en el primer byte que no sea uno de esos. Si la secuencia no es una flecha, es `other`. Así Home (`ESC [ H`) y Supr (`ESC [ 3 ~`) no dejan basura en una línea de texto.
12. **Las flechas son un enum que es `Key`.** cerco no deja un `switch` sobre el byte final (`no-switch`) ni un método estático de búsqueda. `Arrow` sigue el orden de los bytes A..D, y el teclado indexa con `byte - 'A'`.
13. **La sombra es de caracteres (`░`).** Una celda a la derecha y una abajo de cada ventana. No hace falta tocar `Screen`, que solo guarda caracteres. Cuando haya colores, la sombra puede pasar a ser un atributo oscurecido, como en Turbo Vision.
14. **El color se guarda como un tinte por celda, no como código ANSI.** `Screen#paint` y `Screen#tints` manejan letras de una paleta: `w` ventana, `f` vista con foco, `s` sombra, `.` desktop. Así los tests leen los colores como leen las líneas, y solo la terminal sabe de ANSI. La paleta está documentada en el javadoc de `Screen#tints`. Propuesta 0016.
15. **Colores ANSI a la Turbo Vision.** Desktop: gris sobre azul. Ventana: negro sobre gris. Foco: blanco sobre verde. Sombra: gris oscuro sobre negro. La terminal emite un código de color solo al empezar cada fila y cuando el tinte cambia, y cierra con `ESC [0m` para dejar la consola como estaba.
16. **El desktop limpia la pantalla antes de dibujar.** Escribe blancos y pinta `.` en toda la pantalla. La consola reusa la misma `Screen` en cada tecla, y sin esto una ventana movida dejaba rastro. Lo encontró cerco-tdd-97.
