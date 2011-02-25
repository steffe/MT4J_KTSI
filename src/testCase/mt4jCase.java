package testCase;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

public class mt4jCase extends TestCase {
	
	public void testEmptyCollection(){
		Collection coll = new ArrayList();
		coll.add( new Object());
		assertEquals("is empty", 1, coll.size());
		
	}

}
