# Pumpkin's applet

[TOC]
需要加多进程

## 包结构说明

pac -> full name is pumpkin applets container

```
.
├── app
├── base
│   ├── mvvm        >>> 非小程序, mvvm基础框架
│   └── ui          >>> 非小程序，原生ui控件集合
├── pac             >>> 小程序核心能力，默认页面交互 / api实现注册 / 进程实现等 / webview重用
└── pac_core        >>> 核心交互  / api注册 
```

## 依赖关系整理
`native_game` -> `native_game_manager.*` 
`base.mvvm` -> `base.ui`、`base.data`
`parse` -> `base.data`
`pac_core` -> `web_cache`
`pac` -> `pac_core` 、 `base.mvvm` 、`parse`
`app` -> `pac`、`native_game`

