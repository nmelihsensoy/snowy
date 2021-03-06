#!/bin/bash
# Script to download latest blockevent binary from github to Snowy's native library folder.

repo_latest_json="https://api.github.com/repos/nmelihsensoy/blockevent/releases/latest"
jni_dir="app/src/main/jniLibs"
jni_lib_name="blockevent.so"
checksums="checksums.txt"
tmp_dir="tmp"
binaries=("blockevent_arm" "blockevent_arm64" "blockevent_x86" "blockevent_x86_64")
abis=("armeabi-v7a" "arm64-v8a" "x86" "x86_64")
egrep_exp=""

for bin in ${binaries[@]}; do
    egrep_exp="$egrep_exp|browser_download_url.*$bin\""
done

egrep_exp="${egrep_exp#|}|$checksums"

mkdir $tmp_dir

curl -s $repo_latest_json \
| egrep $egrep_exp \
| cut -d : -f 2,3 \
| tr -d \" \
| wget -i - -P $tmp_dir

for index in ${!abis[@]}; do
    mv "$tmp_dir/${binaries[$index]}" "$jni_dir/${abis[$index]}/$jni_lib_name"
done

rm -rf $tmp_dir

