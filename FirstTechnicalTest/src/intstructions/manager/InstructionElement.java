package intstructions.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Model element to hold data related to an instruction.
 */
public class InstructionElement {
	public enum InstructionType { BUY, SELL}
	
	public static DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("dd MMM yyyy");
	
	public static String PROPERTY_ENTITY = "ENTITY";
	public static String PROPERTY_INST_TYPE = "INST_TYPE";
	public static String PROPERTY_AGREED_FX = "AGREED_FX";
	public static String PROPERTY_CURRENCY = "CURRENCY";
	public static String PROPERTY_INST_DATE = "INST_DATE";
	public static String PROPERTY_SETTLEMENT_DATE = "SETTLEMENT_DATE";
	public static String PROPERTY_UNITS = "UNITS";
	public static String PROPERTY_PRICE_PER_UNIT = "PRICE_PER_UNIT";
	
	private static String AED = "AED";
	private static String SAR = "SAR";
	private static String[] CURRENCIES_ARRAY = {"USD", "GBP", "SGP", "AED", "SAR"};
	private static Set<String> CURRENCIES = new HashSet<>();
	
	private String entity;
	private InstructionType instType;
	
	private double agreedFX;
	private String currency;
	private LocalDate instDate;
	private LocalDate settlementDate;
	private long units;
	private double pricePerUnit;

	private List<PropertyChangeListener> listeners = new ArrayList<>();

	static {
		CURRENCIES.addAll(Arrays.asList(CURRENCIES_ARRAY));
	}
	
