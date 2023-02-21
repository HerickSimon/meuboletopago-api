package br.com.g6.orgfinanceiro.model

data class UserBalanceResponse(
            private var receitas: Double? = null,
            private var despesas: Double? = null,
            private var saldo: Double? = null

) {
    fun getReceitas(): Double? {
        return receitas
    }


    fun setReceitas(receitas: Double?) {
        this.receitas = receitas
    }


    fun getDespesas(): Double? {
        return despesas
    }


    fun setDespesas(despesas: Double?) {
        this.despesas = despesas
    }


    fun getSaldo(): Double? {
        return saldo
    }


    fun setSaldo(saldo: Double?) {
        this.saldo = saldo
    }

}