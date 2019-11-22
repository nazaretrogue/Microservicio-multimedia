FROM python:3.7-alpine

RUN mkdir ./src
RUN mkdir ./templates
WORKDIR .

ENV FLASK_RUN_HOST 0.0.0.0
EXPOSE 5000

RUN apk --no-cache add jpeg-dev zlib-dev
RUN apk add --no-cache --virtual .build-deps build-base linux-headers \
    && pip install Pillow

COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt

COPY src/ ./src
COPY templates/ ./templates
COPY app.py app.py

RUN python src/receiver.py &

CMD gunicorn --bind ${FLASK_RUN_HOST}:5000 app:app
