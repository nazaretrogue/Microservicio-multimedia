HOME=.

all: install create_environment install_tester

install:
	pip install virtualenv

create_environment:
	( \
		chmod +x script.sh; \
		./script.sh; \
	)

install_tester:
	pip install pytest-virtualenv

exec_mess_broker:
	python src/receiver.py

start:

stop:

test:
