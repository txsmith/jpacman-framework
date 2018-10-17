package nl.tudelft.jpacman.ui

import java.awt.{BorderLayout, Container}
import java.util
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import javax.swing.{JFrame, WindowConstants}
import nl.tudelft.jpacman.game.Game


/**
  * The default JPacMan UI frame. The PacManUI consists of the following
  * elements:
  *
  * <ul>
  * <li>A score panel at the top, displaying the score of the player(s).
  * <li>A board panel, displaying the current level, i.e. the board and all units
  * on it.
  * <li>A button panel, containing all buttons provided upon creation.
  * </ul>
  *
  * @author Jeroen Roosen
  *
  */
@SerialVersionUID(1L)
object PacManUI {
  /** class Blah {
    * *
    * }
    *
    * The desired frame rate interval for the graphics in milliseconds, 40
    * being 25 fps.
    */
  private val FRAME_INTERVAL = 40
}
/**
  * Creates a new UI for a JPac-Man game.
  *
  * @param game
  * The game to play.
  * @param buttons
  * The map of caption-to-action entries that will appear as
  * buttons on the interface.
  * @param keyMappings
  * The map of keyCode-to-action entries that will be added as key
  * listeners to the interface.
  * @param scoreFormatter
  * The formatter used to display the current score.
  */
@SerialVersionUID(1L)
class PacManUI( var game: Game,
                var buttons: util.Map[String, Action],
                var keyMappings: util.Map[Integer, Action],
                var scoreFormatter: ScorePanel.ScoreFormatter) extends JFrame("JPac-Man") {

  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  val keys = new PacKeyListener(keyMappings)
  addKeyListener(keys)
  val buttonPanel = new ButtonPanel(buttons, this)
  scorePanel = new ScorePanel(game.getPlayers);
  if (scoreFormatter != null) scorePanel.setScoreFormatter(scoreFormatter)
  boardPanel = new BoardPanel(game)
  val contentPanel: Container = getContentPane
  contentPanel.setLayout(new BorderLayout)
  contentPanel.add(buttonPanel, BorderLayout.SOUTH)
  contentPanel.add(scorePanel, BorderLayout.NORTH)
  contentPanel.add(boardPanel, BorderLayout.CENTER)
  pack()
  /**
    * The panel displaying the player scores.
    */
  final private var scorePanel: ScorePanel = null
  /**
    * The panel displaying the game.
    */
  final private var boardPanel: BoardPanel= null

  /**
    * Starts the "engine", the thread that redraws the interface at set
    * intervals.
    */
  def start(): Unit = {
    setVisible(true)
    val service: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor
    service.scheduleAtFixedRate(this.nextFrame, 0, PacManUI.FRAME_INTERVAL, TimeUnit.MILLISECONDS)
  }

  /**
    * Draws the next frame, i.e. refreshes the scores and game.
    */
  private def nextFrame: Runnable = () => {
    boardPanel.repaint()
    scorePanel.refresh()
  }
}
