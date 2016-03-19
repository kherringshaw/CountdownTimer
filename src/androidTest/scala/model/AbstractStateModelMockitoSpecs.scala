package edu.luc.etl.cs313.scala.stopwatch
package model

import org.junit.Assert._
import org.junit.Test
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar
import ui.R
import edu.luc.etl.cs313.scala.stopwatch.common.{Constants, StopwatchUIUpdateListener}
import clock.ClockModel
import state.StopwatchStateMachine
import time.TimeModel
import state.DefaultStopwatchStateMachine

/**
 * An abstract unit test for the state machine abstraction.
 * This is a unit test of an object with multiple dependencies;
 * we use a unified mock object to satisfy all dependencies
 * and verify that the state machine behaved as expected.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractStateModelMockitoSpecs extends JUnitSuite with MockitoSugar {

  trait Dependencies {
    val timeModel: TimeModel
    val clockModel: ClockModel
    val uiUpdateListener: StopwatchUIUpdateListener
  }

  /** Creates an instance of the SUT. */
  def fixtureSUT(dependencies: Dependencies): StopwatchStateMachine

  /** Creates instances of the dependencies. */
  def fixtureDependency() = new Dependencies {
    override val timeModel = mock[TimeModel]
    override val clockModel = mock[ClockModel]
    override val uiUpdateListener = mock[StopwatchUIUpdateListener]
  }

  /**
   * Verifies that we're initially in the stopped state (and told the listener
   * about it).
   */
  @Test def testPreconditions() = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    model.actionInit()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED)
    verify(dep.uiUpdateListener).updateTime(0)
    verifyNoMoreInteractions(dep.uiUpdateListener, dep.clockModel)
  }

  /**
   * Verifies the following scenario: time is 0, press +/cancel 5 times,
   * expect time 5.
   */


  @Test def testScenarioInc(): Unit = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    val t = 5
    model.actionInit()
    verify(dep.clockModel, never).start()
    verify(dep.timeModel, never).incRuntime()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED)
    // directly invoke the button press event handler methods
    (1 to t) foreach { _ => model.onStartStop()}
    verify(dep.timeModel, times(t)).incRuntime()
     verify(dep.uiUpdateListener, times(t+1)).updateTime(anyInt())
  }
  /**
   * Verifies the following scenario: time is incremented, time goes to 0,
   * expect state to BEEPING.
   */

  @Test def testScenarioDec(): Unit = {
    val dep = fixtureDependency()
    val model = fixtureSUT(dep)
    model.actionInit()
    verify(dep.clockModel, never).start()
    verify(dep.uiUpdateListener).updateState(R.string.STOPPED)
    model.onStartStop()
    verify(dep.timeModel,times(1)).incRuntime()
    dep.timeModel.setCount(3)
    dep.uiUpdateListener.updateState(R.string.BEEPING)
    verify(dep.uiUpdateListener).updateState(R.string.BEEPING)

  }


}