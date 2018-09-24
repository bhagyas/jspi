package de.lohndirekt.print.attribute;

import java.io.ByteArrayInputStream;

import javax.print.attribute.Attribute;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

public class AttributeParserTest extends TestCase{
	@Test
	public void testParseResponseInfiniteLoop() throws InterruptedException{
		final boolean threadFinished[] = new boolean[]{false};
		final Object wait = new Object();
		
		Thread thread = new Thread(new Runnable() {
				//@Override
				public void run() {
					
					parse(new byte[]{});
					parse(new byte[]{0,0,0});
					parse(new byte[]{-1});
					
					synchronized (wait) {
						threadFinished[0] = true;
						wait.notify();
					}
				}
			}
		);
		thread.start();
		synchronized (wait) {
			if(!threadFinished[0]){
				wait.wait(1000);
			}
		}
		
		Assert.assertTrue(threadFinished[0]);
	}
	
	public void parse(byte[] data){
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0,0,0});
			Attribute a = null;
			while(true){
				a = AttributeParser.parseAttribute(inputStream, a);
			}
		} catch (Throwable e) {}
	}
}