	InstructionElement(String entity, char instType, double agreedFX, String currency,
			String instDate, String settlementDate, long units, double pricePerUnit) {
		
		setEntity(entity);
		setInstructionType(instType);
		setCurrency(currency);
		setAgreedFX(agreedFX);
		setInstructionDate(instDate);
		setSettlementDate(settlementDate);
		setUnits(units);
		setPricePerUnit(pricePerUnit);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		if(l == null || listeners.contains(l)) {
			return;
		}
		
		listeners.add(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		if(l == null) {
			return;
		}
		
		listeners.remove(l);
	}
	
	private void notifyListeners(String property, Object oldValue, Object newValue) {
		if(oldValue == newValue) {
			return;
		}
		
		if(oldValue != null && oldValue.equals(newValue)) {
			return;
		}
		
		for(PropertyChangeListener l : listeners.toArray(new PropertyChangeListener[0])) {
			l.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
		}
	}
	
	public String getEntity() {
		return entity;
	}
	
	public boolean isSell() {
		return instType == InstructionType.SELL;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public double getAgreedFX() {
		return agreedFX;
	}
	
	public long getUnits() {
		return units;
	}
	
	public double getPricePerUnit() {
		return pricePerUnit;
	}
	
	public String getInstDate() {
		return instDate.format(DATE_FORMATER);
	}
	
	LocalDate getInternalInstDate() {
		return instDate;
	}
	
	public String getSettlementDate() {
		return settlementDate.format(DATE_FORMATER);
	}
	
	LocalDate getInternalSettlementDate() {
		return settlementDate;
	}
	
	public double getUSDAmount() {
		return (pricePerUnit * units) * agreedFX;
	}
	
	public void setEntity(String entitly) {
		if(entitly == null || entitly.isEmpty()) {
			throw new IllegalArgumentException("Entity can't be null or empty");
		}
		
		// Set new value and notify listeners
		String oldValue = this.entity;
		this.entity = entitly;
		notifyListeners(PROPERTY_ENTITY, oldValue, this.entity);
	}
	
	public void setInstructionType(char instType) {
		InstructionType oldValue = this.instType;

		instType = Character.toUpperCase(instType);
		if(instType == 'B') {
			this.instType = InstructionType.BUY;
		} else if(instType == 'S') {
			this.instType = InstructionType.SELL;
		} else {
			throw new IllegalArgumentException("Only buy(B) or sell(S) instrucitons are allowed");
		}
		
		// Notify listeners
		notifyListeners(PROPERTY_INST_TYPE, oldValue, this.instType);
	}
	
	public void setAgreedFX(double agreedFX) {
		if(agreedFX <= 0.0) {
			throw new IllegalArgumentException("AgreedFX should be greater than 0");
		}
		
		// Set new value and notify listeners
		double oldValue = this.agreedFX;
		this.agreedFX = agreedFX;
		notifyListeners(PROPERTY_AGREED_FX, oldValue, this.agreedFX);
	}
	
	public void setCurrency(String currency) {
		if(currency == null || currency.isEmpty()) {
			throw new IllegalArgumentException("Currency can't be null or empty");
		}

		currency = currency.toUpperCase();
		if(!CURRENCIES.contains(currency)) {
			throw new IllegalArgumentException(String.format("Invalid currency '%s'", currency));
		}
		
		// Set new value and notify listeners
		String oldValue = this.currency;
		this.currency = currency;
		notifyListeners(PROPERTY_CURRENCY, oldValue, this.currency);
		
		// Check if we need to change settlement date
		setSettlementDate(settlementDate);
	}
	
	public void setInstructionDate(String date) {
		if(date == null || date.isEmpty()) {
			throw new IllegalArgumentException("Date can't be null or empty");
		}
		
		LocalDate newInstDate = LocalDate.parse(date, DATE_FORMATER);
		if(settlementDate != null && newInstDate.isAfter(settlementDate)) {
			throw new IllegalArgumentException("Instruction date can't be after settlement date");
		}
		
		// Set new value and notify listeners
		LocalDate oldValue = this.instDate;
		this.instDate = newInstDate;
		notifyListeners(PROPERTY_INST_DATE, oldValue, this.instDate);
	}

	public void setSettlementDate(String date) {
		if(date == null || date.isEmpty()) {
			throw new IllegalArgumentException("Date can't be null or empty");
		}
		
		setSettlementDate(LocalDate.parse(date, DATE_FORMATER));
	}
	
	private void setSettlementDate(LocalDate date) {
		if(date == null) {
			return;
		}

		DayOfWeek dow = date.getDayOfWeek();
		int plusDays = 0;
		if(AED.equalsIgnoreCase(currency) || SAR.equalsIgnoreCase(currency)) {
			plusDays = DayOfWeek.FRIDAY == dow ? 2 : (DayOfWeek.SATURDAY == dow ? 1 : 0);
		} else {
			plusDays = DayOfWeek.SATURDAY == dow ? 2 : (DayOfWeek.SUNDAY == dow ? 1 : 0);
		}
		
		LocalDate newSettlementDate = date.plusDays(plusDays);
		if(instDate != null && instDate.isAfter(newSettlementDate)) {
			throw new IllegalArgumentException("Instruction date can't be after settlement date");
		}
		
		// Set new value and notify listeners
		LocalDate oldValue = this.settlementDate;
		this.settlementDate  = newSettlementDate;
		notifyListeners(PROPERTY_SETTLEMENT_DATE, oldValue, this.settlementDate);
	}
	
	public void setUnits(long units) {
		if(units <= 0) {
			throw new IllegalArgumentException("Units should be greater than 0");
		}
		
		// Set new value and notify listeners
		long oldValue = this.units;
		this.units = units;
		notifyListeners(PROPERTY_UNITS, oldValue, this.units);
	}
	
	public void setPricePerUnit(double pricePerUnit) {
		if(pricePerUnit <= 0.0) {
			throw new IllegalArgumentException("Price per unit should be greater than 0");
		}
		
		// Set new value and notify listeners
		double oldValue = this.pricePerUnit;
		this.pricePerUnit = pricePerUnit;
		notifyListeners(PROPERTY_PRICE_PER_UNIT, oldValue, this.pricePerUnit);
	}
}