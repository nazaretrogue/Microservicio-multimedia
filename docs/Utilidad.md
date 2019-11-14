# Conoce el microservicio de tratamiento de imágenes

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
RGB y aplicar en ella un filtro comúnmente conocido como blanco y negro; el filtro
dejará la imagen en escala de grises como si fuera una foto antigua.

## ¿Qué se usará?

La idea principal es implementar el filtro en Python, utilizando bibliotecas propias
del lenguaje, concretamente del módulo *Image*. Puesto que es necesario utilizar
un sistema de log para controlar todo lo que ocurra en el microservicio, utilizaré
*logstash* o *fluentd*, ya que ambos son open source y se adaptan a lo que necesito,
pero aún no he decidido cuál de los dos utilizaré.

En cuanto a middleware de mensajería utilizaré *RabbitMQ*, que tiene bibliotecas
para Python.

Además, para llevar a cabo los tests se utilizará un módulo del lenguaje, *pytest*,
que ofrece los recursos necesarios para hacer tests tanto unitarios como de integración
continua.

Las tecnologías utilizadas se explican de forma más detallada en la
[documentación](https://nazaretrogue.github.io/Microservicio-multimedia/Tecnologias_usadas).

No será necesaria una base de datos donde almacenar las imágenes puesto que se enviarán
directamente en los mensajes HTTP.

## ¿Por qué crear este microservicio?
En una asignatura de otro año nos enseñaron a utilizar en profundidad los paquetes
que se encargan de multimedia en Java, siendo sencillo de escribir en código pero
consiguiendo unos resultados muy visuales. Me gustó tanto que decidí que quería
hacer un proyecto relacionado con multimedia. Tras muchos problemas intentando
implementarlo en Java, decidí cambiar a Python que es mucho más sencillo para
desplegarlo en la nube. Dichos problemas con Java se explican [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/Problemas_java).

Se ha creado un caso hipotético donde un cliente desea que se implemente el filtro
comentado para su empresa. Para ello, se hace una entrevista que se puede leer
[aquí](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/docs/Historia_usuario.pdf).

## ¿Qué hace exactamente el filtro del microservicio?

Un ejemplo del funcionamiento del filtro se puede encontrar en la documentación presente
[en la documentación](https://nazaretrogue.github.io/Microservicio-multimedia/Funcionamiento_filtro).

## Cómo instalar y testear la aplicación en local

Para poder testear la aplicación es necesario tener instalado python3 y el instalador
de módulos de éste, *pip* para poder instalar el módulo *pytest*. Todo esto, y la
funcionalidad del test está explicado con más detalle en la
[documentación](https://nazaretrogue.github.io/Microservicio-multimedia/Tecnologias_usadas).

Una vez que esté instalados, clonamos el repositorio con:

```bash
$ git clone https://github.com/nazaretrogue/Microservicio-multimedia.git
```

Una vez clonado, entramos en el directorio *Microservicio* y abrimos una terminal.
Una vez hecho esto, ejecutamos

```bash
$ pytest
```

Haciendo esto, *pytest* buscará en el directorio *tests* todos aquellos ficheros
que empiecen por *test_*, y a su vez, funciones que empiencen por *test_* dentro
de éstos.
