import pika

def enviar(nombre_img):
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()

    channel.queue_declare(queue='imagenes')
    channel.basic_publish(exchange='', routing_key='imagenes', body=nombre_img)

    print("Enviada imagen a la cola de procesamiento")

    connection.close()
