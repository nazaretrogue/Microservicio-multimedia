# Herramientas, servicios y tecnlogías utilizadas

Para poder llevar a cabo la funcionalidad del servicio correctamente, se han
utilizado dos bibliotecas externas, como se comentó en el README del repositorio.

Dichas bibliotecas están en formato .jar y son
[sm.image.color.jar](https://github.com/nazaretrogue/SMM/blob/master/Evaluacion/sm.image.color.jar)
y [sm.image.jar](https://github.com/nazaretrogue/SMM/blob/master/Evaluacion/sm.image.jar),
que contienen herramientas para gestionar los espacios de color y para los formatos
de las imágenes respectivamente.

Para pasar los tests sobre el código y comprobar que funciona como se espera y no
está "roto" se ha utilizado [Travis CI](https://travis-ci.org/), donde configurando
el archivo [*.travis.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/.travis.yml)
se prueba la aplicación con distintas versiones del *openJDK* para comprobar el
correcto funcionamiento en distintos sistemas.

Se ha incluido un segundo sistema de integración continua, [Shippable](https://app.shippable.com/),
donde se ha configurado el archivo [*shippable.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/shippable.yml)
para que, al igual que con *Travis*, haga tests en dos versiones distintas del *openJDK*
(en ambos sitemas se prueban las mismas versiones, *openJDK10* y *openJDK11*).

Se han utilizado este servicios de integración continua porque son fáciles de configurar y
de utilizar con GitHub. Además se ha incluido un *badget* en el README del repositorio
de ambos para poder comprobar de manera visual que ambos sistemas están funcionando
correctamente.

En el test implementado se comprueba que la imagen que se va a utilizar para
aplicar el filtro está en el formato y el espacio de color correctos (.jpeg/.jpg
y RGB respectivamente). Actualmente solo se comprueba una imagen extrayéndola de
la URL; una vez que se avance más en el proyecto, la imagen que se comprobará será
la que se envíe en la propia petición HTTP, por lo que habrá que modificar ligeramente
la implementación del test.
