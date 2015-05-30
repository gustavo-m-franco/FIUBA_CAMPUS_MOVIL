package ar.uba.fi.fiubappREST.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.junit.Before;
import org.junit.Test;

public class CustomDateSerializerDetailedTest {
	
	private CustomDateSerializerDetailed serializer;
	private JsonGenerator mockJsonGenerator;
	
	@Before
	public void setUp(){
		serializer = new CustomDateSerializerDetailed();
		mockJsonGenerator = mock(JsonGenerator.class);
	}

	@Test
	public void testSerializeOK() {
		try {
			serializer.serialize(new Date(), mockJsonGenerator, null);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}
}

