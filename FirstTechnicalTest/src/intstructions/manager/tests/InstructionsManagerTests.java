package intstructions.manager.tests;

import org.junit.Assert;
import org.junit.Test;

import intstructions.manager.InstructionElement;
import intstructions.manager.InstructionsManager;

public class InstructionsManagerTests {

	@Test
	public void testAddAndRemove() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		try {
			Assert.assertEquals(1, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
	
	@Test
	public void testInstrunctionTypeChange() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		try {
			Assert.assertEquals(1, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
			
			// Now from buy to sell
			ie.setInstructionType('s');
			Assert.assertEquals(1, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
	
	@Test
	public void testInstrunctionDateChange() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		try {
			Assert.assertEquals(1, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
			
			// Now from buy to sell
			ie.setInstructionDate("04 May 2017");
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
			
			Assert.assertEquals(1, InstructionsManager.getInstance().getOutgoingRanking("04 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("04 May 2017").length);
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("04 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("04 May 2017").length);
		}
	}
	
	@Test
	public void testTotalAmount() {
		InstructionElement ie1 = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 8, 1.5);
		InstructionElement ie2 = InstructionsManager.getInstance().addInstructionElement("E2", 'b', 1.5, "GBP", "05 May 2017", "05 May 2017", 9, 1.5);
		InstructionElement ie3 = InstructionsManager.getInstance().addInstructionElement("E3", 'b', 2.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		
		InstructionElement ie4 = InstructionsManager.getInstance().addInstructionElement("E4", 's', 3.5, "GBP", "05 May 2017", "05 May 2017", 5, 1.5);
		InstructionElement ie5 = InstructionsManager.getInstance().addInstructionElement("E5", 's', 4.5, "GBP", "05 May 2017", "05 May 2017", 6, 1.5);
		InstructionElement ie6 = InstructionsManager.getInstance().addInstructionElement("E6", 's', 5.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		
		try {
			Assert.assertTrue(new Double(6.0 + 20.25 + 37.5).equals(InstructionsManager.getInstance().getTotalOutgoing("05 May 2017")));
			Assert.assertTrue(new Double(26.25 + 40.5 + 82.5).equals(InstructionsManager.getInstance().getTotalIncoming("05 May 2017")));
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie1);
			InstructionsManager.getInstance().removeInstructionElement(ie2);
			InstructionsManager.getInstance().removeInstructionElement(ie3);
			InstructionsManager.getInstance().removeInstructionElement(ie4);
			InstructionsManager.getInstance().removeInstructionElement(ie5);
			InstructionsManager.getInstance().removeInstructionElement(ie6);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
	
	@Test
	public void testRanking() {
		InstructionElement ie1 = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 8, 1.5);
		InstructionElement ie2 = InstructionsManager.getInstance().addInstructionElement("E2", 'b', 1.5, "GBP", "05 May 2017", "05 May 2017", 9, 1.5);
		InstructionElement ie3 = InstructionsManager.getInstance().addInstructionElement("E3", 'b', 2.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		
		InstructionElement ie4 = InstructionsManager.getInstance().addInstructionElement("E4", 's', 3.5, "GBP", "05 May 2017", "05 May 2017", 5, 1.5);
		InstructionElement ie5 = InstructionsManager.getInstance().addInstructionElement("E5", 's', 4.5, "GBP", "05 May 2017", "05 May 2017", 6, 1.5);
		InstructionElement ie6 = InstructionsManager.getInstance().addInstructionElement("E6", 's', 5.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		
		try {
			InstructionElement[] outRankings = InstructionsManager.getInstance().getOutgoingRanking("05 May 2017");
			Assert.assertEquals("E3", outRankings[0].getEntity());
			Assert.assertEquals("E2", outRankings[1].getEntity());
			Assert.assertEquals("E1", outRankings[2].getEntity());
			
			InstructionElement[] inRankings = InstructionsManager.getInstance().getIncomingRanking("05 May 2017");
			Assert.assertEquals("E6", inRankings[0].getEntity());
			Assert.assertEquals("E5", inRankings[1].getEntity());
			Assert.assertEquals("E4", inRankings[2].getEntity());
			
			// Now change agreed-fx to change the rankings
			ie1.setAgreedFX(2.5); // 2.5 * (8 * 1.5) = 30
			ie2.setAgreedFX(0.5); // 0.5 * (9 * 1.5) = 6.75
			ie3.setAgreedFX(1.5); // 1.5 * (10 * 1.5) = 22.5
			
			ie4.setAgreedFX(4.5); // 4.5 * (5 * 1.5) = 33.75
			ie5.setAgreedFX(5.5); // 5.5 * (6 * 1.5) = 49.5
			ie6.setAgreedFX(3.5); // 3.5 * (10 * 1.5) = 52.5
			
			// Now check rankings again
			outRankings = InstructionsManager.getInstance().getOutgoingRanking("05 May 2017");
			Assert.assertEquals("E1", outRankings[0].getEntity());
			Assert.assertEquals("E3", outRankings[1].getEntity());
			Assert.assertEquals("E2", outRankings[2].getEntity());
			
			inRankings = InstructionsManager.getInstance().getIncomingRanking("05 May 2017");
			Assert.assertEquals("E6", inRankings[0].getEntity());
			Assert.assertEquals("E5", inRankings[1].getEntity());
			Assert.assertEquals("E4", inRankings[2].getEntity());
			
			// Now change price per unit to change the rankings
			ie1.setPricePerUnit(2.5); // 2.5 * (8 * 2.5) = 50
			ie2.setPricePerUnit(12.5); // 0.5 * (9 * 12.5) = 56.25
			ie3.setPricePerUnit(1.5); // 1.5 * (10 * 1.5) = 22.5
			
			ie4.setPricePerUnit(9.5); // 4.5 * (5 * 9.5) = 213.75
			ie5.setPricePerUnit(5.5); // 5.5 * (6 * 5.5) = 181.5
			ie6.setPricePerUnit(3.5); // 3.5 * (10 * 3.5) = 122.5
			
			// Now check rankings again
			outRankings = InstructionsManager.getInstance().getOutgoingRanking("05 May 2017");
			Assert.assertEquals("E2", outRankings[0].getEntity());
			Assert.assertEquals("E1", outRankings[1].getEntity());
			Assert.assertEquals("E3", outRankings[2].getEntity());
			
			inRankings = InstructionsManager.getInstance().getIncomingRanking("05 May 2017");
			Assert.assertEquals("E4", inRankings[0].getEntity());
			Assert.assertEquals("E5", inRankings[1].getEntity());
			Assert.assertEquals("E6", inRankings[2].getEntity());
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie1);
			InstructionsManager.getInstance().removeInstructionElement(ie2);
			InstructionsManager.getInstance().removeInstructionElement(ie3);
			InstructionsManager.getInstance().removeInstructionElement(ie4);
			InstructionsManager.getInstance().removeInstructionElement(ie5);
			InstructionsManager.getInstance().removeInstructionElement(ie6);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
	
	@Test
	public void testPrint() {
		InstructionElement ie1 = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 8, 1.5);
		InstructionElement ie2 = InstructionsManager.getInstance().addInstructionElement("E2", 's', 1.5, "GBP", "05 May 2017", "05 May 2017", 9, 1.5);
		InstructionElement ie3 = InstructionsManager.getInstance().addInstructionElement("E3", 'b', 2.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		InstructionElement ie4 = InstructionsManager.getInstance().addInstructionElement("E4", 's', 3.5, "GBP", "05 May 2017", "05 May 2017", 5, 1.5);
		
		try {
			InstructionsManager.getInstance().print("05 May 2017", (date, ti, to, in, out) -> {
				Assert.assertEquals("05 May 2017", date);
				Assert.assertTrue(new Double(20.25 + 26.25).equals(ti));
				Assert.assertTrue(new Double(6.0 + 37.5).equals(to));
				Assert.assertEquals(2, in.length);
				Assert.assertEquals(ie4, in[0]);
				Assert.assertEquals(ie2, in[1]);
				Assert.assertEquals(2, out.length);
				Assert.assertEquals(ie3, out[0]);
				Assert.assertEquals(ie1, out[1]);
			});
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie1);
			InstructionsManager.getInstance().removeInstructionElement(ie2);
			InstructionsManager.getInstance().removeInstructionElement(ie3);
			InstructionsManager.getInstance().removeInstructionElement(ie4);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
	
	@Test
	public void testPrintAll() {
		InstructionElement ie1 = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "03 May 2017", "05 May 2017", 8, 1.5);
		InstructionElement ie2 = InstructionsManager.getInstance().addInstructionElement("E2", 's', 1.5, "GBP", "04 May 2017", "05 May 2017", 9, 1.5);
		InstructionElement ie3 = InstructionsManager.getInstance().addInstructionElement("E3", 'b', 2.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		InstructionElement ie4 = InstructionsManager.getInstance().addInstructionElement("E4", 's', 3.5, "GBP", "05 May 2017", "05 May 2017", 5, 1.5);
		
		try {
			InstructionsManager.getInstance().printAll((date, ti, to, in, out) -> {
				if("03 May 2017".equals(date)) {
					Assert.assertTrue(new Double(0.0).equals(ti));
					Assert.assertTrue(new Double(6.0).equals(to));
					Assert.assertEquals(0, in.length);
					Assert.assertEquals(1, out.length);
					Assert.assertEquals(ie1, out[0]);					
				} else if("04 May 2017".equals(date)) {
					Assert.assertTrue(new Double(20.25).equals(ti));
					Assert.assertTrue(new Double(0.0).equals(to));
					Assert.assertEquals(1, in.length);
					Assert.assertEquals(ie2, in[0]);
					Assert.assertEquals(0, out.length);
				} else if("05 May 2017".equals(date)) {
					Assert.assertTrue(new Double(26.25).equals(ti));
					Assert.assertTrue(new Double(37.5).equals(to));
					Assert.assertEquals(1, in.length);
					Assert.assertEquals(ie4, in[0]);
					Assert.assertEquals(1, out.length);
					Assert.assertEquals(ie3, out[0]);
				} else {
					Assert.fail();
				}
			});
		} finally {
			// Remove added instruction elements
			InstructionsManager.getInstance().removeInstructionElement(ie1);
			InstructionsManager.getInstance().removeInstructionElement(ie2);
			InstructionsManager.getInstance().removeInstructionElement(ie3);
			InstructionsManager.getInstance().removeInstructionElement(ie4);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("03 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("03 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("04 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("04 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getOutgoingRanking("05 May 2017").length);
			Assert.assertEquals(0, InstructionsManager.getInstance().getIncomingRanking("05 May 2017").length);
		}
	}
}
