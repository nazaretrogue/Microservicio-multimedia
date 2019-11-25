# Microservicio de tratamiento de imágenes
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/nazaretrogue/Microservicio-multimedia.svg?branch=master)](https://travis-ci.org/nazaretrogue/Microservicio-multimedia)
[![Run Status](https://api.shippable.com/projects/5d9d9d8227d7a00007532757/badge?branch=master)]()

## Tabla de contenidos
<!--ts-->
   * [Introducción al microservicio de tratamiento de imágenes](#Introduccion-al-microservicio-de-tratamiento-de-imagenes)
   * [*Buildtool*](#Buildtool)
   * [Despliegue](#Despliegue)
   * [Despliegue con contenedores](#Despliegue-con-contenedores)
   * [DockerHub](#DockerHub)
   * [Documentación de la API y los tests](#Documentacion-de-la-API-y-los-tests)
<!--te-->

## Introducción al microservicio de tratamiento de imágenes

Se puede leer toda la información sobre qué es, su utilidad, qué se ha utilizado
para construirlo y su testeo en la [documentación](https://nazaretrogue.github.io/Microservicio-multimedia/Utilidad).

## *Buildtool*

```bash
buildtool: Makefile
```

La documentación de la buildtool está en este [enlace](https://nazaretrogue.github.io/Microservicio-multimedia/Tecnologias_usadas).

## Despliegue

Despliegue: https://tratamientoimg.herokuapp.com/

La documentación sobre el despliegue está [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/PaaS).

## Despliegue con contenedores

Contenedor: https://tratamientoimg.herokuapp.com/  
Contenedor:

La documentación sobre los ficheros de configuración utilizados está [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/docker).  
La documentación sobre el procedimiento de despliegue, [aquí](https://nazaretrogue.github.io/Microservicio-multimedia/heroku_docker).  
Se ha actualizado la herramienta de construcción y su [documentación](https://nazaretrogue.github.io/Microservicio-multimedia/Tecnologias_usadas)
con la automatización del despliegue de contenedores.

## DockerHub

DockerHub: https://hub.docker.com/repository/docker/nazaretrogue/tratamientoimg

Para descargarlo en local:

```bash
docker pull nazaretrogue/tratamientoimg:latest
```

## Documentación de la API y los tests

Toda la documentación se ha generado en forma de HTML a través de *sphinx*, un
generador semi-automático de documentación para Python.Para descargarla en forma
de HTML, se accede a los enlaces y se descarga desde ahí para poder visualizarlos.
Si se vieran directamente en github se verían como un html en crudo.

* [Documentación de la API](https://nazaretrogue.github.io/Microservicio-multimedia/build/html/app.html)
* [Documentación de los tests](https://nazaretrogue.github.io/Microservicio-multimedia/build/html/tests.html)
* [Documentación del filtro, el sender y el receiver](https://nazaretrogue.github.io/Microservicio-multimedia/build/html/src.html)
* [Toda la documentación extra](https://nazaretrogue.github.io/Microservicio-multimedia/)
