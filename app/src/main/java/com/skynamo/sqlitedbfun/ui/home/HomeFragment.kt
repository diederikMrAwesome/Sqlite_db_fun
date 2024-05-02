package com.skynamo.sqlitedbfun.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skynamo.sqlitedbfun.data.DataItemGenerator
import com.skynamo.sqlitedbfun.data.dao.DataItemDao
import com.skynamo.sqlitedbfun.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var dao: DataItemDao
    private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textHome
    homeViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

    override fun onStart() {
        super.onStart()

        dao = DataItemDao(this.requireContext())
        val generator = DataItemGenerator()
        dao.createDataItem(generator.generateRandomUser())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dao.close()
    }
}