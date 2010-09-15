package com.schneide.crapertura.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrapStatistics {
	
	private final static double NOT_YET_CALCULATED = -1.00d;
	
	private final Map<Aspect, StatisticAspect> aspectsMap;
	private final int crapThreshold;
	
	protected CrapStatistics(Iterable<StatisticAspect> aspects, int crapThreshold) {
		super();
		this.crapThreshold = crapThreshold;
		this.aspectsMap = new HashMap<CrapStatistics.Aspect, CrapStatistics.StatisticAspect>();
		for (StatisticAspect statisticAspect : aspects) {
			this.aspectsMap.put(statisticAspect.getIdentifier(), statisticAspect);
		}
	}
	
	public static CrapStatistics fromMethodData(Iterable<MethodCrapData> data) {
		List<StatisticAspect> aspects = new ArrayList<CrapStatistics.StatisticAspect>();
		aspects.add(new MethodCount());
		aspects.add(new TotalCrap());
		aspects.add(new CrapMethodCount());
		aspects.add(new CrapLoad());
		aspects.add(new Median());
		aspects.add(new StandardDeviation());
		
		int crapThreshold = 30;
		for (MethodCrapData method : data) {
			crapThreshold = method.getCrapThreshold();
			for (StatisticAspect statisticAspect : aspects) {
				statisticAspect.include(method);
			}
		}
		return new CrapStatistics(aspects, crapThreshold);
	}
	
	protected enum Aspect {
		METHOD_COUNT,
		TOTAL_CRAP,
		CRAP_METHOD_COUNT,
		CRAP_LOAD,
		MEDIAN,
		STANDARD_DEVIATION,
	}
	
	protected static interface StatisticAspect {
		public Aspect getIdentifier();
		
		public void include(MethodCrapData methodData);
		
		public Number getResult();
	}
	
	protected static abstract class AspectBase implements StatisticAspect {
		private final Aspect identifier;

		public AspectBase(Aspect identifier) {
			super();
			this.identifier = identifier;
		}
		
		@Override
		public Aspect getIdentifier() {
			return this.identifier;
		}
	}
	
	protected static class Median extends AspectBase {
		private final List<Double> crapValues;
		
		public Median() {
			super(Aspect.MEDIAN);
			this.crapValues = new ArrayList<Double>();
		}
		
		@Override
		public void include(MethodCrapData methodData) {
			this.crapValues.add(Double.valueOf(methodData.getCrap()));
		}
		
		@Override
		public Number getResult() {
			org.apache.commons.math.stat.descriptive.rank.Median median = new org.apache.commons.math.stat.descriptive.rank.Median();
			double[] values = new double[crapValues.size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = crapValues.get(i).doubleValue();
			}
			return Double.valueOf(median.evaluate(values));
		}
	}
	
	protected static class StandardDeviation extends AspectBase {
		private final org.apache.commons.math.stat.descriptive.moment.StandardDeviation calculator;
		
		public StandardDeviation() {
			super(Aspect.STANDARD_DEVIATION);
			this.calculator = new org.apache.commons.math.stat.descriptive.moment.StandardDeviation();
		}
		
		@Override
		public void include(MethodCrapData methodData) {
			this.calculator.increment(methodData.getCrap());
		}
		
		@Override
		public Number getResult() {
			return Double.valueOf(this.calculator.getResult());
		}
	}
	
	protected static abstract class IntegerAdditionAspect extends AspectBase {
		private int result;
		
		public IntegerAdditionAspect(Aspect identifier) {
			super(identifier);
			this.result = 0;
		}
		
		@Override
		public abstract void include(MethodCrapData methodData);

		protected void addToResult(int offset) {
			this.result += offset;
		}
		
		@Override
		public Number getResult() {
			return Integer.valueOf(this.result);
		}
	}

	protected static abstract class DoubleAdditionAspect extends AspectBase {
		private double result;
		
		public DoubleAdditionAspect(Aspect identifier) {
			super(identifier);
			this.result = 0;
		}
		
		@Override
		public abstract void include(MethodCrapData methodData);

		protected void addToResult(double offset) {
			this.result += offset;
		}
		
		@Override
		public Number getResult() {
			return Double.valueOf(this.result);
		}
	}

	protected static class TotalCrap extends DoubleAdditionAspect {
		public TotalCrap() {
			super(Aspect.TOTAL_CRAP);
		}

		@Override
		public void include(MethodCrapData methodData) {
			addToResult(methodData.getCrap());
		}
	}

	protected static class CrapLoad extends IntegerAdditionAspect {
		public CrapLoad() {
			super(Aspect.CRAP_LOAD);
		}
		
		@Override
		public void include(MethodCrapData methodData) {
			addToResult(methodData.getCrapLoad());
		}
	}
	
	protected static class MethodCount extends IntegerAdditionAspect {
		public MethodCount() {
			super(Aspect.METHOD_COUNT);
		}
		
		@Override
		public void include(MethodCrapData methodData) {
			addToResult(1);
		}
	}

	protected static class CrapMethodCount extends IntegerAdditionAspect {
		public CrapMethodCount() {
			super(Aspect.CRAP_METHOD_COUNT);
		}
		
		@Override
		public void include(MethodCrapData methodData) {
			if (methodData.getCrapLoad() > 0) {
				addToResult(1);
			}
		}
	}
	
	protected Number getResultFor(Aspect aspect) {
		return this.aspectsMap.get(aspect).getResult();
	}

	public double getTotalCrap() {
		return getResultFor(Aspect.TOTAL_CRAP).doubleValue();
	}

	public double getCrap() {
		return getTotalCrap() / ((double) getMethodCount());
	}

	public double getMedian() {
		return getResultFor(Aspect.MEDIAN).doubleValue();
	}

	public double getAverage() {
		return getTotalCrap() / ((double) getMethodCount());
	}

	public double getStandardDeviation() {
		return getResultFor(Aspect.STANDARD_DEVIATION).doubleValue();
	}

	public int getMethodCount() {
		return getResultFor(Aspect.METHOD_COUNT).intValue();
	}

	public int getCrapMethodCount() {
		return getResultFor(Aspect.CRAP_METHOD_COUNT).intValue();
	}

	public double getCrapMethodPercent() {
		return (((double) getCrapMethodCount()) / ((double) getMethodCount()));
	}

	public int getCrapLoad() {
		return getResultFor(Aspect.CRAP_LOAD).intValue();
	}

	public int getCrapThreshold() {
		return this.crapThreshold;
	}

	public double getGlobalAverage() {
		return NOT_YET_CALCULATED;
	}

	public double getGlobalCraploadAverage() {
		return NOT_YET_CALCULATED;
	}

	public double getGlobalCrapMethodAverage() {
		return NOT_YET_CALCULATED;
	}

	public double getGlobalCrapMethodPercent() {
		return NOT_YET_CALCULATED;
	}

	public double getGlobalAverageDiff() {
		return getAverage() + 1.0d;
	}

	public double getGlobalCraploadAverageDiff() {
		return getCrapLoad() + 1.0d;
	}

	public double getGlobalCrapMethodAverageDiff() {
		return getCrapMethodCount() + 1.0d;
	}

	public double getGlobalTotalMethodAverageDiff() {
		return getMethodCount() + 1.0d;
	}
}
