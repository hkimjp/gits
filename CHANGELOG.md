# gits.clj

## Unreleased

- gits --serial 時にエラー終了させる。bash の -e みたいの。
- `gits pull .`  を `gits pull` と間違うこと(. を忘れる)が多い。

## 0.3-SNAPSHOT

- (mav verb dirs) をバラした。
- renamed `Makefile` to `Justfile`

## 0.2.2 - 2023-12-12
- gits が正常終了しなかったのはどのディレクトリか？
  try~exception の位置を git() に移動した。

## 0.2.1 - 2023-12-11

## 0.2.0 - 2023-12-11
- log
- help, version 表示
- try `gits --serial branch .` and so on.

## 0.1.5 - 2023-10-02
- ヘルプが gits.cj のままなのを修正した。

## 0.1.4 - 2023-10-02
- added bump-version.sh
- インストール先スクリプト名から .clj をとって、gits とした。

## 0.1.2 - 2023-10-01
- add an example, `gits.clj`

## 0.1.1 - 2023-10-01
- git flow init
- git-dir?
- git-dirs
- usage
- Makefile (to ~/bin/gits.clj)

## 0.1.0 - 2023-10-01
- initialized repository

    git init
    gh repo create hkim0331/gits --public
    git remote add ssh://git@github.com/hkim0331/gits.git
