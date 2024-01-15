package ui.assignments.a4notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.*
import ui.assignments.a4notes.R
import ui.assignments.a4notes.viewmodel.NotesViewModel


class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        val myVM : NotesViewModel by activityViewModels { NotesViewModel.Factory}

        // Retrieve current note's ID and list of notes' size
        val idForUse = myVM.model.currentIdSelected
        val notesSize =myVM.model.notes.size -1

        for (i in 0..notesSize) {
            // Find current note in the list of notes and adjust edit screen based on given information
            if(myVM.model.notes[i].id == idForUse){
                root.findViewById<EditText>(R.id.editTitle).setText( myVM.model.notes[i].title)
                root.findViewById<EditText>(R.id.editContent).setText( myVM.model.notes[i].content)
                // if a note is important, remove archive switch
                if(myVM.model.notes[i].important) {
                    root.findViewById<Switch>(R.id.editArchived).isVisible = false
                }
                // if a note is archived, remove important switch
                if(myVM.model.notes[i].archived) {
                    root.findViewById<Switch>(R.id.editImportant).isVisible = false
                }
                root.findViewById<Switch>(R.id.editImportant).setChecked(myVM.model.notes[i].important)
                root.findViewById<Switch>(R.id.editArchived).setChecked(myVM.model.notes[i].archived)

            }

        }

        // Check if back button was pressed and update the note's attributes
        val button = root.findViewById<Button>(R.id.backButton)
        button.setOnClickListener {
            myVM.model.updateNoteTitle(idForUse, root.findViewById<EditText>(R.id.editTitle).text.toString())
            myVM.model.updateNoteContent(idForUse,root.findViewById<EditText>(R.id.editContent).text.toString())
            myVM.model.updateNoteArchived(idForUse,root.findViewById<Switch>(R.id.editArchived).isChecked)
            myVM.model.updateNoteImportant(idForUse,root.findViewById<Switch>(R.id.editImportant).isChecked)

            // Return to main screen
            findNavController().navigate(R.id.action_editFragment_to_mainFrag)
        }

        return root
    }

}