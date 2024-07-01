
# Chronos 2.0.0

Lanzamiento: 2024-07-01




## Features

- Soporte para scanner con cámara
- Soporte para alternar entre cámara frontal y trasera
- Switch para alternar entre código encriptado agregado
- Diseño para lector de códigos de barras
- Nuevos Switch agregados
    - Permitir encriptado
        - Hace posible la lectura de los códigos encriptados con la estructura San Juan cuando el usuario lo decida
    - Mostrar Escaner
        - Muestra el escaner en lugar del teclado
    - Usar cámara frontal
        - usa la cámara frontal en lugar de la trasera para lecturar los códigos
- Longitud de DNI dejó de estár deshabilitada
    - Ahora se toma en cuenta, 8 para DNI y 10 para código con estructura San Juan

## Fixed bugs
- Se reparó la funcionalidad para obtener el nombre del dispositivo al momento de realizar la configuración inicial
- Se reparó la lectura de códigos con lector de barras físico
    - Ya no se presionan botones al azar al momento de lecturar

