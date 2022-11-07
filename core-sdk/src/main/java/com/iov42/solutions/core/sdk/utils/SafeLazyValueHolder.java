package com.iov42.solutions.core.sdk.utils;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Wrapper class for a {@link StampedLock} to synchronize lazy fetching of a value.
 * <p>
 * All public methods of this class are threadsafe.
 *
 * @param <T> Type of the Value the instance holds.
 */
public class SafeLazyValueHolder <T> {

    private final StampedLock lock = new StampedLock();
    private final Supplier<T> producer;
    private final Predicate<T> validator;
    private T value;
    private boolean isValid;

    /**
     * Initialises a new object.
     *
     * @param producer  producer to fetch the value.
     * @param validator validator to check whether the value is still valid (if it is not valid a new value is fetched).
     */
    public SafeLazyValueHolder(Supplier<T> producer, Predicate<T> validator) {
        this.producer = producer;
        this.validator = validator;
    }

    /**
     * Returns the value (or lazily fetches a new Value if it was not yet fetched or if it is not valid anymore)
     *
     * @return the value.
     */
    public T getValue() {

        // 1. try to read the value in an optimistic way
        long stamp = lock.tryOptimisticRead();
        T localValue = this.value;
        boolean localIsValid = this.isValid;
        if (localIsValid && validator.test(localValue) && lock.validate(stamp)) {
            // stamp and value are validated - return
            return localValue;
        }

        // 2. nope - invalid value, (blocking) get the write lock
        stamp = lock.writeLock();
        try {
            // 2.1 double check that the valid is still not valid
            if (!this.isValid || !validator.test(this.value)) {
                // get new value from producer
                localValue = producer.get();
                this.value = localValue;
                this.isValid = true;
            } else {
                // get the value from this - it was produced by another thread in the meantime
                localValue = this.value;
            }
        } finally {
            lock.unlockWrite(stamp);
        }
        return localValue;
    }

    /**
     * Invalidates the value. (on the next {@link #getValue()} call a new value will be fetched.
     */
    public void invalidate() {
        long stamp = lock.writeLock();
        try {
            isValid = false;
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
