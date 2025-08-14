#!/bin/sh

if [ -z "$1" ]; then
    echo "usage: $0 <version>"
    exit 1
fi

if [ -x "${HOMEBREW_PREFIX}/bin/gsed" ]; then
    SED="${HOMEBREW_PREFIX}/bin/gsed -E"
else
    SED="/usr/bin/sed -E"
fi

${SED} -i "s|(def \^:private version).*|\1 \"$1\")|" bb/gits.bb
