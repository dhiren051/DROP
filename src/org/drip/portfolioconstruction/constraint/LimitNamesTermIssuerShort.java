
package org.drip.portfolioconstruction.constraint;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
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
 * LimitNamesTermIssuerShort holds the Details of Count of the Total Short Active Assets in the Holdings.
 *
 * @author Lakshmi Krishnamurthy
 */

public class LimitNamesTermIssuerShort extends org.drip.portfolioconstruction.constraint.LimitNamesTermIssuer
{

	/**
	 * LimitNamesTermIssuerShort Constructor
	 * 
	 * @param strName Name of the LimitNamesTermShort Constraint
	 * @param scope Scope of the LimitNamesTermShort Constraint
	 * @param unit Unit of the LimitNamesTermShort Constraint
	 * @param dblMinimum Minimum of the Constraint
	 * @param dblMaximum Maximum of the Constraint
	 * @param adblIssuerSelection Array of the Issuer Selections
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public LimitNamesTermIssuerShort (
		final java.lang.String strName,
		final org.drip.portfolioconstruction.optimizer.Scope scope,
		final org.drip.portfolioconstruction.optimizer.Unit unit,
		final double dblMinimum,
		final double dblMaximum,
		final double[] adblIssuerSelection)
		throws java.lang.Exception
	{
		super (
			strName,
			"CT_LIMIT_SHORT_NAMES",
			"Limits the Total Number of Active Short Names in the Holdings",
			scope,
			unit,
			dblMinimum,
			dblMaximum,
			adblIssuerSelection
		);
	}

	@Override public org.drip.function.definition.RdToR1 rdtoR1()
	{
		return new org.drip.function.definition.RdToR1 (null)
		{
			@Override public int dimension()
			{
				return issuerSelection().length;
			}

			@Override public double evaluate (
				final double[] adblVariate)
				throws java.lang.Exception
			{
				double[] adblIssuerSelection = issuerSelection();

				int iNameCount = 0;
				int iNumAsset = adblIssuerSelection.length;

				if (null == adblVariate || !org.drip.quant.common.NumberUtil.IsValid (adblVariate) ||
					adblVariate.length != iNumAsset)
					throw new java.lang.Exception
						("LimitNamesTermIssuerShort::rdToR1::evaluate => Invalid Variate Dimension");

				for (int i = 0; i < iNumAsset; ++i)
				{
					if (0. > adblVariate[i] && 0. != adblIssuerSelection[i]) ++iNameCount;
				}

				return iNameCount;
			}
		};
	}
}