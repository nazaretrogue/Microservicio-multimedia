all: process_mng install create_environment install_tester

process_mng:
	npm install pm2@latest -g

install:
	pip install virtualenv

create_environment:
	( \
		chmod +x script.sh; \
		./script.sh; \
	)

install_tester:
	pip install pytest-virtualenv

start:
	pm2 start src/receiver.py
	pm2 start app.py

stop:
	pm2 stop src/receiver.py
	pm2 stop app.py

restart:
	pm2 restart src/receiver.py
	pm2 restart app.py

reload:
	pm2 reload src/receiver.py
	pm2 reload app.py

delete:
	pm2 delete src/receiver.py
	pm2 delete app.py

test:
	pytest

heroku:
	sudo snap install heroku --classic
	heroku login
	heroku apps:create tratamientoimg --buildpack heroku/python
	git push heroku master
	
