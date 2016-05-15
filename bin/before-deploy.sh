#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" == 'develop' ] || [ "$TRAVIS_BRANCH" == 'master' ]; then
  if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_351032695731_key -iv $encrypted_351032695731_iv -in bin/codesigning.asc.enc -out bin/codesigning.asc -d
    gpg --fast-import bin/codesigning.asc
  fi
fi
