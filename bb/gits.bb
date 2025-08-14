#!/usr/bin/env bb
(ns gits
  (:require
   [babashka.fs :as fs]
   [babashka.process :as ps]
   [taoensso.timbre :as timbre]))

;; FIXME: must set up timbre
(timbre/merge-config! {:min-level :info})

(def ^:private version "0.3.1")

(defn print-version []
  (println "gits" version))

(defn usage []
  (println "

## SYNOPSIS

gits [options] [git-command] [dir]

dir 内の git dirs に対し、git something を実行する。
options, git-command, dir の順番は変えられない。
gits 単独では、`gits --parallel status .` のように働く。

## EXAMPLES

- gits --paralell status ~/projects
  ~/projects 内の複数の git フォルダの状態を並列にチェックする。

- gits --serial fetch .
  作業ディレクトリ中の複数の git フォルダを逐次 fetch する。

- gits pull .
  作業ディレクトリ中の複数の git フォルダを並列に pull する。

- gits
  gits --parallel status . のように働く。

- gits --help
  ヘルプを表示。

- gits --version
  バージョンナンバーを表示。"))

(defn abbrev
  "もし、s が clean で終わっていたら clean を返す"
  [s]
  (if (re-find #"nothing to commit" s)
    "clean\n"
    s))

(defn git-sh
  "ディレクトリを引数に取り、git verb を実行する関数を返す。"
  [verb]
  (fn [dir]
    (let [ret (ps/shell {:dir dir :out :string :err :string}
                        (str "git " verb))]
      (str dir " ... " (-> (:out ret) abbrev)))))

(defn git-dir?
  "judge if `dir` is under git to check the existance of `dir/.git`."
  [dir]
  (fs/exists? (str dir "/.git")))

(defn git-dirs
  "returns the list of git controlled subdirs under `dir`."
  [dir]
  (filter git-dir? (fs/list-dir (fs/expand-home dir))))

(comment
  (git-dirs "/Users/hkim")
  (git-dirs "/Users/hkim/projects")
  (git-dirs "~/projects")
  :rcf)

(defn gits-debug [verb dir]
  (println "verb: " verb)
  (println "dir: " dir)
  (let [verb (git-sh verb)
        dirs (git-dirs dir)]
    (doseq [dir dirs]
      (try
        (println dir)
        (verb dir)
        (catch Exception e
          (println (.getMessage e))
          (throw (Exception. "git-debug")))))))

(defn gits
  ([] (gits "--parallel" "status" "."))
  ([dir] (gits "--parallel" "status" dir))
  ([verb dir] (gits "--parallel" verb dir))
  ([opt verb dir]
   (if (or (= opt "--serial") (= opt "--debug"))
     (gits-debug verb dir)
     (doall (pmap (git-sh verb) (git-dirs dir))))))

(defn -main
  [& _]
  (case (first *command-line-args*)
    "--debug or --serial" (println "debug")
    "--version" (print-version)
    "--help" (usage)
    (println (apply gits *command-line-args*))))

(-main)
