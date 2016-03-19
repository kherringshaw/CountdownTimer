package edu.luc.etl.cs313.scala.stopwatch
package model

import common.{StopwatchUIUpdateListener, StopwatchUIListener, Constants}
import edu.luc.etl.cs313.scala.stopwatch.ui.{R}
import time.TimeModel
import clock.{ClockModel, OnTickListener}


/** Contains the components of the dynamic state machine model. */
object state {

  trait Initializable { def actionInit(): Unit }

  /**
   * The state machine for the state-based dynamic model of the stopwatch.
   * This interface is part of the State pattern.
   */
  trait StopwatchStateMachine extends StopwatchUIListener with OnTickListener with Initializable {
    def getState(): StopwatchState
    def actionUpdateView(): Unit
  }

  /** A state in a state machine. This interface is part of the State pattern. */
  trait StopwatchState extends StopwatchUIListener with OnTickListener {
    def updateView(): Unit
    def getId(): Int
  }

  /** An implementation of the state machine for the stopwatch. */
  class DefaultStopwatchStateMachine(
                                      timeModel: TimeModel,
                                      clockModel: ClockModel,
                                      uiUpdateListener: StopwatchUIUpdateListener
                                      ) extends StopwatchStateMachine with Serializable {

    /** The current internal state of this adapter component. Part of the State pattern. */
    private var state: StopwatchState = _

    protected def setState(state: StopwatchState): Unit = {
      this.state = state
      uiUpdateListener.updateState(state.getId)
    }

    def getState(): StopwatchState = state

    // forward event uiUpdateListener methods to the current state
    override def onStartStop(): Unit = state.onStartStop()
    override def onPopulate(number:Int): Unit = state.onPopulate(number)
    override def onTick(): Unit      = state.onTick()

    def updateUIRuntime(): Unit = uiUpdateListener.updateTime(timeModel.getRuntime)

    // transitions
    def toRunningState(): Unit    =  setState(RUNNING)
    def toBeepingState(): Unit    =  setState(BEEPING)
    def toStoppedState(): Unit    =  setState(STOPPED)

    // actions
    override def actionInit(): Unit       = { toStoppedState() ; actionReset() }
    override def actionUpdateView(): Unit = state.updateView()
    def actionReset(): Unit          = { timeModel.resetRuntime() ; actionUpdateView() }
    def actionStart(): Unit          = { clockModel.start() }
    def actionStop(): Unit           = { clockModel.stop() }
    def actionInc(): Unit            = { timeModel.incRuntime(); actionUpdateView() }

    //method to begin decrementation
    def actionDec(): Unit            = { timeModel.decRuntime()
      if (timeModel.getRuntime()==Constants.TIMER_START)
      { actionStop()
        actionStart()
        toBeepingState()
      }
        actionUpdateView()
    }

    //method to check if upperbound of 99 has been reached
    def actionUpperBound(): Unit = {
      if (timeModel.getRuntime()==Constants.TIMER){
        actionStart()
        actionTimeOut()
        }
      else{
        actionStart()
        toStoppedState()}
    }
    //method to start running if 3 seconds has passed since last
    //button press
    def actionTimeOut(): Unit = {
      if (timeModel.getCount()==Constants.TIMEOUT){
        uiUpdateListener.playSound()
        toRunningState()
      }
      timeModel.incCount()
    }

    // known states

    private val STOPPED = new StopwatchState {
      override def onStartStop() = {  actionInc(); timeModel.resetCount(); actionStop(); actionUpperBound(); }
      override def onPopulate(number:Int) = {timeModel.setRuntime(number);updateView(); timeModel.resetCount(); actionStop(); actionUpperBound()}
      override def onTick()      = { actionTimeOut()   }
      override def updateView()  = updateUIRuntime()
      override def getId()       = R.string.STOPPED
    }

    private val RUNNING = new StopwatchState {
      override def onStartStop() = {actionStop();  actionReset();  toStoppedState() }
      override def onPopulate(number:Int) = {}
      override def onTick()      = { actionDec()  }
      override def updateView()  = updateUIRuntime()
      override def getId()       = R.string.RUNNING
    }

    private val BEEPING = new StopwatchState {
      override def onStartStop() = { actionStop(); toStoppedState(); actionReset() }
      override def onPopulate(number:Int) = {}
      override def onTick()      = { uiUpdateListener.playSound() }
      override def updateView()  = updateUIRuntime()
      override def getId()       = R.string.BEEPING
    }
  }
}