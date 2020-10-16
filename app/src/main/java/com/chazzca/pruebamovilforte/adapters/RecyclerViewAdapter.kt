package com.chazzca.pruebamovilforte.adapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chazzca.pruebamovilforte.R
import com.chazzca.pruebamovilforte.modelos.ModeloListadoUsuarios
import com.chazzca.pruebamovilforte.utils.changeBackgroundDrawable
import com.chazzca.pruebamovilforte.utils.inflate
import kotlinx.android.synthetic.main.listado_row.view.*

class RecyclerViewAdapter(private val lista: MutableList<ModeloListadoUsuarios>,
                          private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ListadoHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListadoHolder {
        val inflatedView = parent.inflate(R.layout.listado_row, false)
        return ListadoHolder(inflatedView)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ListadoHolder, position: Int) {
        val itemLista = lista[position]
        holder.bindListado(itemLista,listener)
    }

    class ListadoHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var listado: ModeloListadoUsuarios? = null
        private var listener : OnItemClickListener? = null


        fun bindListado(listado: ModeloListadoUsuarios, listener: OnItemClickListener) {
            this.listado = listado
            this.listener = listener

            view.txtUsuario.text = listado.nombreCompleto
            view.txtCorreo.text =  listado.correoElectronico

            if(adapterPosition % 2 == 0){
                view.fondo.setBackgroundColor(Color.LTGRAY);
            }

            view.btnEditar.setOnClickListener{
                listener.onItemClickEditar(adapterPosition)
            }

            view.btnBorrar.setOnClickListener{
                listener.onItemClickBorrar(adapterPosition)
            }

        }

    }

    interface OnItemClickListener {
        fun onItemClickEditar(posicion: Int)

        fun onItemClickBorrar(posicion: Int)

    }
}