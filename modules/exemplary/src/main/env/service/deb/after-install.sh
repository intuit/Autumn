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
  configure|abort-remove|abort-deconfigure)
    for d in "${application.home}" "${log.dir}"; do \
      mkdir -p $d
      chown -R ${application.user}:${application.group} $d
    done
    for f in "${application.home}/bin/run" "${application.home}/service/run" "${application.home}/service/log/run"; do \
      chmod 755 $f
    done
    ln -s ${application.home}/service /etc/service/${application.name}
    ln -s /usr/bin/sv /etc/init.d/${application.name}
    ;;
  abort-upgrade)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
