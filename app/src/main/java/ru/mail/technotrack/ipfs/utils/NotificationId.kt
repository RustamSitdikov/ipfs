package ru.mail.technotrack.ipfs.utils

import java.util.concurrent.atomic.AtomicInteger

object NotificationId {
    private val value = AtomicInteger(0)
    val id: Int
        get() = value.incrementAndGet()
}