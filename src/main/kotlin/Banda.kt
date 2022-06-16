abstract class Banda {

var integrantes = mutableListOf<Integrante>()
    val vito : Vito = Vito
    val porcentajeComisionBanda = 0.2
    var dineroRecolectado = 0.00
    var montoParaVito = 0.00
    lateinit var tarea: Tarea

    fun cantindadDeIntegrantes() = integrantes.size

    fun sumarRecaudacion(monto : Double){
        dineroRecolectado += montoParaLaBanda(monto)
        montoParaVito = monto - montoParaLaBanda(monto)
        vito.sumarRecaudacion(montoParaVito)
    }

    private fun montoParaLaBanda(monto: Double): Double {
        return porcentajeComisionBanda * monto
    }

    fun estaEnBancarrota() = dineroRecolectado <= 0

    fun leInteresaHacer(tarea: Tarea) = !estaEnBancarrota() && criterioEspecifico()

    abstract fun criterioEspecifico():Boolean

}

class Forajida() : Banda (){
    override fun criterioEspecifico() = integrantes.any { integrante -> integrante.quiereHacerTarea() }



}

class Sorora : Banda(){
    override fun criterioEspecifico() = integrantes.all { integrante -> integrante.quiereHacerTarea() }
}

class Tipica : Banda() {
    override fun criterioEspecifico() = esLider().quiereHacerTarea()

    fun esLider() = integrantes.find { integrante -> integrante.esLider }!!
}

