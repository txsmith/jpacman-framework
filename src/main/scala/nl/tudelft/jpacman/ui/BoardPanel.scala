package nl.tudelft.jpacman.ui

import java.awt.{Color, Dimension, Graphics}

import javax.swing.JPanel
import nl.tudelft.jpacman.board.{Board, Square, JpacmanUnit}
import nl.tudelft.jpacman.game.Game


/**
  * Panel displaying a game.
  *
  * @author Jeroen Roosen 
  *
  */
@SerialVersionUID(1L)
object BoardPanel {
  /**
    * The background colour of the board.
    */
  val BACKGROUND_COLOR: Color = Color.BLACK
  /**
    * The size (in pixels) of a square on the board. The initial size of this
    * panel will scale to fit a board with square of this size.
    */
  val SQUARE_SIZE = 16
}

/**
  * Creates a new board panel that will display the provided game.
  *
  * @param game
  * The game to display.
  */
@SerialVersionUID(1L)
class BoardPanel(val game: Game) extends JPanel {
  val board: Board = game.getLevel.getBoard
  val w: Int = board.getWidth * BoardPanel.SQUARE_SIZE
  val h: Int = board.getHeight * BoardPanel.SQUARE_SIZE
  val panelSize = new Dimension(w, h)
  setMinimumSize(panelSize)
  setPreferredSize(panelSize)

  override def paint(g: Graphics): Unit = {
    assert(g != null)
    render(game.getLevel.getBoard, g, getSize)
  }

  /**
    * Renders the board on the given graphics context to the given dimensions.
    *
    * @param board
    * The board to render.
    * @param graphics
    * The graphics context to draw on.
    * @param window
    * The dimensions to scale the rendered board to.
    */
  private def render(board: Board, graphics: Graphics, window: Dimension):Unit = {
    val cellW = window.width / board.getWidth
    val cellH = window.height / board.getHeight
    graphics.setColor(BoardPanel.BACKGROUND_COLOR)
    graphics.fillRect(0, 0, window.width, window.height)
    var y = 0
    while ( {
      y < board.getHeight
    }) {
      var x = 0
      while ( {
        x < board.getWidth
      }) {
        val cellX = x * cellW
        val cellY = y * cellH
        val square = board.squareAt(x, y)
        this.render(square, graphics, cellX, cellY, cellW, cellH)

        {
          x += 1;
          x - 1
        }
      }

      {
        y += 1;
        y - 1
      }
    }
  }

  /**
    * Renders a single square on the given graphics context on the specified
    * rectangle.
    *
    * @param square
    * The square to render.
    * @param graphics
    * The graphics context to draw on.
    * @param x
    * The x position to start drawing.
    * @param y
    * The y position to start drawing.
    * @param width
    * The width of this square (in pixels.)
    * @param height
    * The height of this square (in pixels.)
    */
  private def render(square: Square, graphics: Graphics, x: Int, y: Int, width: Int, height: Int): Unit = {
    square.getSprite.draw(graphics, x, y, width, height)
    import scala.collection.JavaConversions._
    for (unit <- square.getOccupants) {
      unit.getSprite.draw(graphics, x, y, width, height)
    }
  }
}
