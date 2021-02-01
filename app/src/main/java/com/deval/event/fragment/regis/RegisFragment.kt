package com.deval.event.fragment.regis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.deval.event.Models.Peserta
import com.deval.event.R
import com.deval.event.Retrofit.ApiFactory
import com.deval.event.Util.CheckItem
import com.deval.event.fragment.detail.DetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_regis.*

class RegisFragment : Fragment() {

    companion object {
        val ID = "ID"
    }

    private lateinit var id: String
    private var listUnit  = ArrayList<String?>()
    private val restForeground by lazy { ApiFactory.create(false) }
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_regis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = arguments?.getString(ID).toString()

        getUnit()
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listUnit)
        et_regis_unit.setAdapter(adapter)
        et_regis_unit.setText("", false)

        btn_regis_regis.setOnClickListener {
            val nama = et_regis_nama.text.toString()
            val hp = et_regis_hp.text.toString()
            val unit = et_regis_unit.text.toString()
            val idUnit = CheckItem().valueToID(unit)

            postPeserta(nama, hp, idUnit)
        }
    }

    fun getUnit(){
        disposable = restForeground
            .getUnit()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                listUnit.clear()
                it.data?.forEach { data ->
                    listUnit.add(data.nama)
                }
            },{
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            })
    }

    fun postPeserta(nama: String, hp: String, unit: String) {
        disposable = restForeground
            .postPeserta(id, nama, hp, unit)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessPeserta(it)
            }, {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            })
    }

    fun onSuccessPeserta(peserta: Peserta) {
        val bundle = Bundle()
        bundle.putString(DetailFragment.ID, id)
        bundle.putString(DetailFragment.ID_NAMA, peserta.id)

        (activity as AppCompatActivity).findNavController(R.id.nav_host_fragment_container)
            .navigate(R.id.action_regisFragment_to_detailFragment, bundle)
    }

}