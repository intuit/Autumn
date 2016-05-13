#!/usr/bin/env bash
if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_58dce6bb7ff5_key -iv $encrypted_58dce6bb7ff5_iv -in bin/codesigning.asc.enc -out bin/codesigning.asc -d
    gpg --fast-import bin/codesigning.asc
fi
