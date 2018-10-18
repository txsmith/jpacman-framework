package nl.tudelft.jpacman.board

import nl.tudelft.jpacman.sprite.PacManSprites
import nl.tudelft.jpacman.sprite.Sprite


/**
  * A factory that creates {@link Board} objects from 2-dimensional arrays of
  * {@link Square}s.
  *
  * @author Jeroen Roosen
  */
object BoardFactory {

  /**
    * A wall is a square that is inaccessible to anyone.
    *
    * @author Jeroen Roosen
    */
  final class Wall(val background: Sprite)

  /**
    * Creates a new wall square.
    *
    * @param sprite
    * The background for the square.
    */
    extends Square {
    override def isAccessibleTo(unit: JpacmanUnit) = false

    override def getSprite: Sprite = background
  }

  /**
    * A ground square is a square that is accessible to anyone.
    *
    * @author Jeroen Roosen
    */
  final class Ground(val background: Sprite)

  /**
    * Creates a new ground square.
    *
    * @param sprite
    * The background for the square.
    */
    extends Square {
    override def isAccessibleTo(unit: JpacmanUnit) = true

    override def getSprite: Sprite = background
  }

}

class BoardFactory(val sprites: PacManSprites) {
  /**
    * Creates a new board from a grid of cells and connects it.
    *
    * @param grid
    * The square grid of cells, in which grid[x][y] corresponds to
    * the square at position x,y.
    * @return A new board, wrapping a grid of connected cells.
    */
  def createBoard(grid: List[List[Square]]): Board = {
    assert(grid != null)
    val board = new Board(grid)
    val width = board.getWidth
    val height = board.getHeight
    var x = 0
    while ( {
      x < width
    }) {
      var y = 0
      while ( {
        y < height
      }) {
        val square = grid(x)(y)
        for (dir <- Direction.values) {
          val dirX = (width + x + dir.getDeltaX) % width
          val dirY = (height + y + dir.getDeltaY) % height
          val neighbour = grid(dirX)(dirY)
          square.link(neighbour, dir)
        }

        {
          y += 1;
          y - 1
        }
      }

      {
        x += 1;
        x - 1
      }
    }
    board
  }

  /**
    * Creates a new square that can be occupied by any unit.
    *
    * @return A new square that can be occupied by any unit.
    */
  def createGround = new BoardFactory.Ground(sprites.getGroundSprite)

  /**
    * Creates a new square that cannot be occupied by any unit.
    *
    * @return A new square that cannot be occupied by any unit.
    */
  def createWall = new BoardFactory.Wall(sprites.getWallSprite)
}
