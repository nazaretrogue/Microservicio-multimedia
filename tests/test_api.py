import pytest, requests, base64
from io import BytesIO

@pytest.mark.status
def test_status():
    """Testea si el servidor funciona correctamente enviando una petición get a
    /status. Si todo está correcto, devuelve un 200.

    Args:
        None
    """
    response = requests.get('http://localhost:5000/status')

    assert response.status_code == 200, "Fallo, respuesta no correcta"

@pytest.mark.get
def test_get(procesada):
    """Testea la devolución de una imagen ya procesada. Si todo está correcto,
    devuelve un 200.

    Args:
        procesada: ruta de la imagen.
    """
    url = 'http://localhost:5000/img/' + procesada
    response = requests.get(url)

    assert response.status_code == 200, "Fallo, respuesta no correcta"

#@pytest.mark.post
#def test_post(image):
#    """Testea el envío de una imagen para que sea procesada. Si todo está correcto,
#    devuelve un 200.

#    Args:
#        image: ruta de la imagen.
#    """
#    header = {'Content-type': 'application/json'}
#    response = requests.post('http://localhost:5000/', headers=header, json={"data": "http://images.uncyclomedia.co/inciclopedia/es/1/13/Frymiradaasesina.jpg"})

#    assert response.status_code == 200, "Fallo, respuesta no correcta"
