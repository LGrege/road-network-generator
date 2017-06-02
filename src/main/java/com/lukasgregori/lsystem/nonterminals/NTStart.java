/*********************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Lukas Gregori
 * lukas.gregori@student.tugraz.at
 * www.lukasgregori.com
 *
 * (c) 2017 by Lukas Gregori
 *********************************************************************/

package com.lukasgregori.lsystem.nonterminals;

import com.lukasgregori.input.RoadNetworkConfiguration;
import com.lukasgregori.lsystem.LTask;
import com.lukasgregori.lsystem.LTaskScheduler;
import com.lukasgregori.terrain.TerrainParser;
import com.lukasgregori.util.ContextProvider;
import org.apache.commons.lang3.Validate;

import java.security.InvalidParameterException;

/**
 * @author Lukas Gregori
 */
public class NTStart implements Replaceable {

    @Override
    public void replace() {
        checkDependencies();
        RoadNetworkConfiguration config = ContextProvider.getNetworkConfig();
        config.startPoints.forEach(startPoint -> {
            NTHighwayStart newHighwayStart = new NTHighwayStart(startPoint);
            LTaskScheduler.getInstance().addTask(new LTask(newHighwayStart));
        });
    }

    private void checkDependencies() {
        try {
            Validate.notNull(TerrainParser.getHeightMap());
        } catch (NullPointerException ex) {
            System.err.println("Invalid configuration, shutting down");
            throw new InvalidParameterException();
        }
    }
}
