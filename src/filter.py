from PIL import Image
import os

def filtro(nombre_img_source):
    #nombre_orig = img_source.filename
    nombre = str(os.path.splitext(nombre_img_source)[0]) + "_procesado.png"
    img = Image.open(nombre_img_source).convert('LA')
    img.save(nombre)
