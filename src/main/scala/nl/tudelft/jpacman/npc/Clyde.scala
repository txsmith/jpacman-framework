package nl.tudelft.jpacman.npc

import nl.tudelft.jpacman.board.Direction
import nl.tudelft.jpacman.board.Square
import nl.tudelft.jpacman.board.JpacmanUnit
import nl.tudelft.jpacman.level.Player
import nl.tudelft.jpacman.npc.Ghost
import java.util
import nl.tudelft.jpacman.sprite.Sprite

/**
  * <p>
  * An implementation of the classic Pac-Man ghost Clyde.
  * </p>
  * <p>
  * Pokey needs a new nickname because out of all the ghosts,
  * Clyde is the least likely to "C'lyde" with Pac-Man. Clyde is always the last
  * ghost out of the regenerator, and the loner of the gang, usually off doing
  * his own thing when not patrolling the bottom-left corner of the maze. His
  * behavior is very random, so while he's not likely to be following you in hot
  * pursuit with the other ghosts, he is a little less predictable, and still a
  * danger.
  * </p>
  * <p>
  * <b>AI:</b> Clyde has two basic AIs, one for when he's far from Pac-Man, and
  * one for when he is near to Pac-Man.
  * When Clyde is far away from Pac-Man (beyond eight grid spaces),
  * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
  * location. However, when Clyde gets within eight grid spaces of Pac-Man, he
  * automatically changes his behavior and runs away.
  * </p>
  * <p>
  * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
  * </p>
  *
  * @author Jeroen Roosen
  */
object Clyde {
  /**
    * The amount of cells Clyde wants to stay away from Pac Man.
    */
    private val SHYNESS = 8
  /**
    * The variation in intervals, this makes the ghosts look more dynamic and
    * less predictable.
    */
  private val INTERVAL_VARIATION = 50
  /**
    * The base movement interval.
    */
  private val MOVE_INTERVAL = 250
  /**
    * A map of opposite directions.
    */
  private val OPPOSITES = new util.HashMap[Direction, Direction]
  try OPPOSITES.put(Direction.NORTH, Direction.SOUTH)
  OPPOSITES.put(Direction.SOUTH, Direction.NORTH)
  OPPOSITES.put(Direction.WEST, Direction.EAST)
  OPPOSITES.put(Direction.EAST, Direction.WEST)
}

class Clyde(val spriteMap: util.Map[Direction, Sprite])

/**
  * Creates a new "Clyde", a.k.a. "Pokey".
  *
  * @param spriteMap The sprites for this ghost.
  */
  extends Ghost(spriteMap, Clyde.MOVE_INTERVAL, Clyde.INTERVAL_VARIATION) {
  /**
    * {@inheritDoc }
    *
    * <p>
    * Clyde has two basic AIs, one for when he's far from Pac-Man, and one for
    * when he is near to Pac-Man.
    * When Clyde is far away from Pac-Man (beyond eight grid spaces),
    * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
    * location. However, when Clyde gets within eight grid spaces of Pac-Man,
    * he automatically changes his behavior and runs away
    * </p>
    */
  override def nextAiMove: Option[Direction] = {
    assert(hasSquare)
    val nearest: JpacmanUnit = Navigation.findNearest(classOf[Player], getSquare)
    if (nearest == null) return Option.empty
    assert(nearest.hasSquare)
    val target: Square = nearest.getSquare
    val path: util.List[Direction] = Navigation.shortestPath(getSquare, target, this)
    if (path != null && !path.isEmpty) {
      val direction: Direction = path.get(0)
      if (path.size <= Clyde.SHYNESS) return Option(Clyde.OPPOSITES.get(direction))
      return Option(direction)
    }
    Option.empty
  }
}