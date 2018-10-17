package nl.tudelft.jpacman.ui

import java.awt.event.ActionEvent
import java.util
import java.util.Map

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel


/**
  * A panel containing a button for every registered action.
  *
  * @author Jeroen Roosen 
  */
@SerialVersionUID(1L)
class ButtonPanel private[ui](val buttons: util.Map[String, Action],  val myParent: JFrame)

/**
  * Create a new button panel with a button for every action.
  *
  * @param buttons The map of caption - action for each button.
  * @param myParent  The parent frame, used to return window focus.
  */
  extends JPanel {
  

  import scala.collection.JavaConversions._

  for (caption <- buttons.keySet) {
    val button = new JButton(caption)
    button.addActionListener((e: ActionEvent) => {
      def foo(e: ActionEvent) = {
        buttons.get(caption)()
        myParent.requestFocusInWindow
      }

      foo(e)
    })
    add(button)
  }
}
