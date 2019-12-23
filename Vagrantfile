Vagrant.configure("2") do |config|
    # config.vm.define = "tratamientoimg-az"
    config.vm.box = 'azure-dummy'
    config.vm.box_url = 'https://github.com/Azure/vagrant-azure/raw/v2.0/dummy.box'
    config.ssh.private_key_path = "/home/nazaret/.ssh/key_rsa"
    config.vm.provider "azure" do |az, override|
      az.tenant_id = '779beaee-c480-4127-a04a-449a18bf44bb'#ENV['AZURE_TENANT_ID']
      az.client_id = '055c5443-f0c6-4cd3-ba0a-223f75f21d25'#ENV['AZURE_CLIENT_ID']
      az.client_secret = 'e2d8d8fe-6709-4355-b8cb-566364b7a92c'#ENV['AZURE_CLIENT_SECRET']
      az.subscription_id = 'd6726166-25d2-43b0-a36c-7505190c0b6c'#ENV['AZURE_SUBSCRIPTION_ID']
      az.vm_name = "tratamientoimg-az"
      az.vm_size = "Standard_B1s"
      az.vm_image_urn = "Canonical:UbuntuServer:18.04-LTS:latest" # sacado con az vm image list
      az.resource_group_name = "nazaretrg_rg_Linux_westeurope"
      az.location = "westeurope"
    end
    config.vm.provision "ansible_local" do |ansible|
        ansible.playbook = "despliegue/playbook.yml"
    end
end
