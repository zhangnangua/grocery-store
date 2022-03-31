package com.pumpkin.automatic_execution.util

import java.io.File

/**
 * 执行命令
 */
private fun execRuntimeProcess(command: String): Process? = Runtime.getRuntime().exec(command)


// TODO: 2022/2/20 需要测试该方案的可行性
/**
 * 模拟点击
 * @param x x轴坐标
 * @param y y轴坐标
 */
fun mockClick(x: Int, y: Int) {
    execRuntimeProcess("adb_shell shell input tap $x $y")
}