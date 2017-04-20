#!/usr/bin/env bash
mvn package
mv target/*jar*.jar ~/AutoUpnp-3.0.jar
chmod 755 ~/AutoUpnp-3.0.jar
