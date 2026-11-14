# Decisiones de diseño

Registro corto de por qué el diseño es como es. Una entrada por decisión, la más nueva abajo.

1. **Visitors en vez de casts.** Vistas (`ViewVisitor`), teclas (`KeyVisitor`) y marcas (`MarkVisitor`) se distinguen visitándolas; nadie pregunta el tipo.
2. **El estado se visita, no se ramifica.** Un check box entrega un `Mark` visitable en vez de un `boolean` (regla `flag-argument` y pedido de Fernando).
3. **El foco es un índice en `TextDesktop`.** Cada acción sobre el foco es un visitor que cuenta vistas enfocables (`PressFocused`, `ToggleFocused`, `TypeIntoFocused`).
4. **Un solo paquete, tres módulos.** `tui-api` (lo sellado), `tui-text` (implementación), `tui-demo`.
5. **Retroceso (0x7F) como tecla propia.** `KeyVisitor#backspace()`; la línea de texto entrega un `Runnable erase` junto al `Consumer<Character> type`, simétrico a como el check box entrega `toggle`. Propuesta 0014.
6. **`AtFocus` cuenta, las acciones actúan.** El conteo de vistas enfocables vive en un solo visitor que reenvía solo la vista con foco a una acción (`PressFocused`, `ToggleFocused`, `TypeIntoFocused`, `EraseFocused`). Sale de `no-cloned-methods`: cada acción repetía el mismo `seen++`.
7. **Borrar en una línea vacía no hace nada.** Pedido por cerco-tdd-97 al revisar 0014; lo fija `ErasingWithBackspaceTest`.
