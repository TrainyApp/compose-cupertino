package io.github.alexzhirkevich.cupertino.adaptive

import platform.UIKit.UIDevice

actual val DefaultTheme: Theme = if (isAtLeastIOS26()) Theme.LiquidGlass else Theme.Cupertino

private fun isAtLeastIOS26(): Boolean {
    val version = UIDevice.currentDevice.systemVersion
    val majorVersion = version.split(".").firstOrNull()?.toIntOrNull() ?: 0
    return majorVersion >= 26
}
