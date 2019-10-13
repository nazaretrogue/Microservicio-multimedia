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

El archivo de configuración de *Travis* indica qué lenguaje (directiva ***language***) y en qué versiones se va a testear la aplicación (directiva ***jdk***). Puesto que el archivo para construir el programa utilizando *ant* es el *build.xml* y éste está en otro directorio que no es el principal, es necesario indicar en el archivo *.travis.yml* que instale ciertas dependencias opcionales para *ant* (para que todo funcione correctamente, con la directiva ***install***), y que, además,**antes de ejecutar el test** (directiva ***before_script*** en el archivo), que entre en el directorio donde está la herramienta de construcción para poder ejecutar correctamente los tests. Una vez ha entrado, al directorio, se ejecuta automáticamente *ant* utilizando el programa y los tests construidos mediante *build.xm*. Además, se ha prohibido que el microservicio se ejecute con permisos de administrador directiva ***sudo***).

```bash
language: java
jdk:
- openjdk10
- openjdk11
sudo: false
install:
- sudo apt install ant-optional
before_script:
- cd Microservicio
```

Se ha incluido un segundo sistema de integración continua, [Shippable](https://app.shippable.com/),
donde se ha configurado el archivo [*shippable.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/shippable.yml)
para que, al igual que con *Travis*, haga tests en dos versiones distintas del *openJDK*
(en ambos sitemas se prueban las mismas versiones, *openJDK10* y *openJDK11*).

En este caso, el archivo *shippable.yml* indica el lenguaje (mediante la directiva ***language***) y las versiones del *JDK* en las que se va a testear el microservicio (indicadas con la directiva ***jdk***). Al igual que con *Travis*, es necesario indicarle el directorio donde está la herramienta de construcción *build.xml*, por lo que hay que indicar que para construir la integración continua (directiva ***build: ci:***) es necesario que entre en el directorio donde está la herramienta y entonces ejecutar los tests.

```bash
language: java
jdk:
    - openjdk11
    - openjdk10
build:
    ci:
        - cd Microservicio
        - ant
```

Se han utilizado este servicios de integración continua porque son fáciles de configurar y
de utilizar con GitHub. Además se ha incluido un *badge* en el README del repositorio
de ambos para poder comprobar de manera visual que ambos sistemas están funcionando
correctamente.

En el test implementado se comprueba que la imagen que se va a utilizar para
aplicar el filtro está en el formato y el espacio de color correctos (.jpeg/.jpg
y RGB respectivamente). Actualmente solo se comprueba una imagen extrayéndola de
la URL; una vez que se avance más en el proyecto, la imagen que se comprobará será
la que se envíe en la propia petición HTTP, por lo que habrá que modificar ligeramente
la implementación del test.
