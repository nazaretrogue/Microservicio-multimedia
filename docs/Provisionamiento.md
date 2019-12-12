# Archivos para el provisionamiento

## Tabla de contenidos
<!--ts-->
   * [Archivo de configuración: *ansible.cfg*](#Archivo-de-configuracion:-ansible.cfg)
   * [Archivo de inventario: *ansible_hosts*](#Archivo-de-inventario:-ansible_hosts)
   * [Receta: *playbook.yml*](#Receta:-playbook.yml)
   * [Archivo de creación de la máquina: *Vagrantfile*](#Archivo-de-creacion-de-la-maquina:-Vagrantfile)
   * [Bibliografía](#Bibliografia)
<!--te-->

## Archivo de configuración: *ansible.cfg*

El archivo [*ansible.cfg*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/ansible.cfg)
indica la configuración de Ansible respecto a permisos, usuarios, modos de
ejecución...

El archivo es el siguiente:

```ini
[defaults]
# Especificamos el intérprete de python por si acaso intenta coger una versión errónea
ansible_python_interpreter=python3

# Evitamos que pregutne si queremos aceptar una nueva clave cuando una nueva máquina
# se conecta por SSH
host_key_checking = False

# Indicamos la ruta del inventario con las máquinas
inventory = ./ansible_hosts

# Establece el usuario por defecto que ejecutará el playbook si no se especifica
# ninguno
remote_user = root

# Indicamos la shell a utilizar cuando el usuario es root
executable = /bin/sh

# Añadimos la extensión de internalización de Jinja2 para que la aplicación
# pueda traducirse a distintos idiomas según el lenguaje del navegador
jinja2_extensions = jinja2.ext.i18n

# Muestra los mensajes que indican si una tarea específica no se debe ejecutar en
# host. La he puesto para debuggear, aunque no es necesaria
display_skipped_hosts = True
```

A pesar de que está comentado, vamos a explicarlo un poco más detalladamente.

* La primera línea del archivo indica que es la configuración que se va a utilizar
por defecto.
* La segunda línea especifica el intérprete de Python, ya que por defecto estaba
cogiendo la versión global de Ansible, que había sido instalado con Python 2, por
lo que estaba dando problemas de codificación UTF. Así que, por asegurar, se
especifica el interprete a usar.
* La tercera línea sirve para que cada vez que se conecta un usuario por SSH no
pregunte si aceptamos o no la llave, de forma que no quede la máquina esperando
una respuesta que nunca llegaría.
* La cuarta línea indica el inventario con la máquina que se va a levantar. Dicho
archivo se explicará posteriormente.
* La siguiente línea indica qué usuario ejecutará el playbook cuando se ejecuta
dentro de este la orden "become" si no se especifica otro usuario concreto. En
este caso, el usuario que lo ejecutará será root.
* La sexta línea indica qué terminal utilizará el usuario de root. La opción
*/bin/bash* es la opción por defecto, pero por claridad he decidido ponerla.
* La séptima línea es opcional en el caso de que la aplicación sea internacional,
es decir, que hay gente de otros países que la puedan utilizar. Incluir esta opción
supone que todas aquellas partes que estén "templeteadas" pueden traducirse según
el idioma que tenga el navegador.
* La última es utilizada para mostrar por pantalla aquellas tareas que no se pueden
ejecutar en la máquina porque han sido saltadas por algún motivo. Si se pone a
true, mientras se está haciendo el provisionamiento, todas aquellas tareas que
no se hayan ejecutado y se hayan saltado en el provisionamiento, se mostrarán también.
Por defecto no lo hace. Es una opción útil para debuggear sobre todo si hay tareas
que se ejecutan de forma condicional.

## Archivo de inventario: *ansible_hosts*

El inventario, [/*ansible_hosts*/](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/ansible_hosts)
contiene los grupos con las máquinas que se van a levantar.

El archivo contiene lo siguiente:

```ini
[tratamientoimg]
127.0.0.1

[tratamientoimg]
tratamientoimg ansible_ssh_port=2222 ansible_ssh_private_key_file=.vagrant/machines/tratamientoimg/virtualbox/private_key

[tratamientoimg:vars]
ansible_ssh_host=127.0.0.1
ansible_ssh_user=vagrant
```

La primera línea indica el nombre del grupo de máquinas con sus respectivas IPs.
En este caso como la máquina es local, la IP es la de lochalhost.

La siguiente línea indica que las máquinas pertentecientes al grupo *tratamientoimg*
van a conectarse a SSH a través del puerto 2222 y que la clave privada de la máquina
está presente en la ruta indicada.

Las últimas dos líneas definen las variables de entorno para la máquina del grupo
*tratamientoimg*: se define la IP de la máquina, en este caso localhost, y el
usuario que va a acceder a la máquina por defecto si no se indica otro usuario.

## Receta: *playbook.yml*

El archivo [/*playbook.yml*/](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/playbook.yml)
contiene los módulos y dependencias que deben estar instaladas en la máquina que
se va a levantar, es decir, provisiona dicha máquina.

El archivo contiene lo siguiente:

```yaml
---
- hosts: tratamientoimg
  become: true
  tasks:
    - name: Instala git
      apt:
        pkg: git
        state: present
    - name: Instala python
      apt:
        pkg: python3
        state: present
    - name: Instala pip
      apt:
        pkg: python3-pip
        state: present
    - name: Instala NPM
      apt:
        pkg: npm
        state: present
    - name: Instala PM2
      npm:
        name: pm2
        global: yes
    - name: Instala RabbitMQ
      apt:
        pkg: rabbitmq-server
        state: present
    - name: Usuario
      user:
        name: nazaret
        shell: /bin/bash
    - name: SSH
      authorized_key:
        user: nazaret
        state: present
        key: "{{ lookup('file', '/home/nazaret/.ssh/id_rsa.pub') }}"
```

Las a explicaremos parte por parte:

* La primera línea indica el nombre del grupo de máquinas que vamos a provisionar.
* La segunda línea indica que vamos a ejecutar las tareas según el usuario
por defecto definido en el archivo de configuración, explicado anteriormente.
* A partir de la tercera línea, se ejecutarán distintas tareas:
  * La primera tarea instala git, para cuando lo necesitemos para clonar el
  repositorio para llevar a cabo el despliegue con la máquina virtual que hemos
  creado. Indica que apt debe instalar git, puesto que el estado final debe ser
  que dicho paquete esté presente.
  * La segunda tarea se encarga de instalar python3, que al igual que el paquete
  anterior debe estar presente al final del provisionamiento.
  * La tercera tarea instala pip para poder instalar las dependencias que serán
  necesarias para ejecutar el microservicio.
  * La cuarta y quinta tarea instalan npm y pm2 respectivamente; pm2 se instala
  a través de npm y debe instalarse de forma global en el sistema para que cualquier
  usuario pueda utilizarlo para levantar el microservicio.
  * La sexta tarea instala el bróker de mensajería. Al final del provisionamiento,
  como los anteriores paquetes, debe estar presente.
  * La séptima tarea crea un usuario y le asignta un tipo de shell. El usuario
  creado es *"nazaret"*, que se le da la misma shell que a root en el archivo de
  configuración explicado previamente.
  * Por último, la tarea número 8 se encarga de copiar la llave pública de SSH
  para el usuario indicado; dicha clave está en la ruta indicada por la macro *key*.
  Al finalizar el provisionamiento, la clave de dicho usuario debe estar presente.

## Archivo de creación de la máquina: *Vagrantfile*

El archivo [*Vagrantfile*](https://github.com/nazaretrogue/Microservicio-multimedia/blob/master/Vagrantfile)
contiene las órdenes necesarias para crear la máquina virtual; dichas órdenes
pueden verse a continuación.

```rb
Vagrant.configure("2") do |config|
    config.vm.define "tratamientoimg"
    config.vm.box = "ubuntu/bionic64"
    config.vm.provider :virtualbox
    config.vm.network "forwarded_port", guest: 5000, host: 31415, host_ip: "127.0.0.1"
    config.vm.provider "virtualbox" do |v|
        v.name = "tratamientoimg"
        v.memory = 1024
        v.cpus = 1
      end
    config.vm.provision "ansible_local" do |ansible|
        ansible.playbook = "playbook.yml"
    end
end
```

Vamos a explicarlas una por una:

* La primera línea simplemente crea un alias para que sea más cómodo trabajar
y establecer la configuración. A partir de esta, surgen las siguientes líneas:
  * La primera línea tras el alias define la máquina virtual que se va a crear.
  * La segunda define el sistema operativo que va a contener. He utilizado ubuntu
  18 porque es una versión estable y la que más conozco puesto que es la que más
  he utilizado. Además tiene años de soporte aún y no tiene una gran cantidad de
  bugs.
  * La tercera línea especifíca el proveedor que va a utilizar Vagrant. Por defecto
  es VirtualBox, pero por algún motivo estaba intentando utilizar libvirt como
  proveedor y me estaba dando problemas, por eso preferí incluir esta línea para
  asegurarme de que utlizaba el proveedor correcto.
  * La siguente línea define la IP de la máquina, el puerto del anfitrión y el puerto
  de la propia máquina por la que se conecta con el anfitrión. Es decir, esta línea
  define la red interna de la máquina, concretamente es una red NAT.
  * La siguiente línea crea un alias para poder establecer otras caracterísitcas
  de la máquina a través del proveedor. Indica:
    * Primero, el nombre de la máquina, en este caso *tratamientoimg*.
    * Después indica el tamaño de memoria RAM que utilizará; he puesto 1GB pero
    es posible que solo con 512MB funcionara correctamente. No obstante, he preferido
    utilizar más para asegurarme de que todo funciona bien.
    * Por último, definimos el número de CPUs que va a utilizar la máquina, en
    este caso solamente una, ya que no va a tener una carga especialmente pesada.
  * Por último, tras crear un último alias para Ansible:
    * Se indica dónde se encuentra el fichero *playbook.yml* que contiene el provisionamiento
    en sí. En este caso está en el mismo directorio que el *Vagrantfile*.


## Bibliografia

* [Providers en Vagrant](https://www.vagrantup.com/docs/virtualbox/common-issues.html)
* [Vagrant en local](https://tech.osteel.me/posts/how-to-use-vagrant-for-local-web-development)
* [Usando Ansible para provisionar en Vagrant](https://www.vagrantup.com/docs/provisioning/ansible.html)
* [Creando cuentas de usuario con Ansible](https://docs.ansible.com/ansible/latest/modules/user_module.html)
* [Vagrant y VirtualBox](https://wiki.libvirt.org/page/UbuntuKVMWalkthrough)
* [Máquinas de Vagrant: VagrantCloud](https://app.vagrantup.com/ubuntu/boxes/bionic64)
