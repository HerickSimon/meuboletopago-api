package br.com.g6.orgfinanceiro.enumeration

enum class TypeMovement {
    RECEITA,
    DESPESA;

    companion object {
        operator fun get(type: String?): TypeMovement? {
            if (type == null) return null
            for (tp in values()) {
                if (tp.toString().toLowerCase() == type.toLowerCase()) return tp
            }
            return null
        }
    }
}
