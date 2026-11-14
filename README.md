<div align="center">

# 题了个题 · 平板 / 手机 / TV

### 先学再看 · 让每一点进步被看见

**Learn first. Watch later.** · 支持Android 平板、手机和电视，不需要自建服务器

[![Stars](https://img.shields.io/github/stars/SteveTDX/StudyOnTV?style=for-the-badge&logo=github&label=Stars)](https://github.com/SteveTDX/StudyOnTV/stargazers)
[![Release](https://img.shields.io/github/v/release/SteveTDX/StudyOnTV?style=for-the-badge&label=Release)](https://github.com/SteveTDX/StudyOnTV/releases/latest)
[![Platform](https://img.shields.io/badge/Android-5.0%2B-3DDC84?style=for-the-badge&logo=android)](https://github.com/SteveTDX/StudyOnTV)
[![License](https://img.shields.io/badge/license-Personal%20Use%20Only-orange?style=for-the-badge)](./LICENSE)

</div>

---

> **关于本仓库**：这是上游 StudyOnTV（原作者 chadyi）的个人定制 fork，在当前版本基础上修复了若干问题，并把版本检查指向本仓库。完整改动见文末「本 fork 的修订」；上游最新版另存于 `upstream-v0.3.17` 分支。

## 安装

Android 平板、手机和电视使用同一套规则，分别安装对应版本：

- [**手机/平板/TV GitHub 下载（唯一发布渠道）**](https://github.com/SteveTDX/StudyOnTV/releases/latest)

本仓库只在自己的 Releases 发布版本，不提供任何第三方备用下载，也不从上游仓库或源库地址检查更新。

手机和平板直接下载安装；电视可通过 U 盘或 ADB 安装。首次打开设置管理密码，权限可以按需开启，也可以稍后再设置。

👉 **[安装指南 → docs/install.md](./docs/install.md)**

---

## 它能做什么

孩子先完成学习任务，再解锁设备使用时间，到时显示新的学习任务。平板和手机支持触屏、公式、语音输入与手写，电视支持遥控器。题库、规则、使用记录和加练池都保存在被控设备。

- **开放题型**：单选、多选、判断、填空、主观题；题干和选项支持 LaTeX 公式，离线显示。
- **多种玩法**：按组闯关、连续答对、自由练习，可设置正确率和任务数量。时段可选「仅时段内可用」或「仅时段内需要答题」；后者在时段外自由使用、不扣余额，适合白天老人看电视、17–21 点再管控。
- **星级奖励**：1–5 星题目分别设置时长奖励，仍受每日上限约束。
- **累计加练**：答错达到阈值就增加加练题，跨天、重启保留，答对加练题逐道扣除。
- **主观题批改**：家长手动批改、WorkBuddy / Codex 等 Agent 批改，或配置 DeepSeek 等兼容 AI API。
- **间隔重复**：在规则里打开后，优先复习到期题，答对后延长间隔，答错后提前复习，进度跨重启保留。
- **答题反馈**：答完在下方显示对错、正确答案与解析，孩子按“继续”进入下一题；鼓励语可在面板自定义。
- **电视遥控器与音频**：方向键即可完成作答、填空和更新。学习页出现时先暂停播放器、请求音频焦点；仍继续播放时静音兜底，退出管控后恢复原静音状态，原先未静音时恢复媒体音量。
- **深色 / 浅色**：电视默认深色，手机和平板跟随系统，家长可在面板切换。
- **AI 批改更省心**：内置 DeepSeek、通义千问、智谱、Kimi、硅基流动、本地 Ollama 等预设，一键测试连接、获取模型列表。
- **管控保护**：规则可开启「管控期间拦截系统设置」，家长验证后可临时进入，自由时段不拦截。支持设备管理员的系统可另行授权防卸载；这些权限均可选。
- **开机恢复**：接收 Android 开机、部分系统快速开机和覆盖升级广播，尝试恢复管理服务；厂商自启动及遥控器待机清后台场景见安装指南。
- **密码管理**：首次设置管理密码，扫码面板和 API 都要验证。学习页标题连点 5 次，或焦点在最上方时遥控器「上」短按 5 次，可重新打开家长验证入口；面板里也可以修改密码。

[玩法说明](./docs/gameplay.md) · [题库与批改 API](./docs/ai-api.md) · [家庭自动化联动](./docs/integration.md) · [与 Home Assistant 联动](./docs/home-assistant.md) · [示例题库](./data/banks/open-play.json)

管理页面由被控设备在局域网内提供。离线题目直接在设备上完成；AI 批改需要网络和家长配置的服务。

---

## 截图

<div align="center">
<img src="./docs/screenshots/v0.3.0/tablet-quiz.png" width="780" alt="平板公式与主观题作答"><br><sub>平板学习 · 公式、文字与手写作答</sub><br><br>
<img src="./docs/screenshots/v0.3.0/tablet-handwriting.png" width="780" alt="平板手写作答"><br><sub>手写过程可提交家长、Agent 或图像模型批改</sub><br><br>
<table><tr>
<td align="center"><img src="./docs/screenshots/v0.3.0/phone-quiz.png" width="280" alt="手机公式与主观题作答"><br><sub>手机学习 · 与平板共用题库和规则</sub></td>
<td align="center"><img src="./docs/screenshots/v0.3.0/parent-review.png" width="280" alt="家长批改与 AI 设置"><br><sub>家长批改与 AI 设置</sub></td>
</tr></table>

<br>

<img src="./docs/screenshots/v0.3.0/tv-quiz.png" width="780" alt="电视端答题">
<br><sub>遥控器选题，答完在下方显示对错、正确答案与解析</sub>

<br><br>

<img src="./docs/screenshots/v0.3.0/tv-pair.png" width="780" alt="家长扫码连接">
<br><sub>家长用手机扫码，同一个 WiFi 即可管理</sub>

<br><br>

<img src="./docs/screenshots/v0.3.0/tv-lock-schedule.png" width="780" alt="未到可用时段">
<br><sub>未到可用时段时停留提示页，显示下次可用日期和时间</sub>

<br><br>

<table><tr>
<td align="center"><img src="./docs/screenshots/v0.3.0/parent-rules.png" width="220" alt="按周设置可用时间"><br><sub>按周设置可用时间，节假日可同步</sub></td>
<td align="center"><img src="./docs/screenshots/v0.3.0/parent-bank.png" width="220" alt="题库表格与 AI 出题"><br><sub>题库表格 · AI 出题</sub></td>
<td align="center"><img src="./docs/screenshots/v0.3.0/parent-log.png" width="220" alt="使用记录"><br><sub>7天使用记录</sub></td>
</tr></table>

<img src="./docs/screenshots/v0.3.0/parent-desktop.png" width="780" alt="电脑上的家长管理面板"><br><sub>电脑管理 · 规则、题库、批改与学习记录</sub>

</div>

---

## 家长能设置什么

| 设置项 | 说明 |
|---|---|
| 每天最多看 | 超出后当天锁死，第二天重置 |
| 每次答题解锁 | 一组题答完解锁几分钟 |
| 每组题量 | 一组几道题 |
| 答对比例 | 答对多少题才算通过（百分比） |
| 每天最多答几组 | 答完上限即锁定 |
| 时段用途 | 仅时段内可用 / 仅时段内需要答题；按周设置，半小时一格，可选节假日按周日 |
| 系统设置保护 | 管控期间拦截，家长验证后临时放行；需无障碍或悬浮窗配合使用情况访问 |
| 间隔重复 | 开启后优先抽取到期复习题，再补新题 |
| 玩法 | 按组闯关、连续答对、自由练习 |
| 星级奖励 | 分别设置 1–5 星每题奖励 |
| 答错加练 | 累计错误阈值与每次加练题量 |
| 主观题批改 | 家长 / Agent 或 AI API，支持及格分与评分标准 |

---

## 题库

- 手机上新增、删除、即时生效
- **导出 JSON** 发给群里其他家长，对方「导入」一贴即用
- 内置 50 题：唐诗填空、100 以内加减法、英语基础单词
- 让 AI 批量出题：把 [接口说明](./docs/ai-api.md) 发给 WorkBuddy / Codex 等 Agent，让它直接往被控设备里写题

---

## 它是怎么工作的

```
被控设备 App（手机 / 平板包，独立 TV 包）
├── 内置网页：答题 / 锁屏 / 扫码页（离线可用）
├── 内置 HTTP 服务：局域网 8099 端口
└── 本地 SQLite：规则 + 题库 + 事件记录

手机端
└── 扫二维码 → http://<设备IP>:8099/admin/ → 输入管理密码（多个网卡地址会各显示一个地址和二维码）
```

管理密码在首次使用时设置。扫码只打开地址，面板和 API 都需要验证；没有项目自建云端和账号体系。

---

## FAQ

**Q：孩子把 App 强退了怎么办？**
A：可以按设备条件开启悬浮窗或无障碍，并允许后台运行。系统强制停止应用、撤销权限或卸载会影响管控；重新打开后可以在家长设置中调整权限。

**Q：支持开机自启动吗？**
A：已接入 Android 开机与部分快速开机广播。请先打开 App 完成首次设置，并按厂商要求允许自启动、后台运行。小米等电视的遥控器待机、厂商清后台未必发送开机广播，需要在实际设备上验证，详见 [安装指南](./docs/install.md)。

**Q：支持哪些设备？**
A：Android 5.0+ 的平板、手机和电视。厂商后台限制需要在实际设备上确认，鸿蒙 NEXT / Tizen / webOS 待支持。

**Q：数据会传到网上吗？**
A：题库和记录保存在被控设备。开启 AI 批改后，题目和答案会发送到家长配置的服务；项目中已有的第三方统计配置另见 [统计说明](./docs/analytics.md)。

**Q：更换电视会丢数据吗？**
A：题库可以在手机管理页导出 JSON，换电视后重新导入。

---

## 技术栈

| 层 | 技术 |
|---|---|
| Android 原生 | Kotlin，零第三方依赖 |
| 学习页面 | 原生 ES5 + CSS，兼容老 WebView |
| 手机管理页 | React + shadcn/ui |
| 本地存储 | SQLite（Android 内置） |
| 局域网通信 | Kotlin ServerSocket，HTTP/1.1 |

---

## 本 fork 的修订

本仓库 fork 自上游 StudyOnTV（原作者 chadyi），基于 **v0.3.9（versionCode 14）** 做个人家庭定制。上游最新版本另存于 [`upstream-v0.3.17`](https://github.com/SteveTDX/StudyOnTV/tree/upstream-v0.3.17) 分支，便于以后对照合并。改动仅限个人家庭使用，遵循原项目 License。

### 修订清单

| # | 修订 | 涉及文件 | 说明 |
|---|---|---|---|
| 1 | 答题翻页死循环 | `LearningEngine.kt` | 修复答完一题又回到第一题的循环（提交 `7ef73a5`） |
| 2 | 规则保存即时生效 | `LocalStore.kt`、`LanServer.kt`、`app/web/assets/js/app.js` | 后台只改「全天可用 / 起止时间」时重建 `weekSlots` 网格；保存后立即 `GateService.refreshNow()`；前端在任意界面检测到配置版本变化即刷新门禁与主题 |
| 3 | 填空框回车 = 确定 | `TvTextInputActivity.kt`、`app/web/assets/js/app.js` | TV 原生「填写答案」页按软键盘或实体键盘回车即完成，不再插入换行；手机 / 电脑的可编辑填空框回车直接提交答案（Shift 组合与输入法组词不拦截） |
| 4 | 修复 TV 输入答案回填丢失 | `MainActivity.kt` | TV 上 `onStop()` 原本无条件销毁 WebView，原生输入页返回时答案无法回填；改为仅在非输入页场景销毁 |
| 5 | 更新检查指向本仓库 | `Updater.kt`、`UpdateActivity.kt`、`scripts/make-update-manifest.mjs`、`docs/*`、管理页、README | 检查顺序：自定义地址 → 本仓库 Release API → 本仓库 `update.json`；「手动下载」与发版脚本地址一并改为本仓库；v0.3.10 起彻底移除第三方兜底与上游链接 |
| 6 | 隐私清理 | `.gitignore` 等 | 仓库不含真实设备地址、管理密码、会话令牌、本地绝对路径、调试日志与自建题库；新增忽略规则防止误提交 |
| 7 | 多网卡多管理地址 / 多二维码 | `NetworkAddresses.kt`、`LanServer.kt`、`DeviceRuntime.kt`、`app/web/*`、`admin/assets/index-*.js`、`docs/*` | 设备同时有 WiFi / 网线 / 热点 / VPN 多个地址时，连接页列出全部管理地址并为每个地址生成对应二维码（标注网卡来源），地址随网络变化自动刷新；`/api/hello`、`/api/device` 增加 `ips`、`urls`、`addresses` 字段，后台「设备」页显示多个 IP；单地址时保持原样 |
| 8 | 发布与更新源收敛到本仓库 | `Updater.kt`、`build.gradle.kts`、`update.json`、README、docs、`admin/assets/*` | 发布 v0.3.10（versionCode 15）与 `update.json`，检查更新只走本仓库 Release API / 清单；删除第三方更新实现与所有上游仓库链接，保留 LICENSE 版权声明 |
| 9 | 填空题提交与判分优化 | `TvTextInputActivity.kt`、`app/web/assets/js/app.js`、`LearningEngine.kt` | TV 原生输入页点「确定」/回车即直接提交答案，无需再点一次「提交答案」；参考答案为英文/数字等拉丁字符的填空题不进 AI 批改队列，直接本地完全匹配并忽略所有空格差异（大小写、全半角仍归一），中文近义题、主观题与手写作答仍按原逻辑批改 |
| 10 | 家长界面「进入桌面」按钮 | `app/web/index.html`、`app/web/assets/js/app.js`、`DeviceBridge.kt` | 在连接/管理界面「修改密码」按钮右侧新增「进入桌面」按钮，家长点击后无条件退出软件并回到安卓系统桌面；新增无门禁的 `enterDesktop()`（区别于受学习状态约束的 `exitToLauncher()`） |
| 11 | 「进入桌面」同时暂停悬浮门禁 | `Prefs.kt`、`GateService.kt`、`DeviceBridge.kt`、`MainActivity.kt` | 点「进入桌面」后持久化暂停悬浮覆盖（锁定桌面）：悬浮层/无障碍层/GateActivity 兜底都不再弹出，计时统计不受影响；下次手动打开 APK（回到学习页）时自动恢复门禁并立即刷新 |

### 自动更新指向

`Updater.OWNER_REPO` 统一指向 `SteveTDX/StudyOnTV`，改 fork 只需改这一处常量：

- Release API：`https://api.github.com/repos/SteveTDX/StudyOnTV/releases/latest`
- 更新清单：`https://raw.githubusercontent.com/SteveTDX/StudyOnTV/main/update.json`（及 jsDelivr 镜像）
- 手动下载 / 首页徽章：`https://github.com/SteveTDX/StudyOnTV/releases/latest`

所有地址均指向本仓库，不再回退第三方通道，也不访问源仓库。发版与检查顺序详见 [docs/update.md](./docs/update.md)。

### 构建与签名

```bash
export ANDROID_HOME=<Android SDK 路径>
cd app/native
./gradlew assembleTvDebug       # TV 版；assembleMobileDebug 为手机版
```

正式安装需自行签名。若签名与设备既有版本不一致，覆盖安装会失败，需先卸载（会清空本机题库、规则与学习记录，请先导出）。

### 验证与隐私说明

上述修订已在 Android TV 模拟器与实体电视上完成端到端验证：规则保存后当帧生效、回车提交与答案回填、答完自动进入下一题、后台管理页可正常访问、多网卡时连接页生成多个地址与二维码、填空「确定」即提交与英文答案本地判分。

**本仓库不包含任何真实设备的网络地址、管理密码、会话令牌、设备标识或本地文件绝对路径。** 文档里出现的 `192.168.x.x` 均为上游原有的示例地址；仓库也不含任何第三方更新服务标识。自建题库属于设备端数据，通过管理面板导入，不随源码分发。

---

## 社区建议与致谢

- [LINUX DO](https://linux.do) **xiuweng**：支持手机管控、通过隐藏手势重新打开管理入口，以及答错后累计加练、重启保留加练池。
- [LINUX DO](https://linux.do) **free-swimming**：设置题目星级难度，并为不同难度配置不同的时长奖励。
- **Issue #2**：补充修改密码、AI 服务预设与连接测试，改进题库管理，图表与公式按需加载。
- **Issue #3**：答题结果放在题目下方，显示正确答案、解析与鼓励语，由孩子手动继续；加强后台音频处理。
- **Issue #4**：区分可用时段与答题管控时段，增加自由时段、可选防卸载和音频静音兜底。
- **Issue #5**：补充开机启动说明。

这些建议也延伸到其他端，欢迎反馈使用体验。

---

本项目已链接并认可 [LINUX DO](https://linux.do) 社区。

## License

源码可查阅，**仅限个人家庭使用**，禁止商业用途和二次分发。欢迎 Issue 反馈问题。

商业授权请联系原作者（见 [LICENSE](./LICENSE)）。
