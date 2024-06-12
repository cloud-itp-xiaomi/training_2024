# week01 笔记

## Vite

*搭建 Vite 项目*

`npm create vite@latest`

## Git

*创建新仓库* 创建新文件夹，打开，执行 `git init`

*检出仓库* `git clone`

***工作流*** 本地仓库由git维护的三棵“树”组成。第一个是==工作目录==，它持有实际文件；第二个是==暂存区（Index）==，它像个缓存区域，临时保存改动；最后是==HEAD==，它指向最后一次提交的结果

![git工作流](./git工作流.png)

*添加和提交*

- 提出更改（把文件添加到暂存区） `git add <filename>` `git add *`
- 实际提交更改（提交到 HEAD，还没到远端仓库） `git commit -m "代码提交信息"`

*删除*

- `git rm -r 文件夹`
- `git rm 文件`

*推送改动*

- 将改动提交到远端仓库 `git push origin master` *master* 可以换成要推送的任何分支
- 还没有克隆仓库，并且想要将仓库连接到某个远程服务器 `git remote add origin <server>`

*分支*

master是默认分支，分支用来将特性开发绝缘开，在其他分支上进行开发，完成后再将它们合并到主分支上

- 创建分支 `git checkout -b feature_x`
- 切换回主分支 `git checkout master`
- 删除新建分支 `git branch -d feature_x`
- 除非将分支推送到远端仓库，不然该分支就是不为他人所见的 `git push origin <branch>`

*更新与合并*

- 更新本地仓库至最新改动 `git pull`
- 合并其他分支到当前分支 `git merge <branch>`
- 这两种情况下，git会尝试去自动合并改动，可能会出现冲突，需要修改文件来手动合并冲突，改完后执行 `git add <filename>` 以将它们标记为合并成功
- 合并改动前，可以预览差异 `git diff <source_branch> <target_branch>`

*标签*

- 创建标签 `git tag 1.0.0 ID`
- 获取提交ID `git lig`

*替换本地改动*

- 使用HEAD中的最新内容替换掉工作目录中的文件，已添加到暂存区的改动及新文件不会受到影响 `git checkout -- <filename>`
- 丢弃本地的所有改动与提交，到服务器上获取最新的版本历史，并将本地主分支指向它 `git fetch origin` `git reset --hard origin/master`