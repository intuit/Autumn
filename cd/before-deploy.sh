#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" == 'develop' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    openssl aes-256-cbc -K $encrypted_58dce6bb7ff5_key -iv $encrypted_58dce6bb7ff5_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
    gpg --fast-import cd/codesigning.asc
fi
