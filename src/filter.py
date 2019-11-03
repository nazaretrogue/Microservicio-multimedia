from PIL import Image
import io
import os

def filtro(img_source):
    """Aplica un filtro donde transforma la imagen dada en una imagen en escala
    de grises. La imagen creada se guarda con el nombre de procesado.png.

    Args:
        img_source: imagen fuente sobre la que se aplicará el filtro.
    """
    tmp = Image.open(io.BytesIO(bytearray(img_source)))
    img_dest = tmp.convert('LA')
    img_dest.save("procesado.png")
