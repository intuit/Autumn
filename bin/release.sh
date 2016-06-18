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

fromPom() {
  case $# in
    2) mvn -f $1/pom.xml help:evaluate -Dexpression=$2 | sed -n -e '/^\[.*\]/ !{ p; }';;
    3) mvn -f $1/pom.xml help:evaluate -Dexpression=$2 | sed -n -e '/^\[.*\]/ !{ p; }' | \
         python -c "import xml.etree.ElementTree as ET; import sys; field = ET.parse(sys.stdin).getroot().find(\"$3\"); print (field.text if field != None else '')"
  esac
}

application_version=$(fromPom . project.properties application.version)
build_version=`date "+%s"`
version=${application_version}.${build_version}

echo "milestone version: $version"

cp ./build/git/hooks/* .git/hooks

git flow release start -F $version
git flow release finish -m "milestone: $version" -p -D $version
