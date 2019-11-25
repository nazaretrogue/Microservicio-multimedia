FROM rabbitmq:latest

RUN mkdir ./src
RUN mkdir ./templates
WORKDIR .

ENV FLASK_RUN_HOST 0.0.0.0
EXPOSE 5000

RUN apt-get update && apt-get upgrade -y && apt-get install -y python3 python3-pip
RUN update-alternatives --install /usr/bin/python python /usr/bin/python3 1\
    && update-alternatives --install /usr/bin/pip pip /usr/bin/pip3 1

COPY requirements.txt requirements.txt

RUN pip3 install -r requirements.txt

COPY src/ ./src
COPY templates/ ./templates
COPY app.py app.py

RUN python src/receiver.py &

CMD gunicorn --bind ${FLASK_RUN_HOST}:$PORT app:app
