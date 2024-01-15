package ui.assignments.a4notes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ui.assignments.a4notes.R
import ui.assignments.a4notes.viewmodel.NotesViewModel


class CreateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_create, container, false)

        // Check if create button was selected
        root.findViewById<Button>(R.id.createB).setOnClickListener {

            val myVM : NotesViewModel by activityViewModels { NotesViewModel.Factory}

            // Add note based on user's title, content and chosen importance
            myVM.model.addNote(root.findViewById<EditText>(R.id.createTitle).text.toString(),
                view?.findViewById<TextView>(R.id.createContent)?.text.toString(),
                root.findViewById<Switch>(R.id.createImportant).isChecked)

            // Return to main screen
            findNavController().navigate(R.id.action_createFragment_to_mainFrag)

        }
        return root
    }

}