/***************************************************************************
 * Copyright (c) 2013 Codestorming.org.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Codestorming - initial API and implementation
 ****************************************************************************/
package org.codestorming.util.misc;

import java.io.Serializable;

/**
 * Non-mutable representation of a <em>contiguous</em> interval between two endpoints.
 * <p>
 * The endpoints are {@link Long} integers and are limited to {@link Long#MIN_VALUE} and
 * {@link Long#MAX_VALUE}.
 * <p>
 * In case of the <em>empty interval</em>, the endpoints are {@code 0} but are
 * meaningless, when creating intervals through union or intersection, one should check if
 * the interval is empty.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see FragmentedInterval
 */
public class Interval implements Serializable {

	private static final long serialVersionUID = 2324819391610446221L;

	private boolean empty;

	private long inferiorEndPoint;
	private long superiorEndPoint;

	private transient String cachedToString;

	/**
	 * Creates the <em>empty</em> {@code Interval}.
	 */
	public Interval() {
		empty = true;
	}

	/**
	 * Creates a new {@code Interval}.
	 * 
	 * @param inferiorEndPoint The inferior endpoint.
	 * @param superiorEndPoint The superior endpoint.
	 * @throws IllegalArgumentException If {@code inferiorEndPoint > superiorEndpoint}.
	 */
	public Interval(long inferiorEndPoint, long superiorEndPoint) {
		if (inferiorEndPoint > superiorEndPoint) {
			throw new IllegalArgumentException("inferiorEndPoint can't be greater than superiorEndPoint.");
		}// else
		this.inferiorEndPoint = inferiorEndPoint;
		this.superiorEndPoint = superiorEndPoint;
	}

	/**
	 * Indicates if this {@code Interval} is the <em>empty interval</em>.
	 * 
	 * @return {@code true} if this {@code Interval} is the <em>empty interval</em>;<br>
	 *         {@code false} otherwise.
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * Returns the value of {@code inferiorEndPoint}.
	 * <p>
	 * <em>Meaningless if this {@code Interval} is {@link Interval#isEmpty() empty}.</em>
	 * 
	 * @return the value of {@code inferiorEndPoint}.
	 */
	public long getInferiorEndPoint() {
		return inferiorEndPoint;
	}

	/**
	 * Returns the value of {@code superiorEndPoint}.
	 * <p>
	 * <em>Meaningless if this {@code Interval} is {@link Interval#isEmpty() empty}.</em>
	 * 
	 * @return the value of {@code superiorEndPoint}.
	 */
	public long getSuperiorEndPoint() {
		return superiorEndPoint;
	}

	/**
	 * Indicates if the given point is contained in this {@code Interval}.<br>
	 * That is, if {@code inferiorEndPoint <= point <= superiorEndPoint}.
	 * 
	 * @param point The point for which to know if it is contained in this
	 *        {@code Interval}.
	 * @return {@code true} if the given point is contained in this {@code Interval};<br>
	 *         {@code false} otherwise.
	 */
	public boolean contains(long point) {
		return !empty && point >= inferiorEndPoint && point <= superiorEndPoint;
	}

	/**
	 * Indicates if the given interval is contained in this {@code Interval}.
	 * 
	 * @param interval The interval for which to know if it is contained in this
	 *        {@code Interval}.
	 * @return {@code true} if the given interval is contained in this {@code Interval};<br>
	 *         {@code false} otherwise.
	 */
	public boolean contains(Interval interval) {
		if (empty) {
			return interval.isEmpty();
		}// else
		return interval.isEmpty() || contains(interval.getInferiorEndPoint())
				&& contains(interval.getSuperiorEndPoint());
	}

	/**
	 * Indicates if this {@code Interval} is an interval just before the given one.<br>
	 * That is, if {@code this.superiorEndPoint + 1 == interval.inferiorEndPoint}.
	 * 
	 * @param interval The interval that should be next.
	 * @return {@code true} if this {@code Interval} is an interval just before the given
	 *         one;<br>
	 *         {@code false} otherwise.
	 */
	public boolean isPreviousOf(Interval interval) {
		return !empty && superiorEndPoint + 1L == interval.inferiorEndPoint;
	}

	/**
	 * Indicates if this {@code Interval} is an interval just after the given one.<br>
	 * That is, if {@code this.inferiorEndPoint - 1 == interval.superiorEndPoint}.
	 * 
	 * @param interval The interval that should be before.
	 * @return {@code true} if this {@code Interval} is an interval just after the given
	 *         one;<br>
	 *         {@code false} otherwise.
	 */
	public boolean isNextOf(Interval interval) {
		return !empty && interval.isPreviousOf(this);
	}

	/**
	 * Indicates if the given interval intersect this one.
	 * 
	 * @param interval The interval.
	 * @return {@code true} if the given interval intersect this one;<br>
	 *         {@code false} otherwise.
	 */
	public boolean intersect(Interval interval) {
		return !empty
				&& (contains(interval.getInferiorEndPoint()) || contains(interval.getSuperiorEndPoint())
						|| interval.contains(getInferiorEndPoint()) || interval.contains(getSuperiorEndPoint()));
	}

	/**
	 * Creates the interval corresponding to the intersection of this {@code Interval} and
	 * the given one or the <em>empty interval</em> if the two intervals do not intersect.
	 * 
	 * @param interval The interval to intersect with this one.
	 * @return the interval corresponding to the intersection of this {@code Interval} and
	 *         the given one or the <em>empty interval</em>.
	 */
	public Interval intersection(Interval interval) {
		Interval intersection = new Interval();
		if (intersect(interval)) {
			final long infEndP = Math.max(inferiorEndPoint, interval.getInferiorEndPoint());
			final long supEndP = Math.min(superiorEndPoint, interval.getSuperiorEndPoint());
			intersection = new Interval(infEndP, supEndP);
		}
		return intersection;
	}

	/**
	 * Creates the interval corresponding to the union of this {@code Interval} and
	 * the given one.
	 * 
	 * @param interval The interval for which to create the union with this one.
	 * @return the interval corresponding to the union of this {@code Interval} and
	 *         the given one.
	 * @throws NotContiguousIntervalException if the union between the two interval is not
	 *         contiguous.
	 */
	public Interval union(Interval interval) {
		if (empty) {
			return interval;
		}// else
		final long maxInfEndP = Math.max(inferiorEndPoint, interval.getInferiorEndPoint());
		final long minSupEndP = Math.min(superiorEndPoint, interval.getSuperiorEndPoint());
		if (maxInfEndP > minSupEndP + 1) {
			throw new NotContiguousIntervalException();
		}// else
		final long infEndP = Math.min(inferiorEndPoint, interval.getInferiorEndPoint());
		final long supEndP = Math.max(superiorEndPoint, interval.getSuperiorEndPoint());
		return new Interval(infEndP, supEndP);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Interval))
			return false;
		Interval other = (Interval) obj;
		if (empty) {
			return other.isEmpty();
		}
		return inferiorEndPoint == other.inferiorEndPoint && superiorEndPoint == other.superiorEndPoint;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Returns the string representation of this {@code Interval}.
	 * <p>
	 * e.g. {@code [-1, 10]} for an interval between {@code -1} and {@code 10}.
	 */
	@Override
	public String toString() {
		if (cachedToString == null) {
			if (!empty) {
				StringBuilder builder = new StringBuilder();
				builder.append('[');
				builder.append(inferiorEndPoint);
				builder.append(',');
				builder.append(superiorEndPoint);
				builder.append(']');
				cachedToString = builder.toString();
			} else {
				cachedToString = "{\u00D8}";
			}
		}
		return cachedToString;
	}
}
