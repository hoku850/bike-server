package org.ccframe.subsys.core.processor;

import org.ccframe.commons.base.IProcesser;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class ProcessorExample implements IProcesser{
	
	@Override
	@Scheduled(cron="0/3 * * * * ?}")
	public void process() {
		System.out.println("inner run");
	}

}
