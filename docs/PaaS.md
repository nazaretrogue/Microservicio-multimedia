# Despliegue en un PaaS

## Tabla de contenidos
<!--ts-->
    * [Creación de la app en Heroku](#Creacion-de-la-app-en-Heroku)
    * [Despliegue](#Despliegue)
    * [Bibliografía](#Bibliografia)
<!--te-->

## Creación de la app en Heroku

Para poder desplegar en [*Heroku*](https://www.heroku.com/), lo primero es tener
una cuenta creada.

Si ya tenemos una, debemos instalar el CLI de *Heroku*. Para ello, ejecutamos
en la máquina local (con un SO Ubuntu 18.04):

```bash
sudo snap install heroku --classic
```

Esto instalará el CLI y nos permitirá ejecutar órdenes de *Heroku* para crear y
modificar la aplicación que vayamos a desplegar.

Tras esto, debemos loguearnos en la cuenta que tenemos de *Heroku* mediante el
CLI, ya que éste debe tener los credenciales para poder llevar a cabo las distintas
operaciones. Para ello, debemos ejecutar

```bash
heroku login
```

que abrirá el navegador para que nos logueemos dándole simplemente al botón de **Log in**.

Una vez hecho esto, creamos la aplicación que deseamos. En mi caso, hay que añadir
además un addon, el servidor de RabbitMQ en *Heroku*, llamado CloudAMQP, y usando
la versión **lemur** que es la gratuita y no cobran extra. No obstante, para añadir
el addon es necesario meter una tarjeta de crédito o débito aunque sea una cuenta
de estudiante, cosa que no es necesaria si no necesitas instalar nada extra. Para
crear la app e instalar el addon se hace lo siguiente:

```bash
heroku apps:create tratamientoimg
heroku addons:create cloudamqp:lemur
```

Con esto hemos creado un servicio *tratamientoimg* al que le hemos conectado RabbitMQ.

Puesto que es necesario conectarlo al servidor de RabbitMQ en *Heroku*, hay que
modificar ligeramente los archivos [*sender.py*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/src/sender.py)
y [*receiver.py*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/src/receiver.py).
Hablo de ella un poco más abajo. Dicha modificación se basa en la presencia de
una variable de entorno de *Heroku*: si dicha variable está presente significa
que hay que conectar la aplicación al usuario invitado de RabbitMQ en *Heroku*;
si no está, hay que conectarse en *localhost*. Para llevar esto a cabo se utiliza
la siguiente orden

```bash
heroku config:set HEROKU=1
```

que crea una variable ***HEROKU*** y establece su valor a 1 (el valor no es realmente
importante, solo es importante que la variable en sí exista). Además, en la página
de *Heroku* hay que configurar los *dynos* para que haya dos, el principal que
es la web, y el worker, en este caso el receiver, ya que aunque esté incluido en
el Procfile hay que habilitarlo manualmente en la web.

Una vez hecho esto solo hay que decirle a *Heroku* que suba los cambios para
desplegarlos:

```bash
git push heroku master
```

Al crear la aplicación, automáticamente se ha creado una rama local llamada heroku
y por ello hay que mergearla mediante la orden anterior. No obstante, podemos
configurar Heroku para que al hacer simplemente

```bash
git push
```

automáticamente se actualicen los cambios y se re-despliegue la aplicación con los
cambios. Para ello, dentro de la web de *Heroku*, buscamos nuestro proyecto y en
la sección de *Deploy* cambiámos el método a **GitHub**. En mi caso además he
seleccionado la casilla que da la opción de no desplegar la aplicación si no
pasa los tests de integración continua.

A pesar de que estos pasos se han explicado uno por uno, todo esto está agrupado
en un target de la herramienta de construcción, el [*Makefile*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Makefile):

```make
heroku:
	sudo snap install heroku --classic
	heroku login
	heroku apps:create tratamientoimg
	heroku addons:create cloudamqp:lemur
	heroku config:set HEROKU=1
	git push heroku master
```

## Despliegue

Para llevar a cabo el despliegue de la aplicación y del recibidor del bróker de
mensajería, es necesario indicarlo en un archivo llamado [Procfile](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Procfile).
Dicho archivo contiene lo siguiente:

```bash
web: make deploy
worker: python src/receiver.py
```

La directiva *web* indica la instrucción que se va a llevar a cabo para lanzar
la aplicación principal, que corresponde a una etiqueta del *makefile* explicada
[aquí](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/docs/Tecnologias_usadas.md#Makefile).
La directiva *worker* indica el proceso que debe ejecutar en segundo plano a la
vez que la aplicación principal, en este caso el recibidor de la cola de mensajes.

Para comprobar que ambos procesos están funcionando se utiliza

```bash
heroku ps
```

que mostrará una salida como:

```bash
=== web (Free): make deploy (1)
web.1: up 2019/11/13 22:05:17 +0100 (~ 2m ago)

=== worker (Free): python src/receiver.py (1)
worker.1: up 2019/11/13 22:05:03 +0100 (~ 2m ago)
```

Es decir, que ambos servicios están funcionando.

## Bibliografía

* [Addons en Heroku](https://devcenter.heroku.com/articles/cloudamqp)
