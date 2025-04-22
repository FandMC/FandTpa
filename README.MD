# FandTpa 🌟
[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.example.com%2Fjob%2FFandTpa)](https://ci.example.com/job/FandTpa)
[![Download](https://img.shields.io/github/downloads/FandMC/FandTpa/total)](https://github.com/FandMC/FandTpa/releases)
> 一款集基础功能与扩展性于一身的多功能Minecraft插件，创新实现了可视化称号系统
## ✨ 主要功能
### 🛠️ 基础模块
- **智能传送系统**
    - `/tpa` 玩家间实时定位传送
    - `/back` 死亡点/传送点回溯
    - 家园系统（最多设置3个私人坐标点）
### 🏆 称号系统 ("基础实现")

### ⚡ 扩展性
- 多语言支持（内置中文/英文）
- 全功能API接口（支持开发扩展模块）
- 配置文件热重载
## 📥 快速安装
### 基础安装（推荐）
1. 下载最新版本 `FandTpa-x.x.x.jar`
2. 放入服务器 `plugins/` 目录
3. 重启服务器
### 开发者模式
```bash
git clone https://github.com/yourname/FandTpa.git
mvn clean package
