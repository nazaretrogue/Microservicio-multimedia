from flask import Flask, render_template, request, redirect, url_for, jsonify
import os, sys
from src.sender import enviar

app = Flask(__name__, template_folder="templates")

spec = APISpec(title="Tratamiento de imagenes", plugins=[FlaskPlugin(), MarshmallowPlugin()],)

@app.route('/', methods=['POST', 'PUT'])
def img():
    """Define la ruta a la que se enviarán imágenes para ser procesadas.
    Los métodos aceptados son PUT y POST, que requieren de una imagen al enviar
    la petición. Se devuelve un 200 si todo ha ido bien.
    """
    img = request.files['data']
    enviar(img.read())

    return render_template('home.html')

@app.route('/status', methods=['GET'])
def status():
    """Define la ruta de comprobación de funcionamiento del servidor.
    Se accede mediante un GET y se devuelve un 200 si todo ha ido bien.
    """
    body = {'status': 'OK', 'Code': 200, 'Instructions': 'post an image to /, get the image from /img/procesado.png'}
    response = jsonify(body)
    response.status_code = 200

    return response

@app.route('/img/<archivo>', methods=['GET'])
def perfil_usuario(archivo):
    """Define la ruta para devolver una imagen. Se accede con el método GET y
    devuelve la imagen dada por el string introducido.

    Args:
        archivo: nombre del archivo que se desea recuperar.
    """
    if os.path.isfile(archivo):
        dir_path = os.path.dirname(os.path.realpath(archivo))
        dir_path = dir_path + "/" + archivo
        return render_template('mostrar_imagen.html', src=dir_path)

    else:
        return redirect(url_for('error_404'))

@app.errorhandler(404)
def error_404(error):
    return "Error 404, page not found. We can't find the page or file you are looking for", 404

if __name__ == '__main__':
    app.run(debug=True)
