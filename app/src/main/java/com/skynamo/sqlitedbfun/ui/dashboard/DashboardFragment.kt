package com.skynamo.sqlitedbfun.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.skynamo.sqlitedbfun.R
import com.skynamo.sqlitedbfun.databinding.FragmentDashboardBinding
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterFragment.CachedEngineFragmentBuilder
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

private const val ENGINE_ID = "engine_ID"

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

    _binding = FragmentDashboardBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textDashboard
    dashboardViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }
    return root
  }

    companion object {
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
    }

    override fun onStart() {
        super.onStart()
        setupEngine()

        val fragmentManager: FragmentManager = childFragmentManager

        // Attempt to find an existing FlutterFragment, in case this is not the
        // first time that onCreate() was run.
        var flutterFragment = fragmentManager
            .findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?

        // Create and attach a FlutterFragment if one does not exist.
        if (flutterFragment == null) {

            var flutterFragment
            = CachedEngineFragmentBuilder(FlutterFragment::class.java, ENGINE_ID)
                .build<FlutterFragment>()

            fragmentManager
                .beginTransaction()
                .add(
                    R.id.flutter_host,
                    flutterFragment,
                    TAG_FLUTTER_FRAGMENT
                )
                .commit()
        }
    }

    private fun setupEngine() {
        var cachedEngine = FlutterEngineCache
            .getInstance().get(ENGINE_ID)
        if (cachedEngine == null) {
            cachedEngine = FlutterEngine(requireContext())

            // Start executing Dart code to pre-warm the FlutterEngine.
            cachedEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            FlutterEngineCache
                .getInstance().put(ENGINE_ID, cachedEngine)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}