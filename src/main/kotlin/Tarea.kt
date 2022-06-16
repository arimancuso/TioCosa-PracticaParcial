import java.time.LocalDate
import java.time.LocalTime

abstract class Tarea() { //COMMAND
    var fecha: LocalDate = LocalDate.now()
    var cumplida: Boolean = false
    lateinit var banda: Banda
    val tareaObservers = mutableListOf<TareaRealizadaObserver>()

    fun notificarTareaObserver() {
        tareaObservers.forEach { it.tareaRealizada() }
    }

    fun asignarBanda(unaBanda: Banda) {
        banda = unaBanda
    }

    fun realizarTarea() {
        ejecutar()
        cumplida = true
        notificarTareaObserver()
    }

    abstract fun ejecutar()

}

class RecolectarDinero(val comerciante: PersonaInvolucrada) : Tarea() {
    val porcentajeComision = 0.1
    var montoARecaudar = 0.00

    override fun ejecutar() {
        montoARecaudar = porcentajeComision * comerciante.ventasMensuales
        banda.sumarRecaudacion(montoARecaudar)
    }
}

class AbrirDeposito(var deposito: Deposito) : Tarea() {
    var costoCompra: Int = 0
    val precioMetroCuadrado: Int = 100

    override fun ejecutar() {
        costoCompra = precioMetroCuadrado * deposito.superficie
    }

}

class PrestarDinero(var comerciante: PersonaInvolucrada, var montoPrestado: Int) : Tarea() {
    var listaCobrarCuota: MutableList<CobrarCuota> = mutableListOf()
    var montoACobrar: Int = 0

    override fun ejecutar() {
        comerciante.recibirPrestamo(montoPrestado)
        montoACobrar = (montoPrestado * 2) / listaCobrarCuota.size
        listaCobrarCuota.forEach { it.cobrarPrestamo(montoACobrar) }

    }
}

class CobrarCuota(var comerciante: PersonaInvolucrada, var montoACobrar: Int) : Tarea() {
    var numeroCuota: Long = 0
    override fun ejecutar() {
        cobrarPrestamo(montoACobrar)
    }

    fun cobrarPrestamo(monto: Int) {
        numeroCuota++
        fecha.plusMonths(numeroCuota)
        comerciante.pagarPrestamo(montoACobrar)
    }
}

data class PersonaInvolucrada(val nombre: String, var ventasMensuales: Int) {
    var dineroPrestado: Int = 0
    fun nombreContiene(letra: String) = nombre.contains(letra)


    fun recibirPrestamo(monto: Int) {
        dineroPrestado += monto
    }

    fun pagarPrestamo(monto: Int) {
        dineroPrestado -= monto

    }
}

class Deposito(var superficie: Int) {

}

interface TareaRealizadaObserver {
    fun tareaRealizada() {

    }
}

class WppObserver(val sender: Sender) : TareaRealizadaObserver {
    var mensajeClave: String = ""
    override fun tareaRealizada() {
        enviarWpp(sender)
    }

    fun enviarWpp(sender: Sender) {
        sender.sendMensaje(crearMensaje())

    }
    // Ejemplo: “La puerca está en la pocilga - $ 500” (indica el cobro por protección a un comerciante por ese valor)

    fun crearMensaje(): Mensaje =
        Mensaje(
            Vito.numeroWpp,
            mensajeClave + "-" //+ recolectarDinero.montoARecaudar
        )
}


class InformarAFIPObserver(var banda: Banda, val sender: Sender, var tipoMovimiento: Int) : TareaRealizadaObserver {
    val montoMaximo: Int = 1000000
    val concepto: String = "VARIOS"

    override fun tareaRealizada() {
        if (superaMontoMaximo()) {
            sender.informar(crearInforme(tipoMovimiento))
        }
    }

    fun crearInforme(tipoMovimiento: Int): Informe =
        Informe(
            LocalTime.now(),
            tipoMovimiento,
            concepto,
            banda.dineroRecolectado
        )

    fun superaMontoMaximo() = banda.dineroRecolectado > montoMaximo

}

class RepartirDineroObserver(var banda: Banda) : TareaRealizadaObserver {

    override fun tareaRealizada() {
        banda.sumarRecaudacion(dineroParaLaBanda())
        repartirAIntegrantes()
    }

    fun dineroParaLaBanda() = banda.dineroRecolectado * 0.3

    fun repartirAIntegrantes() {
        banda.integrantes.forEach { integrante -> integrante.recolectarDinero(montoARepartirEntreIntegrantes()) }
    }

    fun montoARepartirEntreIntegrantes() = montoRestante() / banda.cantindadDeIntegrantes()

    fun montoRestante() = banda.dineroRecolectado - dineroParaLaBanda()


}

