
package org.drip.xva.dynamics;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * PathSimulator drives the Simulation for various Latent States and Exposures. The References are:
 *  
 *  - Burgard, C., and M. Kjaer (2014): PDE Representations of Derivatives with Bilateral Counter-party Risk
 *  	and Funding Costs, Journal of Credit Risk, 7 (3) 1-19.
 *  
 *  - Burgard, C., and M. Kjaer (2014): In the Balance, Risk, 24 (11) 72-75.
 *  
 *  - Albanese, C., and L. Andersen (2014): Accounting for OTC Derivatives: Funding Adjustments and the
 *  	Re-Hypothecation Option, eSSRN, https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2482955.
 *  
 *  - Burgard, C., and M. Kjaer (2017): Derivatives Funding, Netting, and Accounting, eSSRN,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2534011.
 * 
 *  - Piterbarg, V. (2010): Funding Beyond Discounting: Collateral Agreements and Derivatives Pricing, Risk
 *  	21 (2) 97-102.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class PathSimulator
{
	private int _iCount = -1;
	private org.drip.xva.dynamics.PathSimulatorScheme _simulatorScheme = null;
	private org.drip.xva.universe.MarketVertexGenerator _marketVertexGenerator = null;
	private org.drip.xva.holdings.PositionGroupContainer _positionGroupContainer = null;

	private double[][] positionGroupValueArray (
		final org.drip.xva.universe.MarketVertex[] marketVertexArray)
	{
		org.drip.xva.holdings.PositionGroup[] positionGroupArray =
			_positionGroupContainer.positionGroupArray();

		int vertexCount = marketVertexArray.length;
		int positionGroupCount = positionGroupArray.length;
		double[][] positionGroupValueArray = new double[positionGroupCount][vertexCount];

		for (int positionGroupIndex = 0; positionGroupIndex < positionGroupCount; ++positionGroupIndex)
		{
			for (int vertexIndex = 0; vertexIndex < vertexCount; ++vertexIndex)
			{
				try {
					positionGroupValueArray[positionGroupIndex][vertexIndex] =
						marketVertexArray[vertexIndex].positionManifestValue() *
						(null == positionGroupArray[positionGroupIndex].positionGroupNumeraire() ? 1. :
						positionGroupArray[positionGroupIndex].positionGroupNumeraire().value
						(marketVertexArray[vertexIndex]));
				}
				catch (java.lang.Exception e)
				{
					e.printStackTrace();

					return null;
				}
			}
		}

		return positionGroupValueArray;
	}

	private org.drip.measure.bridge.BrokenDateInterpolator brokenDateInterpolator (
		final org.drip.xva.universe.MarketVertex[] marketVertexArray,
		final double[] positionGroupValueArray,
		final int vertexIndex)
		throws java.lang.Exception
	{
		int brokenDateScheme = _simulatorScheme.brokenDateScheme();

		if (org.drip.xva.dynamics.BrokenDateScheme.LINEAR_TIME == brokenDateScheme)
		{
			return 0 == vertexIndex ? null : new org.drip.measure.bridge.BrokenDateInterpolatorLinearT (
				marketVertexArray[vertexIndex - 1].anchorDate().julian(),
				marketVertexArray[vertexIndex].anchorDate().julian(),
				positionGroupValueArray[vertexIndex - 1],
				positionGroupValueArray[vertexIndex]
			);
		}

		if (org.drip.xva.dynamics.BrokenDateScheme.SQUARE_ROOT_OF_TIME == brokenDateScheme)
		{
			return 0 == vertexIndex ? null : new org.drip.measure.bridge.BrokenDateInterpolatorSqrtT (
				marketVertexArray[vertexIndex - 1].anchorDate().julian(),
				marketVertexArray[vertexIndex].anchorDate().julian(),
				positionGroupValueArray[vertexIndex - 1],
				positionGroupValueArray[vertexIndex]
			);
		}

		if (org.drip.xva.dynamics.BrokenDateScheme.THREE_POINT_BROWNIAN_BRIDGE == brokenDateScheme)
		{
			return 0 == vertexIndex || 1 == vertexIndex ? null : new
				org.drip.measure.bridge.BrokenDateInterpolatorBrownian3P (
					marketVertexArray[vertexIndex - 2].anchorDate().julian(),
					marketVertexArray[vertexIndex - 1].anchorDate().julian(),
					marketVertexArray[vertexIndex].anchorDate().julian(),
					positionGroupValueArray[vertexIndex - 2],
					positionGroupValueArray[vertexIndex - 1],
					positionGroupValueArray[vertexIndex]
				);
		}

		return null;
	}

	private double collateralBalance (
		final org.drip.xva.universe.MarketVertex[] marketVertexArray,
		final org.drip.xva.holdings.PositionGroup positionGroup,
		final int vertexIndex)
		throws java.lang.Exception
	{
		double[] positionGroupValueArray = positionGroup.valueArray (marketVertexArray);

		org.drip.measure.bridge.BrokenDateInterpolator brokenDateInterpolator = brokenDateInterpolator (
			marketVertexArray,
			positionGroupValueArray,
			vertexIndex
		);

		org.drip.xva.set.PositionGroupSpecification positionGroupSpecification =
			positionGroup.positionGroupSpecification();

		return null == brokenDateInterpolator ? 0. : new org.drip.xva.hypothecation.CollateralAmountEstimator
			(positionGroupSpecification.collateralGroupSpecification(),
			positionGroupSpecification.counterPartyGroupSpecification(),
			brokenDateInterpolator (
				marketVertexArray,
				positionGroupValueArray,
				vertexIndex
			),
			java.lang.Double.NaN
		).postingRequirement (marketVertexArray[vertexIndex].anchorDate());
	}

	private double[][] collateralBalanceArray (
		final org.drip.xva.universe.MarketVertex[] marketVertexArray,
		final double[][] positionGroupValueArray)
	{
		org.drip.xva.holdings.PositionGroup[] positionGroupArray =
			_positionGroupContainer.positionGroupArray();

		int vertexCount = marketVertexArray.length;
		int positionGroupCount = positionGroupValueArray.length;
		double[][] collateralBalanceArray = new double[positionGroupCount][vertexCount];

		for (int positionGroupIndex = 0; positionGroupIndex < positionGroupCount; ++positionGroupIndex)
		{
			for (int vertexIndex = 0; vertexIndex < vertexCount; ++vertexIndex)
			{
				try
				{
					collateralBalanceArray[positionGroupIndex][vertexIndex] = collateralBalance (
						marketVertexArray,
						positionGroupArray[positionGroupIndex],
						vertexIndex
					);
				}
				catch (java.lang.Exception e)
				{
					e.printStackTrace();

					return null;
				}
			}
		}

		return collateralBalanceArray;
	}

	private org.drip.xva.hypothecation.CollateralGroupVertex collateralGroupVertex (
		final org.drip.analytics.date.JulianDate anchorDate,
		final double positionGroupValue,
		final double realizedCashFlow,
		final double collateralBalance,
		final org.drip.xva.universe.MarketEdge marketEdge)
	{
		int positionReplicationScheme = _simulatorScheme.positionReplicationScheme();

		try
		{
			if (org.drip.xva.dynamics.PositionReplicationScheme.ALBANESE_ANDERSEN_VERTEX ==
				positionReplicationScheme)
			{
				return new org.drip.xva.hypothecation.AlbaneseAndersenVertex (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					collateralBalance
				);
			}

			if (org.drip.xva.dynamics.PositionReplicationScheme.BURGARD_KJAER_HEDGE_ERROR_DUAL_BOND_VERTEX ==
				positionReplicationScheme)
			{
				return org.drip.xva.hypothecation.BurgardKjaerVertexBuilder.HedgeErrorDualBond (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					collateralBalance,
					_simulatorScheme.hedgeError(),
					marketEdge,
					_simulatorScheme.closeOutScheme()
				);
			}

			if (org.drip.xva.dynamics.PositionReplicationScheme.BURGARD_KJAER_SEMI_REPLICATION_DUAL_BOND_VERTEX
				== positionReplicationScheme)
			{
				return org.drip.xva.hypothecation.BurgardKjaerVertexBuilder.SemiReplicationDualBond (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					collateralBalance,
					marketEdge,
					_simulatorScheme.closeOutScheme()
				);
			}

			if (org.drip.xva.dynamics.PositionReplicationScheme.BURGARD_KJAER_GOLD_PLATED_TWO_WAY_CSA_VERTEX
				== positionReplicationScheme)
			{
				return org.drip.xva.hypothecation.BurgardKjaerVertexBuilder.GoldPlatedTwoWayCSA (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					marketEdge,
					_simulatorScheme.closeOutScheme()
				);
			}

			if (org.drip.xva.dynamics.PositionReplicationScheme.BURGARD_KJAER_ONE_WAY_CSA_VERTEX ==
				positionReplicationScheme)
			{
				return org.drip.xva.hypothecation.BurgardKjaerVertexBuilder.OneWayCSA (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					marketEdge,
					_simulatorScheme.closeOutScheme()
				);
			}

			if (org.drip.xva.dynamics.PositionReplicationScheme.BURGARD_KJAER_SET_OFF_VERTEX ==
				positionReplicationScheme)
			{
				return org.drip.xva.hypothecation.BurgardKjaerVertexBuilder.SetOff (
					anchorDate,
					positionGroupValue,
					realizedCashFlow,
					collateralBalance,
					marketEdge
				);
			}
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private org.drip.xva.hypothecation.CollateralGroupVertex[][] collateralGroupVertexArray (
		final org.drip.xva.universe.MarketVertex[] marketVertexArray,
		final double[][] positionGroupValueArray)
	{
		double[][] collateralBalanceArray = collateralBalanceArray (
			marketVertexArray,
			positionGroupValueArray
		);

		if (null == collateralBalanceArray)
		{
			return null;
		}

		int vertexCount = marketVertexArray.length;
		int positionGroupCount = positionGroupValueArray.length;
		org.drip.xva.hypothecation.CollateralGroupVertex[][] collateralGroupVertexArray = new
			org.drip.xva.hypothecation.CollateralGroupVertex[positionGroupCount][vertexCount];

		for (int positionGroupIndex = 0; positionGroupIndex < positionGroupCount; ++positionGroupIndex)
		{
			for (int vertexIndex = 0; vertexIndex < vertexCount; ++vertexIndex)
			{
				try
				{
					collateralGroupVertexArray[positionGroupIndex][vertexIndex] = collateralGroupVertex (
						marketVertexArray[vertexIndex].anchorDate(),
						positionGroupValueArray[positionGroupIndex][vertexIndex],
						0.,
						collateralBalanceArray[positionGroupIndex][vertexIndex],
						0 == vertexIndex ? null : new org.drip.xva.universe.MarketEdge (
							marketVertexArray[vertexIndex - 1],
							marketVertexArray[vertexIndex]
						)
					);
				}
				catch (java.lang.Exception e)
				{
					e.printStackTrace();

					return null;
				}
			}
		}

		return collateralGroupVertexArray;
	}

	private boolean collateralGroupPathArray (
		final org.drip.xva.universe.MarketPath marketPath)
	{
		org.drip.xva.universe.MarketVertex[] marketVertexArray = marketPath.vertexes();

		org.drip.xva.hypothecation.CollateralGroupVertex[][] collateralGroupVertexArray =
			collateralGroupVertexArray (
				marketVertexArray,
				positionGroupValueArray (marketVertexArray)
			);

		if (null == collateralGroupVertexArray)
		{
			return false;
		}

		int positionGroupCount = collateralGroupVertexArray.length;

		try
		{
			for (int positionGroupIndex = 0; positionGroupIndex < positionGroupCount; ++positionGroupIndex)
			{
				if (!_positionGroupContainer.setCollateralGroupPath (
					positionGroupIndex,
					new org.drip.xva.hypothecation.CollateralGroupPath
						(collateralGroupVertexArray[positionGroupIndex])))
					return false;
			}

			return true;
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Generate a PathSimulator Instance with the corresponding Position Group Value
	 * 
	 * @param iPathCount Path Count
	 * @param marketVertexGenerator Market Vertex Generator
	 * @param positionGroupContainer Container of Position Groups
	 * 
	 * @return The PathSimulator Instance
	 */

	public static final PathSimulator UnitPositionGroupValue (
		final int iPathCount,
		final org.drip.xva.universe.MarketVertexGenerator marketVertexGenerator,
		final org.drip.xva.holdings.PositionGroupContainer positionGroupContainer)
	{
		try
		{
			return new PathSimulator (
				iPathCount,
				marketVertexGenerator,
				org.drip.xva.dynamics.PathSimulatorScheme.AlbaneseAndersenVertex(),
				positionGroupContainer
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * PathSimulator Constructor
	 * 
	 * @param iCount Path Count
	 * @param marketVertexGenerator Market Vertex Generator
	 * @param simulatorScheme Path Simulator Scheme
	 * @param positionGroupContainer Container of Position Groups
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public PathSimulator (
		final int iCount,
		final org.drip.xva.universe.MarketVertexGenerator marketVertexGenerator,
		final org.drip.xva.dynamics.PathSimulatorScheme simulatorScheme,
		final org.drip.xva.holdings.PositionGroupContainer positionGroupContainer)
		throws java.lang.Exception
	{
		if (0 >= (_iCount = iCount) ||
			null == (_marketVertexGenerator = marketVertexGenerator) ||
			null == (_simulatorScheme = simulatorScheme) ||
			null == (_positionGroupContainer = positionGroupContainer))
		{
			throw new java.lang.Exception ("PathSimulator Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Path Count
	 * 
	 * @return The Path Count
	 */

	public int count()
	{
		return _iCount;
	}

	/**
	 * Retrieve the Market Vertex Generator
	 * 
	 * @return The Market Vertex Generator
	 */

	public org.drip.xva.universe.MarketVertexGenerator marketVertexGenerator()
	{
		return _marketVertexGenerator;
	}

	/**
	 * Retrieve the Path Simulator Scheme
	 * 
	 * @return The Path Simulator Scheme
	 */

	public org.drip.xva.dynamics.PathSimulatorScheme scheme()
	{
		return _simulatorScheme;
	}

	/**
	 * Retrieve the Position Group Container
	 * 
	 * @return Position Group Container
	 */

	public org.drip.xva.holdings.PositionGroupContainer positionGroupContainer()
	{
		return _positionGroupContainer;
	}

	/**
	 * Generate a Single Trajectory from the Specified Initial Market Vertex and the Evolver Sequence
	 * 
	 * @param initialMarketVertex The Initial Market Vertex
	 * @param unitEvolverSequence The Evolver Sequence
	 * 
	 * @return Single Trajectory Path Exposure Adjustment
	 */

	public org.drip.xva.cpty.PathExposureAdjustment singleTrajectory (
		final org.drip.xva.universe.MarketVertex initialMarketVertex,
		final double[][] unitEvolverSequence)
	{
		try
		{
			org.drip.xva.universe.MarketPath marketPath = new org.drip.xva.universe.MarketPath (
				_marketVertexGenerator.marketVertex (
					initialMarketVertex,
					unitEvolverSequence
				)
			);

			if (!collateralGroupPathArray (marketPath))
			{
				return null;
			}

			org.drip.xva.hypothecation.CollateralGroupPath[][] positionFundingGroupPath =
				_positionGroupContainer.fundingSegmentPaths();

			org.drip.xva.hypothecation.CollateralGroupPath[][] positionCreditDebtGroupPath =
				_positionGroupContainer.nettingSegmentPaths();

			int positionFundingGroupCount = positionFundingGroupPath.length;
			int positionCreditDebtGroupCount = positionCreditDebtGroupPath.length;
			org.drip.xva.netting.FundingGroupPath[] fundingGroupPathArray = new
				org.drip.xva.strategy.AlbaneseAndersenFundingGroupPath[positionFundingGroupCount];
			org.drip.xva.netting.CreditDebtGroupPath[] creditDebtGroupPathArray = new
				org.drip.xva.strategy.AlbaneseAndersenNettingGroupPath[positionCreditDebtGroupCount];

			if (org.drip.xva.dynamics.AdjustmentDigestScheme.ALBANESE_ANDERSEN_METRICS_POINTER ==
				_simulatorScheme.adjustmentDigestScheme())
			{
				for (int positionFundingGroupIndex = 0; positionFundingGroupIndex <
					positionFundingGroupCount; ++positionFundingGroupIndex)
				{
					fundingGroupPathArray[positionFundingGroupIndex] =
						new org.drip.xva.strategy.AlbaneseAndersenFundingGroupPath (
							positionFundingGroupPath[positionFundingGroupIndex],
							marketPath
						);
				}

				for (int positionCreditDebtGroupIndex = 0; positionCreditDebtGroupIndex <
					positionCreditDebtGroupCount; ++positionCreditDebtGroupIndex)
				{
					creditDebtGroupPathArray[positionCreditDebtGroupIndex] =
						new org.drip.xva.strategy.AlbaneseAndersenNettingGroupPath (
							positionCreditDebtGroupPath[positionCreditDebtGroupIndex],
							marketPath
						);
				}
			}

			return new org.drip.xva.cpty.MonoPathExposureAdjustment (
				creditDebtGroupPathArray,
				fundingGroupPathArray
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Simulate the Realized State/Entity Values and their Aggregates over the Paths
	 * 
	 * @param initialMarketVertex The Initial Market Vertex
	 * @param correlatedPathVertexDimension Path Vertex Dimension Generator
	 * 
	 * @return The Exposure Adjustment Aggregator - Simulation Result
	 */

	public org.drip.xva.cpty.ExposureAdjustmentAggregator simulate (
		final org.drip.xva.universe.MarketVertex initialMarketVertex,
		final org.drip.measure.discrete.CorrelatedPathVertexDimension correlatedPathVertexDimension)
	{
		if (null == correlatedPathVertexDimension)
		{
			return null;
		}

		org.drip.xva.cpty.PathExposureAdjustment[] pathExposureAdjustmentArray = new
			org.drip.xva.cpty.PathExposureAdjustment[_iCount];

		for (int pathIndex = 0; pathIndex < _iCount; ++pathIndex)
		{
			if (null == (pathExposureAdjustmentArray[pathIndex] = singleTrajectory (
				initialMarketVertex,
				org.drip.quant.linearalgebra.Matrix.Transpose (org.drip.quant.linearalgebra.Matrix.Transpose
					(correlatedPathVertexDimension.straightPathVertexRd().flatform())))))
			{
				return null;
			}
		}

		try
		{
			return new org.drip.xva.cpty.ExposureAdjustmentAggregator (pathExposureAdjustmentArray);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}