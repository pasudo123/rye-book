package com.github.ryebook.common.util

import org.springframework.core.env.Environment
import org.springframework.core.env.get

object AppEnvironment

fun Environment.isDataInit(): Boolean {
    return this["custom-config.init-data"].toBoolean()
}
