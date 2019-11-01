import pika
from filter import filtro

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

channel.queue_declare(queue='imagenes')

def callback(ch, method, properties, body):
    print("Recibida imagen de la cola de procesamiento")
    filtro(body)

channel.basic_consume(queue='imagenes', on_message_callback=callback, auto_ack=True)

print("Consumidor en espera. Ctrl+C para acabar")

channel.start_consuming()
