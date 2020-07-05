package com.github.xiavic.essentials.Utils;

public interface Lockable {

    /**
     * Check whether this object is locked.
     * @return Returns whether this object is locked.
     */
    boolean isLocked();

    /**
     * Get the thread object which is locking this thread.
     * @return Returns the thread which has locked this object.
     */
    Thread getLocker();

    /**
     * Blocking method to try and lock the given object.
     */
    void lock();

    /**
     * Method to unlock the given object. Can only be called by the
     * {@link Thread} which originally locked this object.
     */
    void unlock();

}
