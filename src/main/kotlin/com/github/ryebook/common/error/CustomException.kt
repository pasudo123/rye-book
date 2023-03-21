package com.github.ryebook.common.error

open class CustomException(
    override val message: String
) : RuntimeException(message)

class DomainPolicyException(
    override val message: String
) : CustomException(message)

class DataNotFoundException(
    override val message: String
) : CustomException(message)

class SystemStateChangeException(
    override val message: String
) : CustomException(message)