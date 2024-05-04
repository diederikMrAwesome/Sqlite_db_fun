package com.skynamo.sqlitedbfun.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.j256.ormlite.misc.TransactionManager
import com.j256.ormlite.stmt.UpdateBuilder
import com.skynamo.sqlitedbfun.data.DataItem
import com.skynamo.sqlitedbfun.data.DataItemGenerator
import com.skynamo.sqlitedbfun.data.dao.DataItemDao
import com.skynamo.sqlitedbfun.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException
import kotlin.random.Random

class HomeFragment : Fragment() {

    companion object {
        private var _dao: DataItemDao? = null
    }
    // Static? Singleton?
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

        _binding?.button?.setOnClickListener {
            insertRandomUserData()
        }

    }

    private fun insertRandomUserData() {
        if (_dao == null) {
            _dao = DataItemDao(this.requireContext())
        }

        // Launch a coroutine in the background
        GlobalScope.launch(Dispatchers.IO) {

            val generator = DataItemGenerator()

            repeat(3) {
                repeat(1000) {
                    val randomDataItem = generator.generateRandomUser()
                    // Insert the random user data item into the database
                    _dao?.createDataItem(randomDataItem)
                }

                try {
                    TransactionManager.callInTransaction(_dao?.databaseHelper?.connectionSource) {
                        val updateBuilder: UpdateBuilder<DataItem, Int>? = _dao?.updateBuilder()
                        updateBuilder?.updateColumnValue("age", 20)
                        updateBuilder?.where()?.lt("age", 20)
                        updateBuilder?.update()

                        updateBuilder?.reset()
                        updateBuilder?.updateColumnValue("isActive", Random.nextBoolean())
                        updateBuilder?.update()
                    }
                    // Show a toast message on the main/UI thread
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Random data inserted", Toast.LENGTH_SHORT).show()
                    }
                }  catch (e: SQLException) {
                    e.printStackTrace() // Handle the exception appropriately
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}