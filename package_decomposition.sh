#!/bin/bash
# src ディレクトリ内の .scala ファイルを、パッケージごとにディレクトリへ振り分けます。

function getTargetFiles() {
    target_dir=$1
    for path in $target_dir/*; do
        if [ -f $path ]; then
            ext=${path##*.}
            if [ $ext = "scala" ]; then
                targetFiles+=("$path")
            fi
        elif [ -d $path ]; then
            getTargetFiles $path
        fi
    done
}

function decomposition() {
    target=$1
    for src_file in ${targetFiles[@]}; do
        file_name=`basename $src_file`
        package=`cat $src_file | sed -n -e "s/^package\s*//p"`
        package_dir=`echo ${package/\./\//}`
        dest_dir=${PWD}/$target/$package_dir
        dest_file=$dest_dir/$file_name

        if [ ! -e "$dest_dir" ]; then
            mkdir -p $dest_dir
        fi

        if [ $src_file != $dest_file ]; then
            git mv $src_file $dest_file
        fi
    done
}

src=$1

targetFiles=()
getTargetFiles ${PWD}/${src}/main/scala
decomposition ${src}/main/scala

targetFiles=()
getTargetFiles ${PWD}/${src}/test/scala
decomposition ${src}/test/scala

exit 0
