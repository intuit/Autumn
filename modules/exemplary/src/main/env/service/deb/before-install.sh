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

case "$1" in
  install|upgrade)
    getent group ${application.group} >/dev/null || groupadd -r ${application.group}
    getent passwd ${application.user} >/dev/null || useradd -r -g ${application.group} -d /opt/${application.name} \
      -s /usr/sbin/nologin -c "Level Up Analytics / ${application.name}" ${application.user}
    ;;
  abort-upgrade)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
