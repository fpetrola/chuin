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
