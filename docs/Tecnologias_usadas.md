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

```make
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

heroku:
	sudo snap install heroku --classic
	heroku login
	heroku apps:create tratamientoimg
	heroku addons:create cloudamqp:lemur
	heroku config:set HEROKU=1
	git push heroku master

deploy: create_environment
	gunicorn app:app

container-build:
	heroku stack:set container
	git push heroku master
	docker build -t tratamientoimg

container-run: container-build
	docker run -p 5000:5000 tratamientoimg

vm-vagrant:
  vagrant up

vm-provision: vm-vagrant
	ansible-playbook playbook.yml

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
* *reload*: recarga el receiver y la aplicación sin detenerla; vuelve a cargar paquetes,
dependencias... en memoria pero sin parar los procesos.
* *delete*: elimina los procesos lanzados del gestor de procesos, el receiver y la
aplicación.
* *test*: ejecuta los tests con la herramienta pytest de Python.
* *heroku*: ejecuta las instrucciones necesarias para crear la aplicación en *Heroku*.
La explicación a dichas instrucciones está [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/PaaS).
* *deploy*: despliega la aplicación principal en *Heroku*. Esta directiva es llamada
desde el [Procfile](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Procfile).
Para desplegarla, depende de que el entorno virtual esté creado. Cuando está creado
se ejecuta *Gunicorn*, un servidor de interfaz puerta-enlace para Python.
* *container-build*: construye el contenedor para *Heroku*. Para ello, primero establece
una pila en forma de contenedor para indicar que el despliegue será a través de
un contenedor. Esto hace que *Heroku* busque un archivo [heroku.yml](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/heroku.yml)
para ejecutar el contenedor. La documentación de dicho archivo está [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/docker).
Tras
esto se hace un push a nuestro repositorio (realmente la orden es un git push normal
ya que tenemos configurado *Heroku* para que se redespliegue al hacer push a nuestro
repositorio). Por último, construimos el contenedor con build.
* *container-run*: levanta el contenedor para dar servicio, en el puerto 5000.
Depende de la etiqueta *container-build* ya que sin construir el contenedor no
lo vamos a poder lanzar.
* *vm-vagrant*: crea una máquina local provisionada con Ansible mediante Vagrant.
La máquina creada se levanta mediante las pautas indicadas en el archivo del
Vagrantfile(https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Vagrantfile).
* *vm-provision*: utiliza Ansible para provisionar la máquina virtual indicada
en el [playbook.yml](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/playbook.yml).
Es necesario que la máquina esté creada, motivo por el cual depende de la etiqueta
de levantamiento de la máquina con Vagrant.

### Travis CI

El archivo de configuración de *Travis* indica qué lenguaje (directiva ***language***)
y en qué versiones se va a testear la aplicación (directiva ***python***).

Antes de comenzar a explicar este archivo o el de *Shippable*, cabe destacar que
no se ha definido ningún puerto mediante variables de entorno ya que Flask establece
un puerto por defecto, el 5000, por lo que no ha sido necesario establecerlo.

Como se tiene que utilizar un bróker de mensajería en el microservicio, se instala
dicho bróker en *Travis* (concretamente se instala *RabbitMQ Server*
que todo funcione correctamente, con la directiva ***install***). Además, debe
ejecutar el *Makefile* para que se instale el entorno virtual y se ejecuten los tests.
También, **antes de ejecutar el test** (directiva ***before_script*** en el
archivo), debe instalar los módulos de Python indicados en el archivo
[requirements.txt](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/requirements.txt).

Una vez hecho esto, la ejecución (directiva ***script***) que inicia la aplicación
y el receiver, pasa los tests y elimina la aplicación el receiver una vez que ha
terminado la ejecución de dichos tests.

Por último, se ha prohibido que el microservicio se ejecute con permisos de administrador
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
En este caso, solo se prueba la versión 3.7.0 de Python, debido a que hay distintas
versiones disponibles de los lenguajes según el *node pool* que se establezca.
Cualquier otra versión de Python y la versión 3.7.* cambian de pool, por lo que
no puedo probar más de una versión. Solo está permitido tener un *node pool* a
menos que pagues por tener más.

En este caso, el archivo *shippable.yml* indica el lenguaje (mediante la
directiva ***language***) y las versiones de Python en las que se va a testear
el microservicio (indicadas con la directiva ***python***). Al igual que ocurre
en *Travis*, hay que indicarle que instale ciertas cosas antes del testeo, como
que instale el servidor del bróker de mensajería, instale el gestor de procesos y
la herramienta para construir el entorno virtual, construya dicho entorno e instale
la herramienta para los tests (todo esto se indica en la directiva
***build: ci:***). Solo entonces, se iniciarán los servicios y se ejecutarán los tests.

El archivo de *Shippable* es este:

```yaml
language: python
python:
    - "3.7"
build:
    ci:
        - sudo apt-get install rabbitmq-server
        - make
        - pip install -r requirements.txt
        - make start
        - make test
```

Se han utilizado este servicios de integración continua porque son fáciles de configurar y
de utilizar con GitHub. Además se ha incluido un *badge* en el README del repositorio
de ambos para poder comprobar de manera visual que ambos sistemas están funcionando
correctamente.

En el caso de Shippable, no funciona debido a que no encuentra el path a la versión
de Python. He re-abierto un [issue](https://github.com/Shippable/support/issues/4978)
en GitHub preguntando sobre el fallo pero aún estoy esperando una respuesta.

## ¿Qué hace el test?

Se comprueban dos cosas diferentes en los tests funcionales: primero, que el
servicio está funcionando correctamente (hace un get comprobando el status).

El segundo, comprueba que devuelve correctamente una imagen ya procesada.

Debería haber un tercer test que comprobara que se envía correctamente una imagen
para ser procesada. No obstante, utilizar un test era problemático: la codificación
de la multimedia con un objeto request en Python no era aceptado por los procedimientos
internos del bróker de mensajería. No obstante, utilizando el comando *curl*
desde un directorio donde esté la imagen a enviar funciona correctamente. Por
ejemplo con el siguiente:

```bash
curl -i -H "Content-Type: multipart/form-data" -F "data=@fry.jpg" -XPOST localhost:5000/
```

## Bibliografía

* [pm2 y Python](https://medium.com/@gokhang1327/deploying-flask-app-with-pm2-on-ubuntu-server-18-04-992dfd808079)
* [RabbitMQ y pika](https://paulcrickard.wordpress.com/2013/04/17/messaging-in-python-with-rabbitmq-and-pika/)
* [Documentando con Sphinx](https://developer.ridgerun.com/wiki/index.php/How_to_generate_sphinx_documentation_for_python_code_running_in_an_embedded_system)
