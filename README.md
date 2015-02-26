
[![Build Status](https://travis-ci.org/jameskirk84/complico.svg?branch=master)](https://travis-ci.org/jameskirk84/complico)

# complico

This is the code behind [Complico](http://complico.herokuapp.com) - a site that brings to life the fictional company from the [Peter Serafinowicz show](https://www.google.co.uk/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCMQtwIwAA&url=http%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DZiTl8hThyJg&ei=56LtVNvEGIm9Ubb0g_gL&usg=AFQjCNG3V7I2AMYGO8eNTs1n958BnubT6w&sig2=xtQ7ET5pnGxvrIgcr14GmQ).  Making prices complicated for you - on your favourite sites!

## Overview

This web app is written in Clojure/Clojurescript - Clojure and the ring web framework on the server side and Clojurescript for converting prices in the browser.

## Setting up development environment

The easiest way to setup the environment is to use vagrant.  This will create an isolated linux vm and install the required dependancies for you.  But you don't have to use vagrant - see the manual setup section below.

## Setting up with Vagrant

### Prerequisites

You will need to have the following software installed on your development machine: 
* Virtualbox
* Vagrant 

I suggest you use your native/favourite package manager to install these (for windows see [chocolately](https://chocolatey.org/), Mac OSX: [homebrew](), linux: apt-get :))

### Setup

Open a terminal or command prompt and change directory to the root directory of your working copy of this repository.

Then run the command:

```Batchfile
vagrant up
vagrant ssh
cd /vagrant
```

## Setting up manually

### Prerequisites

You will need to have the following software installed on your machine:
* Java JDK 1.7 
* [leiningen](http://leiningen.org/) 2.0 or higher
* [phantomjs](http://phantomjs.org/) 1.9 or higher


# Running the tests

To run all the server and client tests:

```
lein test
```

# Running the server

```
lein ring server-headless
```

Now visit [localhost:3000](http://localhost:3000) in your browser


# Price conversions

The price conversion functions can be found in src-cljs/complico/conversion_funcs.cljs


