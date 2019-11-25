# Despliegue usando Virtualización ligera: contenedores

## Tabla de contenidos
<!--ts-->
   * [*Dockerfile*](#Dockerfile)
   * [*heroku.yml*](#heroku.yml)
   * [Bibliografía](#Bibliografia)
<!--te-->

## *Dockerfile*

El archivo para configurar el contenedor es el *Dockerfile*, que contiene las
indicaciones necesarias para instalar, ejecutar, establecer un entorno de trabajo
en el contenedor...

El archivo es el siguiente:

```bash
FROM rabbitmq:latest

RUN mkdir ./src
RUN mkdir ./templates
WORKDIR .

ENV FLASK_RUN_HOST 0.0.0.0
EXPOSE 5000

RUN apt-get update && apt-get upgrade -y && apt-get install -y python3 python3-pip
RUN update-alternatives --install /usr/bin/python python /usr/bin/python3 1\
    && update-alternatives --install /usr/bin/pip pip /usr/bin/pip3 1

COPY requirements.txt requirements.txt

RUN pip3 install -r requirements.txt

COPY src/ ./src
COPY templates/ ./templates
COPY app.py app.py

RUN python src/receiver.py &

CMD gunicorn --bind ${FLASK_RUN_HOST}:$PORT app:app
```

Vamos a explicarlo instrucción a instrucción.

* La primera línea (***FROM rabbitmq:latest***) indica la imagen base que vamos
a utilizar, en mi caso, la última versión de la imagen que contiene *RabbitMQ*.
No es una versión ligera ya que la instalación del bróker en la versión de
python:3.7-alpine, una versión ligera de solo 100MB de python, daba bastantes
problemas. Por tanto, he utilizado está, que a pesar de no ser ligera, son solo
600MB y 1GB como hay otras imágenes de python.
* La segunda y tercera línea (***RUN mkdir ./src***, ***RUN mkdir ./templates***)
crean dos directorios con la orden *mkdir*: se crea un directorio para los
archivos fuente y otro para las templates de HTML.
* La cuarta línea (***WORKDIR .***) indica el directorio de trabajo, el actual. Esta directiva toma
dicho directorio como la base de todas las órdenes que se llevarán a cabo de aquí
en adelante.
* La quinta línea (***ENV FLASK_RUN_HOST 0.0.0.0***) establece una variable de
entorno con la dirección en la que se ejecutará la aplicación, en este caso 0.0.0.0.
* La sexta línea (***EXPOSE 5000***) indica a *Docker* el puerto en el que se
debe ejecutar la app. Realmente esta línea no efectua ninguna instrucción, sino
que es meramente ilustrativa de cara a documentar el código. Es decir, nuestra
aplicación puede que no se ejecute en el puerto 5000, ya que esta instrucción
solo sirve para informar.
* La séptima y octava línea (***RUN apt-get...***) llevan a cabo la instalación
de distintos paquetes y dependencias de python en la imagen base que teníamos.
* La novena línea (***COPY requirements.txt requirements.txt***) copia el archivo
[requirements.txt](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/requirements.txt)
al directorio de trabajo definido previamente, creando otro archivo con el mismo nombre.
* La décima línea (***RUN pip3 install -r requirements.txt***) se encarga de
instalar los paquetes de python que necesitamos y que están en el archivo que
acabamos de copiar con la orden anterior.
* Las líneas undécima, duodécima y decimotercera (***COPY...***) se encargan de
copiar los archivos fuente, las templates y el archivo que contiene la API en sus
respectivos directorios, creados previamente. El archivo app.py se copia en un
archivo que se crea con el mismo nombre.
* La penúltima línea (***RUN python src/receiver.py &***) lanza el recibidor del
bróker de mensajería, que se conecta con *RabbitMQ*, que viene instalado por
defecto en la imagen base escogida.
* Por último, la línea final (***CMD gunicorn --bind ${FLASK_RUN_HOST}:$PORT app:app***)
indica la orden que se ejecutará cuando se levante el servicio. Se ejecutará
*gunicorn* bindeando la dirección de la app que hemos definido antes con la
variable de entorno con el puerto que nos da *Heroku* mediante la variable de entorno
*$PORT*. Nótese que dicho puerto no tiene que ser el indicado con la orden *EXPOSE*,
por eso es necesario utilizar la variable de entorno y no poner el puerto a mano.

## *heroku.yml*

El archivo utilizado por *Heroku* cuando se le pide el despliegue con contenedores
mediante la orden *heroku stack:set container* (explicada [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/Tecnologias_usadas))
es el siguiente:

```yaml
build:
    docker:
        web: Dockerfile
```

La orden *build* indica qué es lo que se va a construir, en este caso, un contenedor,
indicado con la directiva *docker*. Dentro del contenedor se va a lanzar un solo
proceso web, indicado con la directiva *web*, y cuya configuración está en el
archivo *Dockerfile* anteriormente explicado.
