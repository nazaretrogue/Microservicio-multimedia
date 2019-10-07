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

Se ha utilizado este servicio de integración continua porque es fácil de configurar y
de utilizar con GitHub.

En el test implementado se comprueba que la imagen que se va a utilizar para
aplicar el filtro está en el formato y el espacio de color correctos (.jpeg/.jpg
y RGB respectivamente). Actualmente solo se comprueba una imagen extrayéndola de
la URL; una vez que se avance más en el proyecto, la imagen que se comprobará será
la que se envíe en la propia petición HTTP, por lo que habrá que modificar ligeramente
la implementación del test. 
