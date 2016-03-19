package edu.luc.etl.cs313.scala.stopwatch
package model

import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import ui.R
import common.{StopwatchUIUpdateListener, Constants}
import clock.ClockModel
import state.StopwatchStateMachine
import time.TimeModel

/**
 * An abstract unit test for the state machine abstraction.
 * This is a unit test of an object with multiple dependencies;
 * we use a unified mock object to satisfy all dependencies
 * and verify that the state machine behaved as expected.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractStateModelSpecs extends JUnitSuite {

  /** Creates an instance of the home-grown unified mock dependency. */
  def fixtureDependency() = new UnifiedMockDependency

  /** Creates an instance of the SUT. */
  def fixtureSUT(dependency: UnifiedMockDependency): StopwatchStateMachine

  /**
   * Verifies that we're initially in the stopped state (and told the listener
   * about it).
   */
  @Test def testPreconditions() = {
    val dependency = fixtureDependency()
    val model = fixtureSUT(dependency)
    model.actionInit()
    assertEquals(R.string.STOPPED, dependency.getState)
  }

   /**
   * Verifies the following scenario: time is 0, press +/cancel, time increments +1, 5 times,
   * expect time 5.
   */
   @Test def testScenarioInc(): Unit = {
     val dependency = fixtureDependency()
     val model = fixtureSUT(dependency)
     model.actionInit()
     assertEquals(0, dependency.getTime)
     // directly invoke the button press event handler methods
     model.onStartStop()
     assertEquals(1, dependency.getTime)
     model.onStartStop()
     assertEquals(2, dependency.getTime)
     model.onStartStop()
     assertEquals(3, dependency.getTime)
     model.onStartStop()
     assertEquals(4, dependency.getTime)
     model.onStartStop()
     assertEquals(5, dependency.getTime)
   }

}

/**
 * Manually implemented mock object that unifies the three dependencies of the
 * stopwatch state machine model. The three dependencies correspond to the three
 * interfaces this mock object implements.
 */
class UnifiedMockDependency extends TimeModel with ClockModel with StopwatchUIUpdateListener {
  private var timeValue = 5//-1
  private var stateId = -1
  private var runningTime = 0
  private var started = false
  private var count = 0

  def getTime(): Int = timeValue
  def getState(): Int = stateId
  def isStarted(): Boolean = started
  def getCount(): Int = count


  override def updateTime(timeValue: Int): Unit = this.timeValue = timeValue
  override def updateState(stateId: Int): Unit = this.stateId = stateId
  override def playSound()  : Unit = this.started = started
  override def start(): Unit = started = true
  override def stop(): Unit = started = false
  override def resetRuntime(): Unit = runningTime = 0
  override def incRuntime(): Unit = runningTime += 1
  override def decRuntime(): Unit = runningTime -= 1
  override def getRuntime(): Int = runningTime
  override def incCount(): Unit = { count += 1}
  override def resetCount(): Unit = count = 0
  override def setRuntime(value: Int): Unit = ()
  override def setCount(value: Int): Unit = ()
}
