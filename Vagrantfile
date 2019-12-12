Vagrant.configure("2") do |config|
    config.vm.define "tratamientoimg"
    config.vm.box = "ubuntu/bionic64"
    config.vm.provider :virtualbox
    config.vm.network "forwarded_port", guest: 5000, host: 31415, host_ip: "127.0.0.1"
    config.vm.provider "virtualbox" do |v|
        v.name = "tratamientoimg"
        v.memory = 1024
        v.cpus = 1
      end
    config.vm.provision "ansible_local" do |ansible|
        ansible.playbook = "playbook.yml"
    end
end
