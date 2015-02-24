#!/usr/bin/env bash

VAGRANT_HOME=/home/vagrant

function main(){
  apt-get update
  install_java
  install_leiningen
  install_phantomjs
  echo "Provisioning finished!"
}

function install_java(){
  apt-get install -y openjdk-7-jdk
}

function install_leiningen(){
  if [ ! -f $VAGRANT_HOME/bin/lein ]; then
    mkdir -p $VAGRANT_HOME/bin
    curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein 
    cp lein $VAGRANT_HOME/bin 
    PATH="/home/vagrant/bin:$PATH"
  fi
  chmod a+x $VAGRANT_HOME/bin/lein
  lein upgrade
}

function install_phantomjs(){
  apt-get install -y phantomjs
}

#call main
main
