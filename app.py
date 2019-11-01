from flask import Flask, render_template, request, redirect, url_for, jsonify
import os, sys
from src.sender import enviar

app = Flask(__name__, template_folder="templates")

@app.route('/', methods=['POST', 'PUT'])
def img():
    #img = request.files.get('data').filename
    img = request.files.get('data').stream
    enviar(img)

    return render_template('home.html')

@app.route('/status')
def status():
    body = {'status': 'OK', 'Code': 200, 'Instructions': 'post an image to /, get the image from /img/<file_name>_procesado.png'}
    response = jsonify(body)
    response.status_code = 200

    return response

@app.route('/img/<archivo>', methods=['GET'])
def perfil_usuario(archivo):
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
