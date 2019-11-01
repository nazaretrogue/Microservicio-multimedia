import pytest
import requests

@pytest.mark.status
def test_status():
    response = requests.get('http://localhost:5000/status')

    assert response.status_code == 200, "Fallo, respuesta no correcta"

@pytest.mark.get
def test_get(image):
    url = 'http://localhost:5000/img/' + image
    response = requests.get(url)

    assert response.status_code == 200, "Fallo, respuesta no correcta"

@pytest.mark.post
def test_post(image):
    header = {'Content-type': 'multipart/form-data'}
    file = {'data': open(image, 'rb')}
    response = requests.post('http://localhost:5000/', headers=header, files=file)

    assert response.status_code == 200, "Fallo, respuesta no correcta"
