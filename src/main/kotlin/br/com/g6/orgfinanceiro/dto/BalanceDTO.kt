package br.com.g6.orgfinanceiro.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*


data class BalanceDTO (

    private var debt: Double? = 0.0,
    private var credit: Double? = 0.0,
    private var balance: Double? = 0.0
) {
    fun getCredit(): Double? {
        return credit
    }

    fun setCredit(credit: Double?) {
        this.credit = credit
    }

    fun getDebt(): Double? {
        return debt
    }

    fun setDebt(debt: Double?) {
        this.debt = debt
    }

    fun getBalance(): Double? {
        return balance
    }

    fun setBalance(balance: Double?) {
        this.balance = balance
    }
}