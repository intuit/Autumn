#!/bin/sh

#
# Copyright 2016 Intuit
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

beers="\xF0\x9F\x8D\xBA \xF0\x9F\x8D\xBA \xF0\x9F\x8D\xBA"

brew doctor

sh_formulas=("bash-completion")
make_formulas=("autoconf" "automake" "libtool" "gnu-getopt")
security_formulas=("openssl")
web_formulas=("wget")
aws_formulas=("awscli")
package_formulas=("rpm")
repository_formulas=("git" "git-flow-avh" "hub")
build_formulas=("maven" "gradle")
language_casks=("java" "caskroom/versions/java7")
container_formulas=("docker" "docker-compose" "docker-machine" "docker-swarm" "boot2docker")
container_casks=("virtualbox" "dockertoolbox")
cassandra_formulas=("cassandra")
rdbms_formulas=("mysql")
graph_formulas=("titan-server")
ide_casks=("pycharm-ce" "intellij-idea-ce" "visualvm")
devops_formulas=("packer" "consul" "terraform" "ansible" "saltstack" "nmap")
devops_casks=("vagrant" "otto" "nomad" "serf" "vault" "vagrant-manager" "chefdk")
communications_casks=("hipchat")
miscellaneous_casks=("caffeine")

for formula in "${sh_formulas[@]}" "${make_formulas[@]}" "${security_formulas[@]}" "${web_formulas[@]}" "${aws_formulaas[@]}" "${package_formulas[@]}" "${repository_formulas[@]}" "${build_formulas[@]}" "${language_formulas[@]}" "${container_formulas[@]}" "${cassandra_formulas[@]}" "${rdbms_formulas[@]}" "${graph_formulas[@]}" "${devops_formulas[@]}"; do
  echo "$beers : brewing formula: $formula"
  brew install $formula; brew upgrade $formula
  echo "$beers : brewed formula: $formula"
done

for cask in "${language_casks[@]}" "${container_casks[@]}" "${ide_casks[@]}" "${devops_casks[@]}" "${communications_casks[@]}"; do
  echo "$beers : brewing cask: $cask"
  brew cask install $cask; brew cask update $cask
  echo "$beers : brewing cask: $cask"
done

brew linkapps
brew link --overwrite saltstack

vagrant_plugins=('omnibus' 'ohai' 'berkshelf' 'hosts' 'cachier' 'aws')

for vagrant_plugin in "${vagrant_plugins[@]}"; do
  echo "$beers : vagranting plugin: $vagrant_plugin"
  vagrant plugin install vagrant-$vagrant_plugin
  vagrant plugin update vagrant-$vagrant_plugin
  echo "$beers : vagranted plugin: $vagrant_plugin"
done

gem install fpm
