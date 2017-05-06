package intstructions;

import intstructions.manager.InstructionElement;
import intstructions.manager.InstructionsManager;

public class MianClass {

	public static void main(String[] args) {
		InstructionsManager.getInstance().addInstructionElement("E1", 'b', 0.5, "GBP", "03 May 2017", "05 May 2017", 8, 1.5);
		InstructionsManager.getInstance().addInstructionElement("E2", 's', 1.5, "GBP", "04 May 2017", "05 May 2017", 9, 1.5);
		InstructionsManager.getInstance().addInstructionElement("E3", 'b', 2.5, "GBP", "05 May 2017", "05 May 2017", 10, 1.5);
		InstructionsManager.getInstance().addInstructionElement("E4", 's', 3.5, "GBP", "05 May 2017", "05 May 2017", 5, 1.5);
		
		InstructionsManager.getInstance().printAll((date, ti, to, in, out) -> {
			System.out.println("");
			System.out.println("\t\t\t\t------------------------- " + date + " -------------------------\t\t\t\t");
			System.out.println("Total incomings:\t" + ti);
			System.out.println("Total outgoings:\t" + to);
			System.out.println("");
			
			System.out.println("Incoming rankings:");
			if(in.length == 0) {
				System.out.println("No incomings");
			} else {
				printInstrunctionElementHeader();
				for(InstructionElement ie : in) {
					printInstrunctionElement(ie);
				}
			}
			
			System.out.println("");
			System.out.println("Outgoing rankings:");
			if(out.length == 0) {
				System.out.println("No outgoings");
			} else {
				printInstrunctionElementHeader();
				for(InstructionElement ie : out) {
					printInstrunctionElement(ie);
				}
			}
			System.out.println("");
		});
	}
	
	private static void printInstrunctionElementHeader() {
		System.out.println("Entity\t\tAgreedFX\tCurrency\tInstruction date\tSettlement date\tUnits\tPrice per unit\tTotal amount(USD)");
	}
	
	private static void printInstrunctionElement(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		System.out.println(String.format("%s\t\t%.2f\t\t%s\t\t%s\t\t%s\t%d\t%.2f\t\t%.2f",
										ie.getEntity(),
										ie.getAgreedFX(),
										ie.getCurrency(),
										ie.getInstDate(),
										ie.getSettlementDate(),
										ie.getUnits(),
										ie.getPricePerUnit(),
										ie.getUSDAmount()));
	}
}
