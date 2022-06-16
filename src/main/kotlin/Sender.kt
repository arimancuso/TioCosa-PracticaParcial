import java.time.LocalTime

interface Sender {
    fun sendMensaje(mensaje: Mensaje)
    fun informar(informe: Informe)
}

data class Mensaje(val to: Int, val content: String)

data class Informe(var fechaYHora :LocalTime, var tipoDeMovimiento : Int, var concepto : String, var importe :Double)