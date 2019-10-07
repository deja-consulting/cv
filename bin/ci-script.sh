#!/usr/bin/env bash

main_version_regex="^[0-9]\\.[0-9]\.[0-9]$"
snapshot_version_regex="^[0-9]\\.[0-9]\.[0-9]-SNAPSHOT$"

case "$TRAVIS_BRANCH" in
  'release/latest')
    if [[ ! "$BUILD_VERSION" =~ $main_version_regex ]]; then
      echo "On branch release/latest, build version must be a semantic version, such as 1.2.3, but found: $BUILD_VERSION" >&2
      exit 1
    fi
    ;;
  'release/next')
    if [[ ! "$BUILD_VERSION" =~ $snapshot_version_regex ]]; then
      echo "On branch release/next, build version must be a semantic version ending in -SNAPSHOT, such as 1.2.3-SNAPSHOT, but found: $BUILD_VERSION" >&2
      exit 1
    fi
    ;;
esac

sbt "++$TRAVIS_SCALA_VERSION" test run
