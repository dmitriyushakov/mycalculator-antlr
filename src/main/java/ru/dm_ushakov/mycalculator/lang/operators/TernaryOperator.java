package ru.dm_ushakov.mycalculator.lang.operators;

import java.math.BigDecimal;

import ru.dm_ushakov.mycalculator.lang.OperationContext;

public class TernaryOperator extends AbstractOperator {
	private AbstractOperator condition;
	private AbstractOperator first;
	private AbstractOperator last;
	
	public TernaryOperator(AbstractOperator condition,AbstractOperator first,AbstractOperator last) {
		this.condition = condition;
		this.first = first;
		this.last = last;
		
		addChildOperator(condition);
		addChildOperator(first);
		addChildOperator(last);
	}

	@Override
	public BigDecimal evalute(OperationContext context) {
		return (!condition.evalute(context).equals(BigDecimal.ZERO)) ? first.evalute(context) : last.evalute(context);
	}
}
