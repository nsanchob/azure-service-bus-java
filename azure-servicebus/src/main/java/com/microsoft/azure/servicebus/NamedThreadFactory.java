// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.azure.servicebus;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A named thread factory, based on {@link java.util.concurrent.Executors.DefaultThreadFactory the default thread factory}
 */
public class NamedThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	public NamedThreadFactory(final String poolPrefix) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null)
				? s.getThreadGroup()
				: Thread.currentThread().getThreadGroup();
		namePrefix =
				((poolPrefix != null) && (poolPrefix.length() != 0)
					? poolPrefix
					: "pool")
				+ '-' + poolNumber.getAndIncrement()
				+ "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r,
								namePrefix + threadNumber.getAndIncrement(),
								0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
