# Implementación en java: problemas

Aunque el microservicio se está implementando ahora en Python, me gustaría explicar
el intento de hacerlo en Java y los (múltiples) problemas que he tenido durante
él, los cuales al final han acabado sin solución, motivo por el cual se ha cambiado
de lenguaje.

## Idea original

Al principio la idea era crear un filtro al que le llegaba una imagen en el espacio de
color RGB y la transformaba de manera que aquellas partes de la imagen cuya potencia
de color rojo fuera igual o superior a 128 (es decir, las partes más rojizas, naranjas
y amarillentas) quedaran igual, y el resto de la imagen se transformara a escala
de grises.

Para ello se iban a utilizar bibliotecas propias empaquetadas en sendos *.jar*,
ya que este filtro como tal no existía en el paquete *awt* de Java.

## Problema tras problema: un pequeño resumen

Para construir el proyecto, era necesario utilizar una herramienta de construcción
que ensamblara todas las partes y testeara el código. Para ello, busqué entre las
distintas herramientas que ofrecía Java: *ant*, *maven*, *gradle*... entre otras.
Puesto que la documentación de *maven* era mucho más extensa y además la sintaxis
de los archivos *pom.xml* (archivo de construcción de *maven*) era mucho más sencilla
y fácil de entender, decidí utilizar esta herramienta.

