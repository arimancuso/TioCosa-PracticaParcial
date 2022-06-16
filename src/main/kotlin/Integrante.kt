import java.time.LocalDate

class Integrante(){
    var tipoDePersonalidad : PersonalidadIntegrante = Combinada
    var esLider :Boolean = true
    var montoRecaudado : Double = 0.00
    fun quiereHacerTarea() = tipoDePersonalidad.quiereHacerTarea()
    fun recolectarDinero(monto : Double){
        montoRecaudado + monto
    }

}

interface PersonalidadIntegrante {  // COMPOSITE
    fun quiereHacerTarea() : Boolean
}

class AltoPerfil(var cobroPorTarea: Int) : PersonalidadIntegrante{
    override fun quiereHacerTarea() = cobroPorTarea >= 1000

}

class Culposo(): PersonalidadIntegrante {
    lateinit var personaInvolucrada : PersonaInvolucrada
    override fun quiereHacerTarea() = personaInvolucrada.ventasMensuales < 5000
}

class Alternante(var fecha :LocalDate, var integrante: Integrante, var monto :Int) : PersonalidadIntegrante {
    lateinit var personalidad : PersonalidadIntegrante

    override fun quiereHacerTarea(): Boolean {
        asignarPersonalidad()
        return personalidad.quiereHacerTarea()
    }

    fun asignarPersonalidad() {
        return if (esPar(fecha)) {
            personalidad = Culposo()
        } else personalidad = AltoPerfil(monto)
    }

        fun esPar(fecha :LocalDate) = fecha.monthValue % 2 == 0
}

class Cabulero(var personaInvolucrada : PersonaInvolucrada): PersonalidadIntegrante {
    var letra : String = "x"
    override fun quiereHacerTarea() = !personaInvolucrada.nombreContiene(letra)
}

object Combinada: PersonalidadIntegrante {
    val personalidades = mutableListOf<PersonalidadIntegrante>()

    override fun quiereHacerTarea() = personalidades.all{it.quiereHacerTarea()}

    fun agregarPersonalidad(personalidad:PersonalidadIntegrante) {
        personalidades.add(personalidad)
    }
}
