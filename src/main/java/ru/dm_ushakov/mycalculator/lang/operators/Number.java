package ru.dm_ushakov.mycalculator.lang.operators;

import java.math.BigDecimal;
import ru.dm_ushakov.mycalculator.lang.OperationContext;

public class Number extends AbstractOperator {
	private BigDecimal value;
	
	public Number(String number) {
		value = new BigDecimal(number);
	}
	
	public static Number getNegative(String number) {
		Number num = new Number(number);
		num.value = num.value.negate();
		return num;
	}

	@Override
	public BigDecimal evalute(OperationContext context) {
		return value;
	}
	
	@Override
	public String getDebugPayload() {
		return value.toString();
	}
}
