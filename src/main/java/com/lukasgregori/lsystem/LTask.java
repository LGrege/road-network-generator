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

package com.lukasgregori.lsystem;

import com.lukasgregori.lsystem.nonterminals.NTHighwaySegment;
import com.lukasgregori.lsystem.nonterminals.Replaceable;
import com.lukasgregori.util.EntityContainer;

import java.security.InvalidParameterException;

/**
 * @author Lukas Gregori
 */
public class LTask {

    private Replaceable strategy;

    public LTask(Replaceable strategy) {
        this.strategy = strategy;
    }

    void execute() {
        try {
            System.out.println(strategy.getClass().getSimpleName() + ": Replace");
            strategy.replace();
        } catch (InvalidParameterException ex) {
            System.out.println(strategy.getClass().getSimpleName() + ": End of canvas");
            if (strategy instanceof NTHighwaySegment) {
                EntityContainer.getInstance().highwayLatch.countDown();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
