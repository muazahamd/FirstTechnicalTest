package intstructions.manager.tests;

import java.time.DateTimeException;

import org.junit.Assert;
import org.junit.Test;

import intstructions.manager.InstructionElement;
import intstructions.manager.InstructionsManager;

public class InstructionElementTests {

	@Test
	public void testInvalidValues() {
		try {
			InstructionsManager.getInstance().addInstructionElement(null, 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Entity can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Entity can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'e', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Only buy(B) or sell(S) instrucitons are allowed", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.0, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("AgreedFX should be greater than 0", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', -0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("AgreedFX should be greater than 0", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 's', 0.5, null, "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Currency can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 's', 0.5, "", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Currency can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 's', 0.5, "ZDE", "05 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Invalid currency 'ZDE'", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", null, "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Date can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Date can't be null or empty", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "null", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (DateTimeException e) {
			// Success
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "32 May 2017", "05 May 2017", 10, 1.5);
			Assert.fail();
		} catch (DateTimeException e) {
			// Success
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 0, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Units should be greater than 0", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", -5, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Units should be greater than 0", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 0.0);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Price per unit should be greater than 0", e.getMessage());
		}
		
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, -1.0);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Price per unit should be greater than 0", e.getMessage());
		}
	}
	
	@Test
	public void testInstructionDateAfterSettlementDate() {
		try {
			InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "04 May 2017", 10, 1.5);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Instruction date can't be after settlement date", e.getMessage());
		}
		
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "04 May 2017", "04 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("04 May 2017", ie.getInstDate());
		Assert.assertEquals("04 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Try to change instruction date after settlement date
		try {
			ie.setInstructionDate("05 May 2017");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Instruction date can't be after settlement date", e.getMessage());
		}
		// The instruction date should still be the old one
		Assert.assertEquals("04 May 2017", ie.getInstDate());
		
		// Try to change settlement date before instruction date
		try {
			ie.setSettlementDate("03 May 2017");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Instruction date can't be after settlement date", e.getMessage());
		}
		// The settlement date should still be the old one
		Assert.assertEquals("04 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnThursday() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "04 May 2017", "04 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("04 May 2017", ie.getInstDate());
		Assert.assertEquals("04 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to AED but its Thursday so it shouldn't change anything
		ie.setCurrency("AED");
		Assert.assertEquals("04 May 2017", ie.getInstDate());
		Assert.assertEquals("04 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnFridayAndChangeCurrencyToAED() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("05 May 2017", ie.getInstDate());
		Assert.assertEquals("05 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to AED. Since the old settlement was on Friday so it will change to Sunday
		ie.setCurrency("AED");
		Assert.assertEquals("05 May 2017", ie.getInstDate());
		Assert.assertEquals("07 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnFridayWithAED() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "AED", "05 May 2017", "05 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("AED", ie.getCurrency());
		Assert.assertEquals("05 May 2017", ie.getInstDate());
		Assert.assertEquals("07 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to GBP. Since the old settlement was on Sunday so it will change to Monday
		ie.setCurrency("GBP");
		Assert.assertEquals("05 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnSaturday() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "06 May 2017", "06 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("06 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to AED. Since settlement is already on Monday, this will not change anything.
		ie.setCurrency("AED");
		Assert.assertEquals("06 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnSunday() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "07 May 2017", "07 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("07 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to AED. Since settlement is already on Monday, this will not change anything.
		ie.setCurrency("AED");
		Assert.assertEquals("07 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
	
	@Test
	public void testInstructionsOnMonday() {
		InstructionElement ie = InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "08 May 2017", "08 May 2017", 10, 1.5);
		Assert.assertEquals("E1", ie.getEntity());
		Assert.assertFalse(ie.isSell());
		Assert.assertTrue(0.5 == ie.getAgreedFX());
		Assert.assertEquals("GBP", ie.getCurrency());
		Assert.assertEquals("08 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		Assert.assertEquals(10, ie.getUnits());
		Assert.assertTrue(1.5 == ie.getPricePerUnit());
		Assert.assertTrue(((1.5 * 10) * 0.5) == ie.getUSDAmount());
		
		// Change the currency to AED. Since settlement is already on Monday, this will not change anything.
		ie.setCurrency("AED");
		Assert.assertEquals("08 May 2017", ie.getInstDate());
		Assert.assertEquals("08 May 2017", ie.getSettlementDate());
		
		// Remove added instruction element
		InstructionsManager.getInstance().removeInstructionElement(ie);
	}
}
