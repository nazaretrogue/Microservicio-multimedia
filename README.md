# Microservicio de tratamiento de imágenes
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/nazaretrogue/Microservicio-multimedia.svg?branch=master)](https://travis-ci.org/nazaretrogue/Microservicio-multimedia)
[![Run Status](https://api.shippable.com/projects/5d9d9d8227d7a00007532757/badge?branch=master)]()

## Tabla de contenidos
<!--ts-->
   * [¿Qué es y para qué sirve?](#¿Que-es-y-para-que-sirve?)
   * [¿Qué se usará?](#¿Que-se-usara?)
   * [¿Por qué crear este microservicio?](#¿Por-que-crear-este-microservicio?)
   * [¿Qué hace exactamente el filtro del microservicio?](#¿Que-hace-exactamente-el-filtro-del-microservicio?)
   * [Cómo instalar y testear la aplicación en local](#Como-instalar-y-testear-la-aplicacion-en-local)
<!--te-->

## ¿Qué es y para qué sirve?

Se trata de un microservicio que se va a encargar de recibir una imagen en formato
RGB y aplicar en ella un filtro termal que mantendrá los colores rojizos,
anaranjados o cualquier color cálido que tenga una potencia de color igual o
superior a 128 en la banda R de color. El resto de la imagen quedará en escala de
grises.

## ¿Qué se usará?

La idea principal es implementar el filtro en Java, utilizando bibliotecas propias
del lenguaje, concretamente del paquete *awt*. Puesto que es necesario utilizar
un sistema de log para controlar todo lo que ocurra en el microservicio, utilizaré
*logstash* o *fluentd*, ya que ambos son open source y se adaptan a lo que necesito,
pero aún no he decidido cuál de los dos utilizaré.

En cuanto a middleware de mensajería utilizaré *RabbitMQ*, que tiene bibliotecas
para Java. Lo utilizaré junto con *springboot* para poder desplegar el microservicio
de Java en la nube. El compilador utilizado por Java es el *JDK*.

Se necesitarán dos bibliotecas externas, disponibles en el repositorio de
[sistemas multimedia](https://github.com/nazaretrogue/SMM/tree/master/Evaluacion).
Se explican más detallada en la [documentación](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/doc/Tecnologias_usadas.md).

No será necesaria una base de datos donde almacenar las imágenes puesto que se enviarán
directamente en los mensajes HTTP.

## ¿Por qué crear este microservicio?
En una asignatura de otro año nos enseñaron a utilizar en profundidad los paquetes
que se encargan de multimedia en Java, siendo sencillo de escribir en código pero
consiguiendo unos resultados muy visuales. Me gustó tanto que decidí que quería
hacer un proyecto utilizando lo que aprendí, y de ahí surgió la idea de hacer
un microservicio de tratamiento de imágenes.  

Se ha creado un caso hipotético donde un cliente desea que se implemente el filtro
comentado para su empresa. Para ello, se hace una entrevista que se puede leer
[aquí](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/doc/Historia_usuario.pdf).

## ¿Qué hace exactamente el filtro del microservicio?

Un ejemplo del funcionamiento del filtro se puede encontrar en la documentación presente
[aquí](https://github.com/nazaretrogue/SMM/tree/master/Evaluacion). Para probar
el propio filtro solo hay que seguir las [instrucciones](https://github.com/nazaretrogue/SMM.git)
de instalación detalladas en el repositorio, abrir una imagen en la aplicación
y aplicar el filtro termal sobre dicha imagen.  

La [implementación del filtro](https://github.com/nazaretrogue/Microservicio-multimedia/tree/master/src)
está disponible en este repositorio.

## Cómo instalar y testear la aplicación en local

Para poder testear la aplicación, es necesario tener instalado el *JDK* con el framework
de *jUnit*, y tener instalado *ant* en el local. Todo esto, y la funcionalidad del
test está explicado con más detalle en la [documentación](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/doc/Tecnologias_usadas.md).

Una vez que estén instalados (y por supuesto, que tengamos el repositorio clonado en
la máquina local), ejecutamos el script de bash que hay en el directorio principal,
[build.sh](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/build.sh).
Este script se encarga de entrar al directorio donde está la aplicación principal y
ejecutar el test mediante el comando *ant*; para ello, solo hay que ejecutar dicho script con

```bash
$ ./build.sh
```

Con esto, se pasará el test automáticamente y la salida por pantalla mostrará si
el test ha sido exitoso o no.
