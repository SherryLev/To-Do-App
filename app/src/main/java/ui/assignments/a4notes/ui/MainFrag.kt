package ui.assignments.a4notes.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import ui.assignments.a4notes.R
import ui.assignments.a4notes.viewmodel.NotesViewModel


class MainFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        // Check if create note button was clicked and move to add page if so
        val button = root.findViewById<Button>(R.id.createNote)
        button.setOnClickListener {
            Log.i("MainFrag", "Button Clicekd")
            findNavController().navigate(R.id.action_mainFrag_to_createFragment)

        }

        val myVM : NotesViewModel by activityViewModels()
        var mode = true

        // Check if archived button was switched and display notes accordingly
        val switch = root.findViewById<SwitchCompat>(R.id.checkArchieve)
        switch.setOnCheckedChangeListener { _, state ->
            myVM.viewArchived.value = state
            Log.i("MainFrag", "Button Clicked $state")
        }

        // Check if dark mode was selected
        val darkModeButton : Button = root.findViewById<Button>(R.id.darkModeButton)
        darkModeButton.setOnClickListener{
            myVM.model.currentMode = false
            view?.findViewById<LinearLayout>(R.id.container)?.setBackgroundColor(Color.BLACK)
            view?.findViewById<ScrollView>(R.id.mainScroll)?.setBackgroundColor(Color.BLACK)

        }


        // Check if light mode was selected
        val lightModeButton : Button = root.findViewById<Button>(R.id.lightModeButton)
        lightModeButton.setOnClickListener{
            myVM.model.currentMode = true
            view?.findViewById<LinearLayout>(R.id.container)?.setBackgroundColor(Color.WHITE)
            view?.findViewById<ScrollView>(R.id.mainScroll)?.setBackgroundColor(Color.WHITE)
        }

        // Loop through notes
        myVM.getNotes().observe(viewLifecycleOwner) {con->
            if(myVM.model.currentMode  == false){
                view?.findViewById<LinearLayout>(R.id.container)?.setBackgroundColor(Color.BLACK)
                view?.findViewById<ScrollView>(R.id.mainScroll)?.setBackgroundColor(Color.BLACK)
            }else{
                view?.findViewById<LinearLayout>(R.id.container)?.setBackgroundColor(Color.WHITE)
                view?.findViewById<ScrollView>(R.id.mainScroll)?.setBackgroundColor(Color.WHITE)
            }
            val totalNotes = root.findViewById<TextView>(R.id.valueTotal)
            var textDisplay = " " + con.size.toString()
            totalNotes.setText(textDisplay)


            val currentLayout = view?.findViewById<LinearLayout>(R.id.container)
            currentLayout?.removeAllViews()

            con.forEach {
                // Create individual notes using stored information
                layoutInflater.inflate(R.layout.view,null,false).apply {

                    //Assign note attributes based on the information stored
                    findViewById<TextView>(R.id.viewTitle)?.text = it.value?.title
                    findViewById<TextView>(R.id.viewContent)?.text = it.value?.content

                    val noteButton: Button = findViewById(R.id.note)
                    if(it.value?.important == true){
                        // If dark mode set note to be yellow, otherwise cyan
                        if(!myVM.model.currentMode){
                            Log.i("MainFrag", "dark mode button -> yellow")
                            noteButton.setBackgroundColor(Color.YELLOW)
                        }else {
                            noteButton.setBackgroundColor(Color.CYAN)
                        }
                        findViewById<Button>(R.id.deleteButton).isClickable = false
                    }
                    if(it.value?.archived == true){
                        noteButton.setBackgroundColor(Color.LTGRAY)
                    }


                    // Check if the note's archive button was pressed and archive it accordingly
                    findViewById<Button>(R.id.archiveButton)?.setOnClickListener{but->
                        Log.i("MainFrag", "button was archived")
                        myVM.model.updateNoteArchived(it.value?.id!!.toInt(), true)

                    }

                    // Check if the note's delete button was pressed and delete it accordignly
                    findViewById<Button>(R.id.deleteButton)?.setOnClickListener{butDel->
                        Log.i("MainFrag", "button was deleted")
                        myVM.model.removeNote(it.value?.id!!.toInt())

                    }
                    // assign current note's id value
                    val current = it.value!!.id

                    //Check if note was pressed and navigate to edit screen
                    noteButton.setOnClickListener {select ->
                        // assign the model's current ID selected to current ID
                        myVM.model.currentIdSelected = current

                        // navigate to edit screen
                        findNavController().navigate(R.id.action_mainFrag_to_editFragment)
                    }

                    currentLayout?.addView(this)
                }

            }
        }

        return root

    }

}