# 版本检查与更新提示

客户端回到学习页时按顺序检查更新源，**有新版才提示**，结果缓存 6 小时。检查源全部指向本仓库（fork）；本仓库暂未发版或 GitHub 不通时，回退到蒲公英通道（见下文「检查顺序与备用来源」）。

```
GET https://api.github.com/repos/SteveTDX/StudyOnTV/releases/latest
GET https://raw.githubusercontent.com/SteveTDX/StudyOnTV/main/update.json
```

| 字段 | 用途 |
|---|---|
| `tag_name` | 版本号，写成 `v1.2.3` |
| `body` | 更新说明，直接显示在提示里；**含 `[force]` 就强制更新** |
| `assets[].browser_download_url` | 有 `.apk` 就点「立即更新」直接下载，没有就打开 Release 页 |

## 两种提示

| 类型 | 触发 | 表现 |
|---|---|---|
| 强制更新 | `body` 里有 `[force]` | 全屏页，返回键无效，只能点「立即更新」 |
| 普通更新 | 版本号更高但没有 `[force]` | 右上角小卡片，可关闭 |

## 发版流程

```bash
# 1. 改版本号（app/native/app/build.gradle.kts 里的 versionCode / versionName）
# 2. 打包
cd app/native && ./gradlew assembleRelease      # 需签名
# 3. 发布到本仓库
gh release create v0.3.10 <tv.apk> <mobile.apk> \
  --repo SteveTDX/StudyOnTV \
  --title "v0.3.10" \
  --notes "· 修复……"
```

要强制更新时，在说明里加一行：

```
[force]
本次必须更新
```

发布后客户端最长 6 小时内（或下次启动）就会看到提示。

> **签名提醒**：本 fork 若使用自己的签名密钥，就与官方 release 签名不同，覆盖安装会失败，需先卸载。卸载会清空设备上的题库、规则与学习记录，请先在管理面板导出题库。

## 检查顺序与备用来源

客户端按顺序尝试，第一个成功返回版本信息的来源为准：

1. 家长在管理面板「规则与设备 → 版本更新 → 高级」里填写的自定义地址（GitHub Release JSON 或 update.json 格式）
2. 本仓库的 GitHub Releases API
3. 本仓库 main 分支的 `update.json`：raw.githubusercontent.com → cdn / fastly / gcore.jsdelivr.net
4. 以上都不可用（尚未发版、GitHub 不通）时，回退上游原有的蒲公英检查通道

APK 下载依次尝试：直连下载地址 → 家长在面板配置的加速镜像。
镜像是第三方服务，所以下载后会校验 SHA-256（GitHub API 的 `digest` 或 update.json 中的 `sha256`），
并要求 APK 签名与已安装版本一致，否则拒绝安装。「手动下载」按钮打开本仓库的 Release 页面。

### 发版时生成 update.json

```bash
node scripts/make-update-manifest.mjs release-artifacts --notes "· 修复……"
git add update.json && git commit -m "update.json v0.3.10"
```

脚本生成的 APK 下载地址指向 `https://github.com/SteveTDX/StudyOnTV/releases/download/...`。
jsDelivr 对分支文件最多缓存约 12 小时；GitHub API 能访问时不受影响。
