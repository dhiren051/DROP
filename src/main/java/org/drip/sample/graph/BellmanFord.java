
package org.drip.sample.graph;

import java.util.Set;

import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.spaces.graph.BellmanFordScheme;
import org.drip.spaces.graph.Edge;
import org.drip.spaces.graph.Topography;
import org.drip.spaces.graph.ShortestPathVertex;
import org.drip.spaces.graph.ShortestPathTree;

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
 * BellmanFord illustrates the Execution of the Bellman Ford Shortest Path First Algorithm. The References
 *  are:
 *  
 *  1) Wikipedia (2018a): Graph (Abstract Data Type)
 *  	https://en.wikipedia.org/wiki/Graph_(abstract_data_type).
 *  
 *  2) Wikipedia (2018b): Graph Theory https://en.wikipedia.org/wiki/Graph_theory.
 *  
 *  3) Wikipedia (2018c): Graph (Discrete Mathematics)
 *  	https://en.wikipedia.org/wiki/Graph_(discrete_mathematics).
 *  
 *  4) Wikipedia (2018d): Dijkstra's Algorithm https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
 *  
 *  5) Wikipedia (2018e): Bellman-Ford Algorithm
 *  	https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BellmanFord
{

	private static final Topography SetTopography()
		throws Exception
	{
		String[] vertexArray = new String[]
		{
			"Delhi     ",
			"Bombay    ",
			"Madras    ",
			"Calcutta  ",
			"Bangalore ",
			"Hyderabad ",
			"Cochin    ",
			"Pune      ",
			"Ahmedabad ",
			"Jaipur    "
		};

		Topography topography = new Topography();

		for (String vertexName : vertexArray)
		{
			topography.addVertex (vertexName);
		}

		topography.addEdge (
			new Edge (
				vertexArray[0], // Delhi
				vertexArray[1], // Bombay
				1388.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[0], // Delhi
				vertexArray[2], // Madras
				2191.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[1], // Bombay
				vertexArray[2], // Madras
				1279.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[0], // Delhi
				vertexArray[3], // Calcutta
				1341.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[1], // Bombay
				vertexArray[3], // Calcutta
				1968.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[2], // Madras
				vertexArray[3], // Calcutta
				1663.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[2], // Madras
				vertexArray[4], // Bangalore
				361.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[2], // Madras
				vertexArray[5], // Hyderabad
				784.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[2], // Madras
				vertexArray[6], // Cochin
				697.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[1], // Bombay
				vertexArray[7], // Pune
				192.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[1], // Bombay
				vertexArray[8], // Ahmedabad
				492.
			)
		);

		topography.addEdge (
			new Edge (
				vertexArray[0], // Delhi
				vertexArray[9], // Jaipur
				308.
			)
		);

		return topography;
	}

	private static final String PathVertexes (
		final String source,
		final String destination,
		final ShortestPathTree vertexPeripheryMap)
		throws Exception
	{
		String path = "";
		String vertex = destination;

		ShortestPathVertex vertexPeriphery = vertexPeripheryMap.shortestPathVertex (vertex);

		while (!source.equalsIgnoreCase (vertexPeriphery.current()))
		{
			path = path + vertexPeriphery.current() + " <- ";

			vertexPeriphery = vertexPeripheryMap.shortestPathVertex (vertexPeriphery.preceeding());
		}

		path = path + source;
		return path;
	}

	public static void main (
		final String[] inputArray)
		throws Exception
	{
		EnvManager.InitEnv ("");

		Topography topography = SetTopography();

		BellmanFordScheme bellmanFordScheme = new BellmanFordScheme (topography);

		Set<String> vertexNameSet = topography.vertexNameSet();

		for (String source : vertexNameSet)
		{
			ShortestPathTree vertexPeripheryMap = bellmanFordScheme.spf (source).vertexPeripheryMap();

			System.out.println ("\t||------------------------------------------------------------||");

			for (String vertex : vertexNameSet)
			{
				if (!vertex.equalsIgnoreCase (source))
				{
					ShortestPathVertex vertexPeriphery = vertexPeripheryMap.shortestPathVertex (vertex);

					System.out.println (
						"\t|| " + source + " to " + vertex + " is " +
						FormatUtil.FormatDouble (vertexPeriphery.weightFromSource(), 4, 0, 1.) +
						" | Previous is " + vertexPeriphery.preceeding() + " || " + PathVertexes (
							source,
							vertex,
							vertexPeripheryMap
						)
					);
				}
			}

			System.out.println ("\t||------------------------------------------------------------||\n");
		}

		EnvManager.TerminateEnv();
	}
}