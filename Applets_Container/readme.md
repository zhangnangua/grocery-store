# Pumpkin's applet

[TOC]

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

`pac` -> `pac_core` 、 `base.mvvm` 、 `base.ui` 
`app` -> `pac`

