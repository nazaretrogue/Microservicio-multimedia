# Funcionamiento del filtro

Vamos a hacer una pequeña demostración de cómo debe funcionar el filtro que
se va a aplicar en las imágenes que lleguen a nuestro microservicio.

La idea es que nos llegue una imagen en un formato *.jpg*, *.jpeg* o *.png*, por
ejemplo, nos llega la siguiente imagen:

![Imagen de Fry](img/fry.jpg)

Al aplicar el filtro, la imagen quedará en escala de grises, tal y como se puede
ver en la siguiente imagen, que ha sido procesada por el filtro:

![Imagen procesada](img/procesado.png)

La imagen devuelta estará en formato *.png*.
