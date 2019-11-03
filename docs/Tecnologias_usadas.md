# Herramientas, servicios y tecnlogías utilizadas

## Tabla de contenidos
<!--ts-->
   * [Ejecución de tests y herramientas de construcción](#Ejecución-de-tests-y-herramientas-de-construccion)
   * [Uso de un *Makefile*](#Uso-de-un-Makefile)
        * [*Makefile*](#Makefile)
        * [Travis CI](#Travis-CI)
        * [Shippable CI](#Shippable-CI)
   * [¿Qué hace el test?](#¿Que-hace-el-test)
   * [Bibliografía](#Bibliografia)
<!--te-->

## Ejecución de tests y herramientas de construcción

Para pasar los tests sobre el código y comprobar que funciona como se espera y no
está "roto" se ha utilizado [Travis CI](https://travis-ci.org/), donde configurando
el archivo [*.travis.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/.travis.yml)
se prueba la aplicación con distintas versiones de Python para comprobar el
correcto funcionamiento en distintos sistemas que puedan tener versiones más antiguas
o más nuevas. Se prueban las dos versiones últimas estables, es decir, la 3.6 y 3.7.
No se prueba la versión 2 porque le quedan pocos meses para estar deprecated, y no
se prueban la 3.4 o 3.5 porque cambian poco (o nada) respecto a las versiones que
se están testeando.

Para configurar *Travis* y *Shippable*, hay que registrarse en la plataforma directamente
desde *GitHub* y darle acceso al repo que se va a probar.

## Uso de un *Makefile*

La herramienta de construcción utilizada es el *makefile*, una herramienta de
construcción genérica, que se puede usar en cualquier lenguaje y con una
sintaxis sencilla basada en etiquetas.

### *Makefile*

```bash
```

### Travis CI

El archivo de configuración de *Travis* indica qué lenguaje (directiva ***language***)
y en qué versiones se va a testear la aplicación (directiva ***python***). Como
se tiene que utilizar un bróker de mensajería en el microservicio, se instala
dicho bróker en *Travis* (concretamente se instala *RabbitMQ Server*
que todo funcione correctamente, con la directiva ***install***). Además, debe
ejecutar el *Makefile* para que se instale el entorno virtual y se ejecuten los tests.
También, **antes de ejecutar el test** (directiva ***before_script*** en el
archivo), debe instalar los módulos de Python indicados en el archivo
[requirements.txt](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/requirements.txt).
Una vez hecho esto, se lanza el receiver del bróker de mensajería para que esté
a la escucha de peticiones encoladas que atender.

Además, se ha prohibido que el microservicio se ejecute con permisos de administrador
(directiva ***sudo***, esto no significa que la instalación de dependencias previa
no se pueda hacer, se ha prohibido el uso del microservicio con sudo, no el uso
en general de sudo).

El archivo de *Travis* es el siguiente:

```yaml
language: python
python:
- "3.6"
- "3.7"
sudo: false
install:
- sudo apt-get install rabbitmq-server
- make
before_script:
- pip install -r requirements.txt
- make exec_mess_broker
```

### Shippable CI

Se ha incluido un segundo sistema de integración continua, [Shippable](https://app.shippable.com/),
donde se ha configurado el archivo [*shippable.yml*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/shippable.yml)
para que, al igual que con *Travis*, haga tests en dos versiones distintas del lenguaje
(en ambos sistemas se prueban las mismas versiones, *3.6* y *3.7*, por los motivos
explicados anteriormente).

En este caso, el archivo *shippable.yml* indica el lenguaje (mediante la
directiva ***language***) y las versiones de Python en las que se va a testear
el microservicio (indicadas con la directiva ***python***). Al igual que ocurre
en *Travis*, hay que indicarle que instale ciertas cosas antes del testeo, como
el bróker, los módulos del lenguaje o la ejecución previa del receiver del bróker
para escuchar las peticiones que pueda haber (todo esto se indica en la directiva
***build: ci:***). Solo entonces, ejecutará los tests.

El archivo de *Shippable* es este:

```yaml
language: python
python:
    - 3.6.5
    - 3.7.2
build:
    ci:
        - sudo apt-get install rabbitmq-server
        - make
        - pip install -r requirements.txt
        - make exec_mess_broker
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
