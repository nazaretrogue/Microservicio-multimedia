import pika
from filter import filtro

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.queue_declare(queue='imagenes')

def callback(ch, method, properties, body):
    """Recibe una imagen de la cola del bróker para enviarla a su procesamiento.
    Previamente se ha abierto una conexión con el bróker y creado una cola (si
    no existe ya) para extraer las imágenes de ahí. Se ejecuta cada vez que hay
    una imagen encolada.

    Args:
        ch: canal de recepción.
        method: método de recepción de elementos de la cola.
        properties: propiedades de la recepción de la cola de la que recibe.
        body: contiene la imagen recibida de la cola.
    """
    print("Recibida imagen de la cola de procesamiento")
    filtro(body)

channel.basic_consume(queue='imagenes', on_message_callback=callback, auto_ack=True)

print("Consumidor en espera. Ctrl+C para acabar")

channel.start_consuming()
