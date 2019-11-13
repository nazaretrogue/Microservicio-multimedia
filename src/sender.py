import pika, os
import logging

def enviar(img_src):
    """Recibe una imagen de una petición HTTP que envía a la cola del bróker de
    mensajería a la espera de ser procesada. Para ello, abre una conexión y
    crea una cola (si no está ya creada) donde almacena la imagen.

    Args:
        img_src: imagen recibida en la petición HTTP.
    """
    if not 'HEROKU' in os.environ:
        logging.basicConfig()
        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))

    else:
        url = os.environ.get('CLOUDAMQP_URL', 'amqp://guest:guest@localhost:5672/%2f')
        params = pika.URLParameters(url)
        connection = pika.BlockingConnection(params)

    channel = connection.channel()

    channel.queue_declare(queue='imagenes')
    channel.basic_publish(exchange='', routing_key='imagenes', body=img_src)

    print("Enviada imagen a la cola de procesamiento")

    connection.close()
