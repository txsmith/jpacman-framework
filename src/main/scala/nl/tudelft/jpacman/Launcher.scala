package nl.tudelft.jpacman

import java.awt.event.KeyEvent
import java.io.IOException
import java.util
import java.util.List

import nl.tudelft.jpacman.board.BoardFactory
import nl.tudelft.jpacman.board.Direction
import nl.tudelft.jpacman.game.Game
import nl.tudelft.jpacman.game.GameFactory
import nl.tudelft.jpacman.level.Level
import nl.tudelft.jpacman.level.LevelFactory
import nl.tudelft.jpacman.level.MapParser
import nl.tudelft.jpacman.level.Player
import nl.tudelft.jpacman.level.PlayerFactory
import nl.tudelft.jpacman.npc.ghost.GhostFactory
import nl.tudelft.jpacman.sprite.PacManSprites

import nl.tudelft.jpacman.ui.PacManUI
import nl.tudelft.jpacman.ui.PacManUiBuilder


/**
  * Creates and launches the JPacMan UI.
  *
  * @author Jeroen Roosen
  */
@SuppressWarnings(Array("PMD.TooManyMethods")) object Launcher {
  private val SPRITE_STORE = new PacManSprites
  val DEFAULT_MAP = "/board.txt"

  /**
    * Main execution method for the Launcher.
    *
    * @param args
    * The command line arguments - which are ignored.
    * @throws IOException
    * When a resource could not be read.
    */
  @throws[IOException]
  def main(args: Array[String]): Unit = {
    new Launcher().launch()
  }
}

@SuppressWarnings(Array("PMD.TooManyMethods")) class Launcher {
  private var levelMap: String = Launcher.DEFAULT_MAP
  private var pacManUI: PacManUI = null
  private var game: Game = null

  /**
    * @return The game object this launcher will start when { @link #launch()}
    *                                                               is called.
    */
  def getGame: Game = game

  /**
    * The map file used to populate the level.
    *
    * @return The name of the map file.
    */
  protected def getLevelMap: String = levelMap

  /**
    * Set the name of the file containing this level's map.
    *
    * @param fileName
    * Map to be used.
    * @return Level corresponding to the given map.
    */
  def withMapFile(fileName: String): Launcher = {
    levelMap = fileName
    this
  }

  /**
    * Creates a new game using the level from {@link #makeLevel()}.
    *
    * @return a new Game.
    */
  def makeGame: Game = {
    val gf = getGameFactory
    val level = makeLevel
    game = gf.createSinglePlayerGame(level)
    game
  }

  /**
    * Creates a new level. By default this method will use the map parser to
    * parse the default board stored in the <code>board.txt</code> resource.
    *
    * @return A new level.
    */
  def makeLevel: Level = try
    getMapParser.parseMap(getLevelMap)
  catch {
    case e: IOException =>
      throw new PacmanConfigurationException("Unable to create level, name = " + getLevelMap, e)
  }

  /**
    * @return A new map parser object using the factories from
    *         { @link #getLevelFactory()} and { @link #getBoardFactory()}.
    */
  protected def getMapParser = new MapParser(getLevelFactory, getBoardFactory)

  /**
    * @return A new board factory using the sprite store from
    *         { @link #getSpriteStore()}.
    */
  protected def getBoardFactory = new BoardFactory(getSpriteStore)

  /**
    * @return The default { @link PacManSprites}.
    */
  protected def getSpriteStore: PacManSprites = Launcher.SPRITE_STORE

  /**
    * @return A new factory using the sprites from { @link #getSpriteStore()}
    *                                                      and the ghosts from { @link #getGhostFactory()}.
    */
  protected def getLevelFactory = new LevelFactory(getSpriteStore, getGhostFactory)

  /**
    * @return A new factory using the sprites from { @link #getSpriteStore()}.
    */
  protected def getGhostFactory = new GhostFactory(getSpriteStore)

  /**
    * @return A new factory using the players from { @link #getPlayerFactory()}.
    */
  protected def getGameFactory = new GameFactory(getPlayerFactory)

  protected def getPlayerFactory = new PlayerFactory(getSpriteStore)

  /**
    * Adds key events UP, DOWN, LEFT and RIGHT to a game.
    *
    * @param builder
    * The { @link PacManUiBuilder} that will provide the UI.
    */
  protected def addSinglePlayerKeys(builder: PacManUiBuilder): Unit = {
    builder.addKey(KeyEvent.VK_UP, moveTowardsDirection(Direction.NORTH)).addKey(KeyEvent.VK_DOWN, moveTowardsDirection(Direction.SOUTH)).addKey(KeyEvent.VK_LEFT, moveTowardsDirection(Direction.WEST)).addKey(KeyEvent.VK_RIGHT, moveTowardsDirection(Direction.EAST))
  }

  private def moveTowardsDirection(direction: Direction) = () => {
      getGame.move(getSinglePlayer(getGame), direction)
  }

  private def getSinglePlayer(game: Game) = {
    val players = game.getPlayers
    if (players.isEmpty) throw new IllegalArgumentException("Game has 0 players.")
    players.get(0)
  }

  /**
    * Creates and starts a JPac-Man game.
    */
  def launch(): Unit = {
    makeGame
    val builder = new PacManUiBuilder().withDefaultButtons
    addSinglePlayerKeys(builder)
    pacManUI = builder.build(getGame)
    pacManUI.start()
  }

  /**
    * Disposes of the UI. For more information see
    * {@link javax.swing.JFrame#dispose()}.
    *
    * Precondition: The game was launched first.
    */
  def dispose(): Unit = {
    assert(pacManUI != null)
    pacManUI.dispose()
  }
}
