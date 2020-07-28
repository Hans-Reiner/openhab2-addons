/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.ism8.server;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DataPointFactory} creates the data points depending on the types
 *
 * @author Hans-Reiner Hoffmann - Initial contribution
 */
public class DataPointFactory {
    private final Logger logger = LoggerFactory.getLogger(DataPointFactory.class);
    private static ArrayList<IDataPoint> dataPoints = new ArrayList<>();

    /**
     * Creates the concrete data-point based on the type.
     *
     */
    public static IDataPoint createDataPoint(int id, String knxType, String description) {
        IDataPoint dataPoint = null;
        try {
            switch (knxType) {
                case "1.001":
                case "1.002":
                case "1.003":
                case "1.009":
                    dataPoint = new DataPointBool(id, knxType, description);
                    break;
                case "5.001":
                    dataPoint = new DataPointScaling(id, knxType, description);
                    break;
                case "9.001":
                case "9.002":
                case "9.006":
                    dataPoint = new DataPointValue(id, knxType, description);
                    break;
                case "13.002":
                    dataPoint = new DataPointLongValue(id, knxType, description);
                    break;
                case "20.102":
                case "20.103":
                case "20.105":
                    dataPoint = new DataPointByteValue(id, knxType, description);
                    break;
            }
        } catch (Exception e) {
            new DataPointFactory().logger.warn("Error creating data point {}. {}", id, e.getMessage(), e);
        }

        if (dataPoint != null) {
            for (IDataPoint dp : dataPoints) {
                if (dp.getId() == dataPoint.getId()) {
                    new DataPointFactory().logger.info("Data point already exists ({}).", id);
                    return null;
                }
            }
            dataPoints.add(dataPoint);
            return dataPoint;
        }
        return null;
    }
}
