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
sintaxis sencilla basada en etiquetas. El contenido es el siguiente:

### *Makefile*

```bash
all: process_mng install create_environment install_tester

process_mng:
	npm install pm2@latest -g

install:
	pip install virtualenv

create_environment:
	( \
		chmod +x script.sh; \
		./script.sh; \
	)

install_tester:
	pip install pytest-virtualenv

start:
	pm2 start src/receiver.py
	pm2 start app.py

stop:
	pm2 stop src/receiver.py
	pm2 stop app.py

restart:
	pm2 restart src/receiver.py
	pm2 restart app.py

reload:
	pm2 reload src/receiver.py
	pm2 reload app.py

delete:
	pm2 delete src/receiver.py
	pm2 delete app.py

test:
	pytest
```

Vamos a explicar qué hace cada etiqueta.

* *all*: ejecuta las etiquetas *process_mng*, *install*, *create_environment* e
*install_tester* cuando se ejecuta el comando **make**.
* *process_mng*: instala el gestor de procesos para mantener viva la aplicación
que se despliega.
* *install*: instala el entorno virtual de Python.
* *create_environment*: da permisos de ejecución y ejecuta un script de bash que
crea el entorno virtual y lo activa. Tiene que hacerse así porque cada orden de
un makefile se ejecuta en un terminal diferente, es decir, que no puede depender
una orden de otra. Por ese motivo he tenido que crear el entorno y activarlo en
un script de bash aparte donde sí se pueden ejecutar órdenes dependientes de otras.
Se puede ver el script de bash en este [enlace](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/script.sh).
* *install_tester*: instala la herramienta pytest para poder ejecutar los tests
de la aplicación.
* *start*: lanza la aplicación mediante el gestor de procesos pm2. Se debe lanzar
el receiver del bróker de mensajería para que reciba peticiones y la propia aplicación,
en este orden.
* *stop*: detiene los procesos que se han lanzado, tanto el receiver como la aplicación
en sí.
* *restart*: detiene e inicia de nuevo el receiver y la aplicación.
* *reload*: recarga el receiver y la aplicación sin deterla; vuelve a cargar paquetes,
dependencias... en memoria pero sin parar los procesos.
* *delete*: elimina los procesos lanzados del gestor de procesos, el receiver y la
aplicación.
* *test*: ejecuta los tests con la herramienta pytest de Python.

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
a la escucha de peticiones encoladas que atender. Por último, la ejecución (directiva
***script***) que inicia la aplicación y el receiver, pasa los tests y elimina
la aplicación el receiver una vez que ha terminado la ejecución de dichos tests.

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
script:
- make start
- make test
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
