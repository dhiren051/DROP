
package org.drip.simm20.product;

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
 * IRNetSensitivity holds the Weighted Net Sensitivities for each of the IR Risk Factors - OIS, LIBOR 1M,
 *  LIBOR 3M, LIBOR 6M LIBOR 12M, PRIME, and MUNICIPAL. The References are:
 *  
 *  - Andersen, L. B. G., M. Pykhtin, and A. Sokol (2017): Credit Exposure in the Presence of Initial Margin,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2806156, eSSRN.
 *  
 *  - Albanese, C., S. Caenazzo, and O. Frankel (2017): Regression Sensitivities for Initial Margin
 *  	Calculations, https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2763488, eSSRN.
 *  
 *  - Anfuso, F., D. Aziz, P. Giltinan, and K. Loukopoulus (2017): A Sound Modeling and Back-testing
 *  	Framework for Forecasting Initial Margin Requirements,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2716279, eSSRN.
 *  
 *  - Caspers, P., P. Giltinan, R. Lichters, and N. Nowaczyk (2017): Forecasting Initial Margin Requirements
 *  	- A Model Evaluation https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2911167, eSSRN.
 *  
 *  - International Swaps and Derivatives Association (2017): SIMM v2.0 Methodology,
 *  	https://www.isda.org/a/oFiDE/isda-simm-v2.pdf.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IRNetSensitivity
{
	private java.util.Map<java.lang.String, java.lang.Double> _ois = null;
	private java.util.Map<java.lang.String, java.lang.Double> _prime = null;
	private java.util.Map<java.lang.String, java.lang.Double> _libor1M = null;
	private java.util.Map<java.lang.String, java.lang.Double> _libor3M = null;
	private java.util.Map<java.lang.String, java.lang.Double> _libor6M = null;
	private java.util.Map<java.lang.String, java.lang.Double> _libor12M = null;
	private java.util.Map<java.lang.String, java.lang.Double> _municipal = null;

	/**
	 * IRNetSensitivity Constructor
	 * 
	 * @param ois OIS Weighted/Unweighted Sensitivity
	 * @param libor1M LIBOR 1M Weighted/Unweighted Sensitivity
	 * @param libor3M LIBOR 3M Weighted/Unweighted Sensitivity
	 * @param libor6M LIBOR 6M Weighted/Unweighted Sensitivity
	 * @param libor12M LIBOR 12M Weighted/Unweighted Sensitivity
	 * @param prime Prime Weighted/Unweighted Sensitivity
	 * @param municipal Municipal Weighted/Unweighted Sensitivity
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public IRNetSensitivity (
		final java.util.Map<java.lang.String, java.lang.Double> ois,
		final java.util.Map<java.lang.String, java.lang.Double> libor1M,
		final java.util.Map<java.lang.String, java.lang.Double> libor3M,
		final java.util.Map<java.lang.String, java.lang.Double> libor6M,
		final java.util.Map<java.lang.String, java.lang.Double> libor12M,
		final java.util.Map<java.lang.String, java.lang.Double> prime,
		final java.util.Map<java.lang.String, java.lang.Double> municipal)
		throws java.lang.Exception
	{
		if (null == (_ois = ois) ||
			null == (_libor1M = libor1M) ||
			null == (_libor3M = libor3M) ||
			null == (_libor6M = libor6M) ||
			null == (_libor12M = libor12M) ||
			null == (_prime = prime) ||
			null == (_municipal = municipal))
		{
			throw new java.lang.Exception ("IRNetSensitivity Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the OIS Net Tenor Sensitivity Map
	 * 
	 * @return The OIS Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> ois()
	{
		return _ois;
	}

	/**
	 * Retrieve the LIBOR 1M Net Tenor Sensitivity Map
	 * 
	 * @return The LIBOR 1M Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> libor1M()
	{
		return _libor1M;
	}

	/**
	 * Retrieve the LIBOR 3M Net Tenor Sensitivity Map
	 * 
	 * @return The LIBOR 3M Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> libor3M()
	{
		return _libor3M;
	}

	/**
	 * Retrieve the LIBOR 6M Net Tenor Sensitivity Map
	 * 
	 * @return The LIBOR 6M Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> libor6M()
	{
		return _libor6M;
	}

	/**
	 * Retrieve the LIBOR 12M Net Tenor Sensitivity Map
	 * 
	 * @return The LIBOR 12M Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> libor12M()
	{
		return _libor12M;
	}

	/**
	 * Retrieve the PRIME Net Tenor Sensitivity Map
	 * 
	 * @return The PRIME Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> prime()
	{
		return _prime;
	}

	/**
	 * Retrieve the MUNICIPAL Net Tenor Sensitivity Map
	 * 
	 * @return The MUNICIPAL Net Tenor Sensitivity Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> municipal()
	{
		return _municipal;
	}

	/**
	 * Compute the OIS-OIS Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-OIS Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_OIS (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_OIS => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_OIS = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisOuterEntry : _ois.entrySet())
		{
			double outerSensitivity = oisOuterEntry.getValue();

			java.lang.String outerTenor = oisOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisInnerEntry : _ois.entrySet())
			{
				java.lang.String innerTenor = oisInnerEntry.getKey();

				covariance_OIS_OIS = covariance_OIS_OIS + outerSensitivity * oisInnerEntry.getValue() * (
					outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
						tenorCorrelation.entry (
							outerTenor,
							innerTenor
						)
					);
			}
		}

		return covariance_OIS_OIS;
	}

	/**
	 * Compute the LIBOR1M-LIBOR1M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR1M-LIBOR1M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR1M_LIBOR1M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR1M_LIBOR1M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR1M_LIBOR1M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MOuterEntry : _libor1M.entrySet())
		{
			double outerSensitivity = libor1MOuterEntry.getValue();

			java.lang.String outerTenor = libor1MOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MInnerEntry :
				_libor1M.entrySet())
			{
				java.lang.String innerTenor = libor1MInnerEntry.getKey();

				covariance_LIBOR1M_LIBOR1M = covariance_LIBOR1M_LIBOR1M + outerSensitivity *
					libor1MInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_LIBOR1M_LIBOR1M;
	}

	/**
	 * Compute the LIBOR3M-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR3M-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR3M_LIBOR3M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR3M_LIBOR3M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR3M_LIBOR3M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MOuterEntry : _libor3M.entrySet())
		{
			double outerSensitivity = libor3MOuterEntry.getValue();

			java.lang.String outerTenor = libor3MOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MInnerEntry :
				_libor3M.entrySet())
			{
				java.lang.String innerTenor = libor3MInnerEntry.getKey();

				covariance_LIBOR3M_LIBOR3M = covariance_LIBOR3M_LIBOR3M + outerSensitivity *
					libor3MInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_LIBOR3M_LIBOR3M;
	}

	/**
	 * Compute the LIBOR6M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR6M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR6M_LIBOR6M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR6M_LIBOR6M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR6M_LIBOR6M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor6MOuterEntry : _libor6M.entrySet())
		{
			double outerSensitivity = libor6MOuterEntry.getValue();

			java.lang.String outerTenor = libor6MOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor6MInnerEntry :
				_libor3M.entrySet())
			{
				java.lang.String innerTenor = libor6MInnerEntry.getKey();

				covariance_LIBOR6M_LIBOR6M = covariance_LIBOR6M_LIBOR6M + outerSensitivity *
					libor6MInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_LIBOR6M_LIBOR6M;
	}

	/**
	 * Compute the LIBOR12M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR12M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR12M_LIBOR12M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception
				("IRNetSensitivity::covariance_LIBOR12M_LIBOR12M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR12M_LIBOR12M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor12MOuterEntry : _libor12M.entrySet())
		{
			double outerSensitivity = libor12MOuterEntry.getValue();

			java.lang.String outerTenor = libor12MOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor12MInnerEntry :
				_libor12M.entrySet())
			{
				java.lang.String innerTenor = libor12MInnerEntry.getKey();

				covariance_LIBOR12M_LIBOR12M = covariance_LIBOR12M_LIBOR12M + outerSensitivity *
					libor12MInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_LIBOR12M_LIBOR12M;
	}

	/**
	 * Compute the PRIME-PRIME Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The PRIME-PRIME Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_PRIME_PRIME (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_PRIME_PRIME => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_PRIME_PRIME = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> primeOuterEntry : _prime.entrySet())
		{
			double outerSensitivity = primeOuterEntry.getValue();

			java.lang.String outerTenor = primeOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> primeInnerEntry : _prime.entrySet())
			{
				java.lang.String innerTenor = primeInnerEntry.getKey();

				covariance_PRIME_PRIME = covariance_PRIME_PRIME + outerSensitivity *
					primeInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_PRIME_PRIME;
	}

	/**
	 * Compute the MUNICIPAL-MUNICIPAL Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The MUNICIPAL-MUNICIPAL Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_MUNICIPAL_MUNICIPAL (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception
				("IRNetSensitivity::covariance_MUNICIPAL_MUNICIPAL => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_MUNICIPAL_MUNICIPAL = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> municipalOuterEntry :
			_municipal.entrySet())
		{
			double outerSensitivity = municipalOuterEntry.getValue();

			java.lang.String outerTenor = municipalOuterEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> municipalInnerEntry :
				_municipal.entrySet())
			{
				java.lang.String innerTenor = municipalInnerEntry.getKey();

				covariance_MUNICIPAL_MUNICIPAL = covariance_MUNICIPAL_MUNICIPAL + outerSensitivity *
					municipalInnerEntry.getValue() * (
						outerTenor.equalsIgnoreCase (innerTenor) ? 1. :
							tenorCorrelation.entry (
								outerTenor,
								innerTenor
							)
						);
			}
		}

		return covariance_MUNICIPAL_MUNICIPAL;
	}

	/**
	 * Compute the OIS-LIBOR1M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-LIBOR1M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_LIBOR1M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_LIBOR1M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_LIBOR1M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MEntry : _libor1M.entrySet())
			{
				java.lang.String libor1MTenor = libor1MEntry.getKey();

				covariance_OIS_LIBOR1M = covariance_OIS_LIBOR1M + oisSensitivity * libor1MEntry.getValue() * (
					oisTenor.equalsIgnoreCase (libor1MTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							libor1MTenor
						)
					);
			}
		}

		return covariance_OIS_LIBOR1M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the OIS-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_LIBOR3M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_LIBOR3M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_LIBOR3M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MEntry : _libor3M.entrySet())
			{
				java.lang.String libor3MTenor = libor3MEntry.getKey();

				covariance_OIS_LIBOR3M = covariance_OIS_LIBOR3M + oisSensitivity * libor3MEntry.getValue() * (
					oisTenor.equalsIgnoreCase (libor3MTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							libor3MTenor
						)
					);
			}
		}

		return covariance_OIS_LIBOR3M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the OIS-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_LIBOR6M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_LIBOR6M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_LIBOR6M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor6MEntry : _libor6M.entrySet())
			{
				java.lang.String libor6MTenor = libor6MEntry.getKey();

				covariance_OIS_LIBOR6M = covariance_OIS_LIBOR6M + oisSensitivity * libor6MEntry.getValue() * (
					oisTenor.equalsIgnoreCase (libor6MTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							libor6MTenor
						)
					);
			}
		}

		return covariance_OIS_LIBOR6M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the OIS-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_LIBOR12M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_LIBOR12M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_LIBOR12M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor12MEntry : _libor12M.entrySet())
			{
				java.lang.String libor12MTenor = libor12MEntry.getKey();

				covariance_OIS_LIBOR12M = covariance_OIS_LIBOR12M + oisSensitivity * libor12MEntry.getValue() * (
					oisTenor.equalsIgnoreCase (libor12MTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							libor12MTenor
						)
					);
			}
		}

		return covariance_OIS_LIBOR12M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the OIS-PRIME Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-PRIME Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_PRIME (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_PRIME => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_PRIME = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> primeEntry : _prime.entrySet())
			{
				java.lang.String primeTenor = primeEntry.getKey();

				covariance_OIS_PRIME = covariance_OIS_PRIME + oisSensitivity * primeEntry.getValue() * (
					oisTenor.equalsIgnoreCase (primeTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							primeTenor
						)
					);
			}
		}

		return covariance_OIS_PRIME * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the OIS-MUNICIPAL Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The OIS-MUNICIPAL Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_OIS_MUNICIPAL (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_OIS_MUNICIPAL => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_OIS_MUNICIPAL = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> oisEntry : _ois.entrySet())
		{
			double oisSensitivity = oisEntry.getValue();

			java.lang.String oisTenor = oisEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> municipalEntry : _municipal.entrySet())
			{
				java.lang.String municipalTenor = municipalEntry.getKey();

				covariance_OIS_MUNICIPAL = covariance_OIS_MUNICIPAL + oisSensitivity * municipalEntry.getValue() * (
					oisTenor.equalsIgnoreCase (municipalTenor) ? 1. :
						tenorCorrelation.entry (
							oisTenor,
							municipalTenor
						)
					);
			}
		}

		return covariance_OIS_MUNICIPAL * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the LIBOR1M-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR1M-LIBOR3M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR1M_LIBOR3M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR1M_LIBOR3M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR1M_LIBOR3M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MEntry : _libor1M.entrySet())
		{
			double libor1MSensitivity = libor1MEntry.getValue();

			java.lang.String libor1MTenor = libor1MEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MEntry : _libor3M.entrySet())
			{
				java.lang.String libor3MTenor = libor3MEntry.getKey();

				covariance_LIBOR1M_LIBOR3M = covariance_LIBOR1M_LIBOR3M + libor1MSensitivity *
					libor3MEntry.getValue() * (
						libor1MTenor.equalsIgnoreCase (libor3MTenor) ? 1. :
							tenorCorrelation.entry (
								libor1MTenor,
								libor3MTenor
							)
						);
			}
		}

		return covariance_LIBOR1M_LIBOR3M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the LIBOR1M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR1M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR1M_LIBOR6M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR1M_LIBOR6M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR1M_LIBOR6M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MEntry : _libor1M.entrySet())
		{
			double libor1MSensitivity = libor1MEntry.getValue();

			java.lang.String libor1MTenor = libor1MEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor6MEntry : _libor6M.entrySet())
			{
				java.lang.String libor6MTenor = libor6MEntry.getKey();

				covariance_LIBOR1M_LIBOR6M = covariance_LIBOR1M_LIBOR6M + libor1MSensitivity *
					libor6MEntry.getValue() * (
						libor1MTenor.equalsIgnoreCase (libor6MTenor) ? 1. :
							tenorCorrelation.entry (
								libor1MTenor,
								libor6MTenor
							)
						);
			}
		}

		return covariance_LIBOR1M_LIBOR6M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the LIBOR1M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR1M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR1M_LIBOR12M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR1M_LIBOR12M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR1M_LIBOR12M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor1MEntry : _libor1M.entrySet())
		{
			double libor1MSensitivity = libor1MEntry.getValue();

			java.lang.String libor1MTenor = libor1MEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor12MEntry : _libor12M.entrySet())
			{
				java.lang.String libor12MTenor = libor12MEntry.getKey();

				covariance_LIBOR1M_LIBOR12M = covariance_LIBOR1M_LIBOR12M + libor1MSensitivity *
					libor12MEntry.getValue() * (
						libor1MTenor.equalsIgnoreCase (libor12MTenor) ? 1. :
							tenorCorrelation.entry (
								libor1MTenor,
								libor12MTenor
							)
						);
			}
		}

		return covariance_LIBOR1M_LIBOR12M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the LIBOR3M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR3M-LIBOR6M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR3M_LIBOR6M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR3M_LIBOR6M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR3M_LIBOR6M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MEntry : _libor3M.entrySet())
		{
			double libor3MSensitivity = libor3MEntry.getValue();

			java.lang.String libor3MTenor = libor3MEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor6MEntry : _libor6M.entrySet())
			{
				java.lang.String libor6MTenor = libor6MEntry.getKey();

				covariance_LIBOR3M_LIBOR6M = covariance_LIBOR3M_LIBOR6M + libor3MSensitivity *
					libor6MEntry.getValue() * (
						libor3MTenor.equalsIgnoreCase (libor6MTenor) ? 1. :
							tenorCorrelation.entry (
								libor3MTenor,
								libor6MTenor
							)
						);
			}
		}

		return covariance_LIBOR3M_LIBOR6M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}

	/**
	 * Compute the LIBOR3M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @param curveTenorSensitivitySettings The Curve Tenor Sensitivity Settings
	 * 
	 * @return The LIBOR3M-LIBOR12M Net Sensitivity Co-variance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double covariance_LIBOR3M_LIBOR12M (
		final org.drip.simm20.product.CurveTenorSensitivitySettings curveTenorSensitivitySettings)
		throws java.lang.Exception
	{
		if (null == curveTenorSensitivitySettings)
		{
			throw new java.lang.Exception ("IRNetSensitivity::covariance_LIBOR3M_LIBOR12M => Invalid Inputs");
		}

		org.drip.measure.stochastic.LabelCorrelation tenorCorrelation =
			curveTenorSensitivitySettings.singleCurveTenorCorrelation();

		double covariance_LIBOR3M_LIBOR12M = 0.;

		for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor3MEntry : _libor3M.entrySet())
		{
			double libor3MSensitivity = libor3MEntry.getValue();

			java.lang.String libor3MTenor = libor3MEntry.getKey();

			for (java.util.Map.Entry<java.lang.String, java.lang.Double> libor12MEntry : _libor12M.entrySet())
			{
				java.lang.String libor12MTenor = libor12MEntry.getKey();

				covariance_LIBOR3M_LIBOR12M = covariance_LIBOR3M_LIBOR12M + libor3MSensitivity *
					libor12MEntry.getValue() * (
						libor3MTenor.equalsIgnoreCase (libor12MTenor) ? 1. :
							tenorCorrelation.entry (
								libor3MTenor,
								libor12MTenor
							)
						);
			}
		}

		return covariance_LIBOR3M_LIBOR12M * curveTenorSensitivitySettings.crossSubCurveCorrelation();
	}
}