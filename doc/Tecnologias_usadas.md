# Herramientas, servicios y tecnlogías utilizadas

## Tabla de contenidos
<!--ts-->
   * [Bibliotecas externas](#Bibliotecas-externas)
   * [Ejecución de tests y herramientas de construcción](#Ejecución-de-tests-y-herramientas-de-construccion)
        * [Problemas con las herramientas de construcción](#Problemas-con-las-herramientas-de-construccion)
   * [Uso de *ant*](#Uso-de-ant)
        * [Travis CI](#Travis-CI)
        * [Shippable CI](#Shippable-CI)
   * [¿Qué hace el test?](#¿Que-hace-el-test)
   * [Bibliografía](#Bibliografia)
<!--te-->

## Bibliotecas externas

Para poder llevar a cabo la funcionalidad del servicio correctamente, se han
utilizado dos bibliotecas externas, como se comentó en el README del repositorio.

Dichas bibliotecas están en formato .jar y son
[sm.image.color.jar](https://github.com/nazaretrogue/SMM/blob/master/Evaluacion/sm.image.color.jar)
y [sm.image.jar](https://github.com/nazaretrogue/SMM/blob/master/Evaluacion/sm.image.jar),
que contienen herramientas para gestionar los espacios de color y para los formatos
de las imágenes respectivamente.

## Ejecución de tests y herramientas de construcción

Para pasar los tests sobre el código y comprobar que funciona como se espera y no
está "roto" se ha utilizado [Travis CI](https://travis-ci.org/), donde configurando
el archivo [*.travis.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/.travis.yml)
se prueba la aplicación con distintas versiones del *openJDK* para comprobar el
correcto funcionamiento en distintos sistemas que puedan tener versiones más antiguas
o más nuevas. No se prueba la 7 o la 8 porque están cerca de estar ya deprecated.
Para configurar *Travis* y *Shippable*, hay que registrarse en la plataforma directamente
desde *GitHub* y darle acceso al repo que se va a probar.

### Problemas con las herramientas de construcción
Antes de explicar la configuración de la herramienta de construcción y la herramienta
para ejecutar tests, cabe destacar que comencé utilizando *maven*; al igual que *ant*
es una herramienta de construcción para los proyectos de *Java*. Para construir
proyectos utiliza el archivo *pom.xml*.

No obstante, tuve que cambiar a *ant* porque mi proyecto utiliza bibliotecas externas
a *Java* y enlazarlas con *maven* era, siendo sincera, una pesadilla; me vi en el
infierno para intentar que funcionara con *maven* y al final no lo conseguí. Las dependencias
de *maven* hacen difícil enlazar bibliotecas manualmente por lo que decidí cambiar de
herramienta. Utilizando el IDE de *netbeans*, generar archivos *ant* que enlacen
las bibliotecas externas es un proceso trivial.

No obstante, con *maven* el uso del framework de *jUnit* para los tests no requería
de ningún esfuerzo puesto que todo funcionaba correctamente. Con *ant* ha sido
necesario enlazar manualmente una biblioteca extra, [jUnit-4.12.jar](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/lib/junit-4.12.jar)
puesto que, de otro modo, *Travis* utilizaba una versión antigua con la que no funcionaba
la ejecución de los tests. A pesar de ser una biblioteca que viene por defecto
con el *JDK*, ha habido que enlazarla, motivo por el cual está subida en el repositorio.

## Uso de *ant*

### Travis CI

El archivo de configuración de *Travis* indica qué lenguaje (directiva ***language***)
y en qué versiones se va a testear la aplicación (directiva ***jdk***). Puesto
que el archivo para construir el programa utilizando *ant* es el *build.xml* y
éste está en otro directorio que no es el principal, es necesario indicar en el
archivo *.travis.yml* que instale ciertas dependencias opcionales para *ant* (para
que todo funcione correctamente, con la directiva ***install***), y que,
además, **antes de ejecutar el test** (directiva ***before_script*** en el
archivo), entre en el directorio donde está la herramienta de construcción
para poder ejecutar correctamente los tests. Una vez ha entrado al
directorio, se ejecuta automáticamente *ant* utilizando el programa y
los tests construidos mediante [*build.xml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Microservicio/build.xml).
Además, se ha prohibido que el microservicio se ejecute con permisos de administrador
(directiva ***sudo***, esto no significa que la instalación de dependencias previa
no se pueda hacer, se ha prohibido el uso del microservicio con sudo, no el uso
en general de sudo).

El archivo de *Travis* es el siguiente:

```yaml
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

### Shippable CI

Se ha incluido un segundo sistema de integración continua, [Shippable](https://app.shippable.com/),
donde se ha configurado el archivo [*shippable.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/shippable.yml)
para que, al igual que con *Travis*, haga tests en dos versiones distintas del *openJDK*
(en ambos sistemas se prueban las mismas versiones, *openJDK10* y *openJDK11*, por los motivos
explicados anteriormente).

En este caso, el archivo *shippable.yml* indica el lenguaje (mediante la
directiva ***language***) y las versiones del *JDK* en las que se va a testear
el microservicio (indicadas con la directiva ***jdk***). Al igual que con
*Travis*, es necesario indicarle el directorio donde está la herramienta de
construcción *build.xml*, por lo que hay que indicar que para construir el proyecto con
integración continua (directiva ***build: ci:***) es necesario que entre en
el directorio donde está la herramienta y, solo entonces, ejecutar los tests.

El archivo de *Shippable* es este:

```yaml
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

## ¿Qué hace el test?

En el test implementado se comprueba que la imagen que se va a utilizar para
aplicar el filtro está en el formato y el espacio de color correctos (.jpeg/.jpg
y RGB respectivamente). Actualmente solo se comprueba una imagen extrayéndola de
la URL; una vez que se avance más en el proyecto, la imagen que se comprobará será
la que se envíe en la propia petición HTTP, por lo que habrá que modificar ligeramente
la implementación del test.

## Bibliografía

    * [Herramienta de construcción: Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
    * [Dependecias externas en maven](https://stackoverflow.com/questions/28703491/how-can-i-add-a-3rd-party-jar-to-my-travis-ci-maven-build)
    * [Travis usa ant en lugar de maven](https://stackoverflow.com/questions/53180531/travis-uses-ant-instead-of-maven)
    * [Dependecias externas en ant](https://www.mkyong.com/ant/ant-how-to-create-a-jar-file-with-external-libraries/)
    * [Integración continua en Travis CI](https://docs.travis-ci.com/user/languages/java/)
    * [Tests en Java. Uso de jUnit](https://dev.to/chrisvasqm/introduction-to-unit-testing-with-java-2544)
    * [jUnit con ant](http://ant.apache.org/manual/Tasks/junit.html)
