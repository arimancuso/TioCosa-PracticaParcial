import java.awt.Menu

object Vito {
    var dineroRecaudado : Double = 0.00
    var bandas = mutableListOf<Banda>()
    var tareas = mutableListOf<Tarea>()
    val numeroWpp : Int = 11111111

    fun sumarRecaudacion(monto: Double){
        dineroRecaudado + monto
    }

    fun pagar(monto: Double){
        dineroRecaudado -= monto
    }


    fun asignarTarea(){
        //tareas.filter { tarea -> tarea.banda  }
        tareas.forEach { tarea -> tarea.asignarBanda(bandas.first{ it.leInteresaHacer(tarea) })
    }

}


}


