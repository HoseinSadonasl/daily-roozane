package com.abc.daily.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class ByName(orderType: OrderType): NoteOrder(orderType)
    class ByDate(orderType: OrderType): NoteOrder(orderType)
}
