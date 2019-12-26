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
	heroku apps:create tratamientoimg
	heroku addons:create cloudamqp:lemur
	heroku config:set HEROKU=1
	git push heroku master

deploy: create_environment
	gunicorn app:app

container-build:
	heroku stack:set container
	git push heroku master
	docker build -t tratamientoimg

container-run: container-build
	docker run -p 5000:5000 tratamientoimg

vm-vagrant:
	vagrant up

vm-vagrant-unprovisioned:
	vagrant up -no--provision

vm-provision: vm-vagrant-unprovisioned
	ansible-playbook playbook.yml

vm-azure-deploy:
	vagrant plugin install vagrant-azure
	vagrant box add azure-dummy https://github.com/Azure/vagrant-azure/raw/v2.0/dummy.box --provider azure
	az login
	az ad sp create-for-rbac
	export AZURE_TENANT_ID=$(az account list --query '[?isDefault].tenantId' -o tsv)
	export AZURE_SUBSCRIPTION_ID=$(az account list --query '[?isDefault].id' -o tsv)
	vagrant up --provider=azure

vm-azure-app: install create_environment
	pm2 start Microservicio-multimedia/src/receiver.py
	pm2 start Microservicio-multimedia/app.py
