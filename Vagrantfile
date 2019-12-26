Vagrant.configure("2") do |config|
    config.vm.box = "azure-dummy"
    config.vm.box_url = 'https://github.com/Azure/vagrant-azure/raw/v2.0/dummy.box'
    config.ssh.private_key_path = "/home/nazaret/.ssh/id_rsa"
    config.vm.provider "azure" do |az, override|
      az.tenant_id = ENV['AZURE_TENANT_ID']
      az.client_id = ENV['AZURE_CLIENT_ID']
      az.client_secret = ENV['AZURE_CLIENT_SECRET']
      az.subscription_id = ENV['AZURE_SUBSCRIPTION_ID']
      az.vm_name = "tratamientoimg-az"
      az.vm_size = "Standard_B1s"
      az.vm_image_urn = "Canonical:UbuntuServer:18.04-LTS:latest" # sacado con az vm image list
      az.resource_group_name = "nazaretrg_rg_Linux_westeurope"
      az.location = "westeurope"
      az.tcp_endpoints = '5000'
    end
    config.vm.provision "ansible_local" do |ansible|
        ansible.playbook = "despliegue/playbook.yml"
    end
end
