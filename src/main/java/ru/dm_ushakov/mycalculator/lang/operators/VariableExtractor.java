package ru.dm_ushakov.mycalculator.lang.operators;

import java.math.BigDecimal;

import ru.dm_ushakov.mycalculator.lang.OperationContext;

public class VariableExtractor extends AbstractOperator {
	private String variableName;
	
	public VariableExtractor(String varName) {
		variableName = varName;
	}
	
	public String getVariableName() {
		return variableName;
	}

	@Override
	public BigDecimal evalute(OperationContext context) {
		return context.getVariableValue(variableName);
	}
	
	@Override
	public String getDebugPayload() {
		return variableName;
	}
}
