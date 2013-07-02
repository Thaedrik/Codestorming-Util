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
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see FragmentedInterval
 */
public class Interval implements Serializable {

	private static final long serialVersionUID = 2324819391610446221L;

	private long inferiorEndPoint;
	private long superiorEndPoint;

	private String cachedToString;

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
	 * Returns the value of {@code inferiorEndPoint}.
	 * 
	 * @return the value of {@code inferiorEndPoint}.
	 */
	public long getInferiorEndPoint() {
		return inferiorEndPoint;
	}

	/**
	 * Returns the value of {@code superiorEndPoint}.
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
		return point >= inferiorEndPoint && point <= superiorEndPoint;
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
		return contains(interval.getInferiorEndPoint()) && contains(interval.getSuperiorEndPoint());
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
		return superiorEndPoint + 1L == interval.inferiorEndPoint;
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
		return interval.isPreviousOf(this);
	}

	/**
	 * Indicates if the given interval intersect this one.
	 * 
	 * @param interval The interval.
	 * @return {@code true} if the given interval intersect this one;<br>
	 *         {@code false} otherwise.
	 */
	public boolean intersect(Interval interval) {
		return contains(interval.getInferiorEndPoint()) || contains(interval.getSuperiorEndPoint())
				|| interval.contains(getInferiorEndPoint()) || interval.contains(getSuperiorEndPoint());
	}

	/**
	 * Creates the interval corresponding to the intersection of this {@code Interval} and
	 * the given one or {@code null} if the two intervals do not intersect.
	 * 
	 * @param interval The interval to intersect with this one.
	 * @return the interval corresponding to the intersection of this {@code Interval} and
	 *         the given one or {@code null}.
	 */
	public Interval intersection(Interval interval) {
		Interval intersection = null;
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
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			builder.append(inferiorEndPoint);
			builder.append(',');
			builder.append(superiorEndPoint);
			builder.append(']');
			cachedToString = builder.toString();
		}
		return cachedToString;
	}
}
