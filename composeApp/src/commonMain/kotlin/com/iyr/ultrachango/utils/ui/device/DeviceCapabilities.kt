package com.iyr.ultrachango.utils.ui.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

class DeviceCapabilities

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp
