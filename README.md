# Microservicio de tratamiento de imágenes
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/nazaretrogue/Microservicio-multimedia.svg?branch=master)](https://travis-ci.org/nazaretrogue/Microservicio-multimedia)

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

No será necesaria una base de datos donde almacenar las imágenes puesto que se enviarán
directamente en los mensajes HTTP.

## ¿Por qué crear este microservicio?
En una asignatura de otro año nos enseñaron a utilizar en profundidad los paquetes
que se encargan de multimedia en Java, siendo sencillo de escribir en código pero
consiguiendo unos resultados muy visuales. Me gustó tanto que decidí que quería
hacer un proyecto utilizando lo que aprendí, y de ahí surgió la idea de hacer
un microservicio de tratamiento de imágenes.  

Se ha creado un caso hipotético donde un cliente desea que se implemente el filtro
comentado para su empresa. Para ello, se hace una entrevista que se puede leer [aquí](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/doc/Historia_usuario.pdf).

## ¿Qué hace exactamente el filtro del microservicio?

Un ejemplo del funcionamiento del filtro se puede encontrar en la documentación presente
[aquí](https://github.com/nazaretrogue/SMM/tree/master/Evaluacion). Para probar
el propio filtro solo hay que seguir las [instrucciones](https://github.com/nazaretrogue/SMM.git)
de instalación detalladas en el repositorio, abrir una imagen en la aplicación
y aplicar el filtro termal sobre dicha imagen.  

La [implementación del filtro](https://github.com/nazaretrogue/Microservicio-multimedia/tree/master/src) está disponible en este repositorio.
