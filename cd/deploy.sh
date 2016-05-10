#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" == 'develop' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn deploy -e -X -P sign --settings cd/settings.xml
fi
