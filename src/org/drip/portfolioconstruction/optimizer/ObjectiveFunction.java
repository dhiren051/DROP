
package org.drip.portfolioconstruction.optimizer;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * ObjectiveFunction holds the Terms composing the Objective Function and their Weights.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ObjectiveFunction {
	private java.util.List<org.drip.portfolioconstruction.optimizer.ObjectiveTermUnit> _lsOTU = new
		java.util.ArrayList<org.drip.portfolioconstruction.optimizer.ObjectiveTermUnit>();

	/**
	 * Retrieve the List of Objective Terms
	 * 
	 * @return The List of Objective Terms
	 */

	public java.util.List<org.drip.portfolioconstruction.optimizer.ObjectiveTermUnit> list()
	{
		return _lsOTU;
	}

	/**
	 * Evaluate the Objective Function over the List of Objective Terms
	 * 
	 * @param lsadblParameter List of the Objective Term Parameters
	 * 
	 * @return The Result of the Evaluation
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double evaluate (
		final java.util.List<double[]> lsadblParameter)
		throws java.lang.Exception
	{
		if (null == lsadblParameter || lsadblParameter.size() != _lsOTU.size())
			throw new java.lang.Exception ("ObjectiveFunction::evaluate => Invalid Inputs");

		int i = 0;
		double dblValue = 0.;

		for (org.drip.portfolioconstruction.optimizer.ObjectiveTermUnit otu : _lsOTU)
			dblValue += otu.term().rdtoR1().evaluate (lsadblParameter.get (i++));

		return dblValue;
	}
}