Se creó el archivo *pom.xml* y se construyó el proyecto. Funcionaba correctamente.
Se pasaban los tests en [*TravisCI*](https://travis-ci.org/). Todo iba bien. Así que
ahora había que incluir la propia funcionalidad del microservicio, el filtro que se
iba a implementar y que se ha explicado antes. Y aquí vino el primer problema.

Por lo general, las herramientas de construcción no tienen ningún problema en
incluir y enlazar bibliotecas que no están en el core del lenguaje. El problema
radicaba en que no es solo que las bibliotecas que yo debía usar no formaban
parte del core del lenguaje, sino que yo solamente tenía los binarios de dichas
bibliotecas, y enlazarlas en *maven* era una tarea que se me iba de las manos.
Era prácticamente imposible enlazar archivos propios. Así que tras mucho buscar
en *google* (bendito sea por ayudarme tanto), desistí. Usar *maven* había sido
un error: había que cambiar de herramienta.

Así es como empecé a utilizar *ant*. El enlazado de archivos propios era mucho más
sencillo; además, tenía una pequeña ventaja: estaba utilizando el IDE **netbeans**,
el cual disponía de un plugin que generaba los archivos *build.xml* (el archivo
de construcción de *ant*) y enlazaba directamente las bibliotecas con solo un click
de ratón. Ilusa de mí si pensé que eso me ayudaría. Los archivos que genera **netbeans**
funcionan; hasta cierto punto, todo sea dicho.

No sé por qué motivo, el IDE tiene la mala costumbre de incluir cientos de líneas
de código en los archivos *build.xml* que sirven solo y exclusivamente para la
ejecución de la aplicación, la que sea, dentro del propio IDE. Pista: en el momento en
el que sale fuera, da problemas, MUCHOS problemas, de los que hablaré un poco más
adelante.

Por ahora, funcionaba. Se construía el proyecto entero, se ejecutaban los tests y
todo estaba correctamente enlazado. Se ejecutaba correctamente en los dos sistemas
de integración continua que estoy utilizando. Así que se hizo la entrega y todo
pareció calmarse.

Ahora vienen nuevos (y mejores) problemas con esteroides. La cosa no había hecho
más que empezar.

En el desarrollo del microservicio, y debido a su naturaleza de tratamiento de
multimedia, es necesario utilizar un bróker de mensajería que acumulase en una
cola las imágenes que llegaban en las peticiones HTTP. Comencé utilizando *RabbitMQ*;
el desarrollo parecía sencillo y tampoco parecía haber problema en ponerlo en
funcionamiento.

Error: sí que habría problemas.

Implementé el sender y el receiver y todo parecía correcto. Así que decidí que era
el momento de descargar el cliente de *RabbitMQ* para Java y probar si funcionaba.
Y llegó un pequeño percance que no esperaba: el cliente necesitaba de la versión
de pago del JDK, es decir, había que utilizar *oracleJDK*, y puesto que yo usaba
*openJDK*, la cosa pintaba mal.

Tras buscar mil formas de descargar e implantar *oracleJDK* y no encontrar ninguna
solución, decidí cambiar de bróker de mensajería. Alguno que no requiriese usar
software privativo. Así que opté por *activeMQ*, que tampoco era difícil de utilizar.

Implementé en este nuevo bróker el sender y el receiver; en este caso, el cliente
era sencillo pues formaba parte de una biblioteca de Java, así que solo había que
incluir el archivo en el proyecto y todo iría bien. Así que lo hice, y funcionó.
Así que pensé: "bache superado".

Y así fue; el bróker de mensajería iba bien, había superado otro obstáculo. Ahora
había que desplegar el servicio en Internet y probar el funcionamiento.

El IDE de **netbeans** tiene por defecto el servidor de *GlassFish 4.0.1*, así
que decidí probar con ese. Había tutoriales en Internet de como hacer una APIRest
en Java y desplegar el servicio usando este servidor.

Me topé con otro problema más, el cuál ya había vivido antes: de nuevo, para que
*GlassFish* se pudiera ejecutar necesitaba el *oracleJDK*, así que la versión libre
no funcionaba. Habría que hacer otro cambio a un servidor nuevo. Probé con *TomCat*;
pero no me dejaba instalarlo. Lo intenté con una versión de *JBoss* que traía el
IDE; tampoco podía instalarlo. Así que al final, opté por *WildFly*, una versión de
*JBoss* que sí funcionaba. Estaba escrito en la versión de JavaEE. Lo instalé, y
en este caso, sí me dejó.

El servidor estaba ahora en funcionamiento. Pero cuando intentaba lanzar el microservicio,
boom. Explotaba por algún motivo, había errores por todos sitios, buscaba en *google*
y no encontraba solución, "¿es que a nadie le ha fallado lo mismo que a mí? ¿Por
qué no hay nada ni en stackoverflow ni en ningún lado?". A día de hoy sigo pensando
que tengo el poder de generar fallos que no existen en realidad.

Se me recomendó usar otro IDE distinto. Lo descargué e intenté abrir el proyecto.
Pero, o bien no lo reconocía, o bien daba fallos por dependencias, conté unos 37 fallos
que no podía ponerme a arreglar uno por uno. Esos fallos provenían del traspaso de
un IDE a otro: al haber utiliza **netbeans**, muchas de las dependencias se arreglaban
instalando plugins en el IDE y fin del problema. Cuando cambias de IDE... todos
esos fallos ahora están sin resolver. Y había muchas que tenía que resolver manualmente
tocando el archivo *build.xml*, el cual contaba con un total de 2200 líneas.

Así que cambiar de IDE ahora no era una opción. Seguí en **netbeans**.

Desde el IDE no había manera de levantar el servicio. Así que quise probar a ver
qué pasaba en *travisCI*. Subí los cambios a mi repositorio y esperé a que se
pasaran los tests (los unitarios, aún no había programado tests para la API).
Por supuesto, **_build failed_**. Además, el fallo era, cuanto menos, gracioso:
¿os acordáis de los cientos de líneas que *netbeans* añadía al *build.xml*? Pues
el fallo estaba en una de ellas (en realidad en más de la mitad pero el fallo lo dio la
primera línea).

Llegados a este punto, decidí que había tenido suficiente con Java. Intentaba arreglar
un fallo y salían 4 más, además de que el que intentaba arreglar seguía ahí. Así
que después de estar al límite de mis fuerzas con el proyecto, cambié de lenguaje;
uno sencillo, que fuera adecuado para hacer cosas en la nube y que tuviera documentación
más que suficiente: y así surgió hacerlo en Python.

## Bibliografía utilizada

Para intentar arreglar los problemas, dependí mucho de *google*, y he querido
incluir algunos de los enlaces que más me han ayudado.

* [Herramienta de construcción: Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)  
* [Dependecias externas en maven](https://stackoverflow.com/questions/28703491/how-can-i-add-a-3rd-party-jar-to-my-travis-ci-maven-build)  
* [Travis usa ant en lugar de maven](https://stackoverflow.com/questions/53180531/travis-uses-ant-instead-of-maven)  
* [Dependecias externas en ant](https://www.mkyong.com/ant/ant-how-to-create-a-jar-file-with-external-libraries/)  
* [Integración continua en Travis CI](https://docs.travis-ci.com/user/languages/java/)  
* [Tests en Java. Uso de jUnit](https://dev.to/chrisvasqm/introduction-to-unit-testing-with-java-2544)  
* [jUnit con ant](http://ant.apache.org/manual/Tasks/junit.html)
* [JavaEE y Servlets](https://docs.oracle.com/javaee/7/tutorial/servlets004.htm#BNAFU)
* [Creando una APIRest en Java](https://www.oscarblancarteblog.com/2018/06/25/creando-un-api-rest-en-java-parte-1/)
* [MimeTypes en Java](https://www.baeldung.com/java-file-mime-type)
* [Imagen en el método POST](https://stackoverflow.com/questions/11444528/retrieve-an-image-from-a-post-request)
* [APIRest y NetBeans](https://www.adictosaltrabajo.com/2015/09/23/creando-servicios-restful-con-netbeans-8/)
* [Aplicaciones web y NetBeans](https://www.studytonight.com/servlet/creating-servlet-in-netbeans.php)
* [Paso de mensajes con ActiveMQ](https://github.com/ballerina-guides/messaging-with-activemq)
* [Spring Boot y ActiveMQ en TravisCI](https://stackoverflow.com/questions/30284900/use-activemq-in-travis-ci)
* [Imágenes con Java Servlets](http://zetcode.com/articles/javaservletimage/)
* [WildFly en NetBeans](http://plugins.netbeans.org/plugin/53333/wildfly-application-server)
