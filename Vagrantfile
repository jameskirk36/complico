# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.provider "virtualbox" do |v|
    v.name = "complico-dev-env"
    v.memory = 1024
    v.cpus = 1
  end
  #networking 
  config.vm.network "forwarded_port", guest: 3000, host: 3000
  config.vm.hostname = "complico-dev-env"

  #provisioning
  config.vm.provision :shell, path: "bootstrap.sh"
end
