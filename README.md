# Autumn

master [![Build Status](https://travis-ci.org/jwtodd/Autumn.svg?branch=master)](https://travis-ci.org/jwtodd/Autumn)
develop [![Build Status](https://travis-ci.org/jwtodd/Autumn.svg?branch=develop)](https://travis-ci.org/jwtodd/Autumn)
[![Coverage Status](https://coveralls.io/repos/github/intuit/Autumn/badge.svg?branch=develop)](https://coveralls.io/github/intuit/Autumn?branch=develop)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Project <a href="https://github.com/intuit/Autumn/blob/develop/misc/autumn_sm.png" target="_blank"><img src="https://github.com/intuit/Autumn/blob/develop/misc/autumn_sm.png" alt="Autumn" style="max-width:15%;"></a>

####Why

  Contemporary API services built to scale are largely comprised of a sizable amount of common concerns, examples being secure service endpoints, request integrity validation, application logic delegation, durable persistence, operational concerns such as auditing and logging, etc.

####What

  Autumn is a collection of ready-to-use-as-is base implementations that solve the above thereby freeing up application concerns to focus on it’s principle value proposition. Autumn is used by a number of Intuit/IDEA initiatives, namely: **redacted**

  Autumn can be viewed as the artifact of disciplined engineering rigor. Stabilizing the provided resources with concrete and readily consumable components yet readily enabling implementation to vary over time without requiring complete/large-scale application re-writes/overhauls thereby allowing applications organization wide to keep pace with current state offerings, innovate more rapidly via selective leverage, etc.

####How

  Autumn is a contemporary collection of readily consumable state-of-the-art Java libraries that satisfies micro-service principle concerns, namely: protocols, observability, manageability, separation of concerns, injectability (IoC), component lifecycle management, and discreet testability.

## Getting involved

* Source: [https://github.com/intuit/autumn](https://github.com/intuit/autumn)

The principles of Autumn can be summarized as both a strong leverage of
[dependency injection](https://en.wikipedia.org/wiki/Dependency_injection) and consistently specified component
lifecycle management, collectively allowing for significant higher-order application leverage while at the same
time allowing for rapid incorporate of new internal service implementations.

Documentation improvements are always welcome, so please send patches our way.

## Contributing

The `master` branch of this repository contains the latest stable release of Autumn, and snapshots are published to
the `develop` branch. In general pull requests should be submitted against `develop` in the form of `feature` branch
pull requests. See the [Contributing to a Project](https://guides.github.com/activities/contributing-to-open-source/)
article for more details about how to contribute.

## Development

### Environment

*   % /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
*   % for f in "git git-flow-avh maven"; do brew install $f; fi
*   % for c in "java"; do brew cask install $c; fi

### Build

*   % git clone https://github.com/intuit/autumn
*   % cd autumn
*   % git flow init
*   % mvn clean package

### Run

*   % java -cp ./modules/exemplary/target/autumn.exemplary-*-SNAPSHOT-development-all.jar com.intuit.data.autumn.exemplary.server.Main

### Test

Java

*   % java -cp modules/exemplary/target/autumn.exemplary-20151113104424-SNAPSHOT-development-all.jar com.intuit.data.autumn.exemplary.client.PingClient

curl

*   % curl http://localhost:8080/foo/proto/ping/d5bba1d7-3631-4f0a-a2c9-5ea53fb3d157
*   % curl http://localhost:8080/foo/proto/pings
*   % curl -X POST -H "Content-Type: application/json" -d '{"id":"id-1","message":"message-1"}' http://localhost:8080/foo/proto/ping

## Future Considerations

*  --tbd--

## License

The code is published under the terms of the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
