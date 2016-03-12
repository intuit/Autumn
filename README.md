# Autumn

build status : master [![Build Status : master](https://travis-ci.org/jwtodd/Autumn.svg?branch=master)](https://travis-ci.org/jwtodd/Autumn)
build status : develop [![Build Status : develop](https://travis-ci.org/jwtodd/Autumn.svg?branch=develop)](https://travis-ci.org/jwtodd/Autumn)

## Project

This project is a collection of common/popular libraries brought together via low cohesion means allowing for
rapid creation of modern Java applications brought about by judiciously adhering to near-zero component cohesion.
As such, expected commodity elements are readily available via any number of configurations yet while at the same
time entirely new subsystems can and will be added over time with little overall disruption. Further, every element
can and should be considered as an operational peer to any other element, realizing the principle of
[Separation of concerns](https://en.wikipedia.org/wiki/Separation_of_concerns).

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
*   # note: jdk7 required until all clients upgrade to jdk8
*   % for c in "java7"; do brew cask install $c; fi

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

*  upgrade to jdk8 (note: jdk7 required until all clients upgrade to jdk8)

## License

The code is published under the terms of the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
