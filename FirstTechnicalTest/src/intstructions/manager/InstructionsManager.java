package intstructions.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import intstructions.manager.InstructionElement.InstructionType;

public class InstructionsManager implements PropertyChangeListener {

	private static final InstructionsManager INSTANCE = new InstructionsManager();
	
	private Comparator<InstructionElement> comparator = new Comparator<InstructionElement>() {
        @Override
        public int compare(InstructionElement ie1, InstructionElement ie2) {
        	if(new Double(ie1.getUSDAmount()).equals(ie2.getUSDAmount())) {
        		return 0;
        	}
        	
        	return ie1.getUSDAmount() - ie2.getUSDAmount() > 0 ? -1 : 1;
        }
    };
    
    private Map<LocalDate, Set<InstructionElement>> incomings = new TreeMap<>();
    private Map<LocalDate, Set<InstructionElement>> outgoings = new TreeMap<>();

	private InstructionsManager() {
	}

	public static InstructionsManager getInstance() {
		return INSTANCE;
	}
	
	public InstructionElement addInstructionElement(String entity, char instType, double agreedFX, String currency,
			String instDate, String settlementDate, long units, double pricePerUnit) {
		InstructionElement ie = new InstructionElement(entity, instType, agreedFX, currency, instDate, settlementDate, units, pricePerUnit);
		ie.addPropertyChangeListener(this);
		if(ie.isSell()) {
			addToIncomings(ie);
		} else {
			addToOutgoings(ie);
		}
		
		return ie;
	}
	
	public void removeInstructionElement(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		ie.removePropertyChangeListener(this);
		if(ie.isSell()) {
			removeFromIncomings(ie);
		} else {
			removeFromOutgoings(ie);
		}
	}
	
	private void addToIncomings(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		Set<InstructionElement> set = incomings.get(ie.getInternalInstDate());
		if(set == null) {
			incomings.put(ie.getInternalInstDate(), set = new HashSet<>());
		}
		set.add(ie);
	}
	
	private void addToOutgoings(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		Set<InstructionElement> set = outgoings.get(ie.getInternalInstDate());
		if(set == null) {
			outgoings.put(ie.getInternalInstDate(), set = new HashSet<>());
		}
		set.add(ie);
	}
	
	private void removeFromIncomings(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		Set<InstructionElement> set = incomings.get(ie.getInternalInstDate());
		if(set == null) {
			return;
		}
		set.remove(ie);
	}
	
	private void removeFromOutgoings(InstructionElement ie) {
		if(ie == null) {
			return;
		}
		
		Set<InstructionElement> set = outgoings.get(ie.getInternalInstDate());
		if(set == null) {
			return;
		}
		set.remove(ie);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getOldValue() == evt.getNewValue()) {
			return;
		}
		
		if(evt.getOldValue() != null && evt.getOldValue().equals(evt.getNewValue())) {
			return;
		}
		
		InstructionElement ie = (InstructionElement) evt.getSource();
		if(InstructionElement.PROPERTY_INST_TYPE.equals(evt.getPropertyName())) {
			if(evt.getOldValue() == InstructionType.SELL) {
				removeFromIncomings(ie);
			} else {
				removeFromOutgoings(ie);
			}
			
			if(evt.getNewValue() == InstructionType.SELL) {
				addToIncomings(ie);
			} else {
				addToOutgoings(ie);
			}
		} else if(InstructionElement.PROPERTY_INST_DATE.equals(evt.getPropertyName())) {
			LocalDate oldDate = (LocalDate) evt.getOldValue();
			if(ie.isSell()) {
				// Remove from previous set
				Set<InstructionElement> set = incomings.get(oldDate);
				if(set != null) {
					set.remove(ie);
				}
				
				// Re-add the element.
				addToIncomings(ie);
			} else {
				// Remove from previous set
				Set<InstructionElement> set = outgoings.get(oldDate);
				if(set != null) {
					set.remove(ie);
				}
				
				// Re-add the element.
				addToOutgoings(ie);
			}
		}
	}
	
	public double getTotalIncoming(String instDate) {
		return getTotalUSDAmount(instDate, incomings);
	}
	
	public double getTotalOutgoing(String instDate) {
		return getTotalUSDAmount(instDate, outgoings);
	}
	
	private double getTotalUSDAmount(String instDate, Map<LocalDate, Set<InstructionElement>> map) {
		if(instDate == null || instDate.isEmpty()) {
			throw new IllegalArgumentException("Date can't be null or empty");
		}
		
		if(map == null) {
			return 0.0;
		}
		
		Set<InstructionElement> set = map.get(LocalDate.parse(instDate, InstructionElement.DATE_FORMATER));
		if(set == null) {
			return 0.0;
		}
		
		double returnValue = 0.0;
		for(InstructionElement ie : set.toArray(new InstructionElement[0])) {
			returnValue += ie.getUSDAmount();
		}
		
		return returnValue;
	}
	
	public InstructionElement[] getIncomingRanking(String instDate) {
		return getRanking(instDate, incomings);
	}
	
	public InstructionElement[] getOutgoingRanking(String instDate) {
		return getRanking(instDate, outgoings);
	}
	
	private InstructionElement[] getRanking(String instDate, Map<LocalDate, Set<InstructionElement>> map) {
		if(instDate == null || instDate.isEmpty()) {
			throw new IllegalArgumentException("Date can't be null or empty");
		}
		
		if(map == null) {
			return new InstructionElement[0];
		}
		
		Set<InstructionElement> set = map.get(LocalDate.parse(instDate, InstructionElement.DATE_FORMATER));
		if(set == null) {
			return new InstructionElement[0];
		}
		
		InstructionElement[] ies = set.toArray(new InstructionElement[0]);
		Arrays.sort(ies, comparator);
		return ies;
	}
	
	public void print(String instDate, IInstructionElementsPrinter printer) {
		if(printer == null) {
			throw new IllegalArgumentException("Printer can't be null");
		}
		
		printer.printForADay(instDate,
				getTotalIncoming(instDate),
				getTotalOutgoing(instDate),
				getIncomingRanking(instDate),
				getOutgoingRanking(instDate));
	}
	
	public void printAll(IInstructionElementsPrinter printer) {
		if(printer == null) {
			throw new IllegalArgumentException("Printer can't be null");
		}
		
		Set<LocalDate> dates = new TreeSet<>(new Comparator<LocalDate>() {
			@Override
			public int compare(LocalDate o1, LocalDate o2) {
				// We want latest date on top
				return o2.compareTo(o1);
			}
		});
		dates.addAll(incomings.keySet());
		dates.addAll(outgoings.keySet());
		
		for(LocalDate date : dates.toArray(new LocalDate[0])) {
			String instDate = date.format(InstructionElement.DATE_FORMATER);
			printer.printForADay(instDate,
					getTotalIncoming(instDate),
					getTotalOutgoing(instDate),
					getIncomingRanking(instDate),
					getOutgoingRanking(instDate));
		}
	}
	
	/**
	 * Interface to print the data.
	 */
	public interface IInstructionElementsPrinter {
		public void printForADay(String date, double totalUSDIncoming, double totalUSDOutgoing, InstructionElement[] incomings, InstructionElement[] outgoings);
	}
}
