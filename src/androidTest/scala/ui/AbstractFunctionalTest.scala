package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.widget.{Button, TextView, EditText}
import org.junit.Assert._
import org.junit.Test
import common.Constants.TIMER

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractFunctionalTest {

  /** The activity to be provided by concrete subclasses of this test. */
  protected def activity(): MainActivity

  @Test def testActivityExists(): Unit = assertNotNull(activity)

  @Test def testActivityInitialValue(): Unit = assertEquals(0, getDisplayedValue())

  /**
   * Verifies the following scenario: time is 5, press start, wait 5+ seconds, expect time 0.
   *
   * @throws Throwable
   */
  @Test def testActivityScenarioDec(): Unit = {
    activity.runOnUiThread {
      assertEquals(5, setDisplayedValue)
      assertTrue(getStartStopButton().performClick())
    }
    Thread.sleep(5500) // <-- do not run this in the UI thread!
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(0, getDisplayedValue)
      assertTrue(getStartStopButton.performClick())
    }

  }


  // auxiliary methods for easy access to UI widgets

  protected def tvToInt(t: TextView): Int = t.getText.toString.trim.toInt

  def getDisplayedValue(): Int = {
    val ts = activity.findView(TR.seconds)
    tvToInt(ts)
  }

  protected def tvInt(i: Int): Int = i.toInt

  def setDisplayedValue(): Int = {
    tvInt(5)
  }

  protected def getEditTextValue(e: Int): Int = e.toInt

    def setEditTextValue(): Int = {
    getEditTextValue(5)
    }


  protected def getStartStopButton(): Button = activity.findView(TR.startStop)

  protected def getPopulateButton(): Button = activity.findView(TR.populate)

  protected def getPopulatButton(): Button = activity().findView(TR.populate)

  protected def getPopulateEditText() : EditText = activity.findView(TR.typedDigits)

  /**
   * Explicitly runs tasks scheduled to run on the UI thread in case this is required
   * by the testing framework, e.g., Robolectric. When this is not required,
   * it should be overridden with an empty method.
   */
  protected def runUiThreadTasks(): Unit
}
