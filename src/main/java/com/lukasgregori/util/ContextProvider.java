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

package com.lukasgregori.util;

import com.lukasgregori.input.RoadNetworkConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Lukas Gregori
 */
public class ContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        ContextProvider.context = context;
    }

    public static RoadNetworkConfiguration getNetworkConfig() {
        return context.getBean(RoadNetworkConfiguration.class);
    }

    static String getString(String name) throws MissingResourceException {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        return bundle.getString(name);
    }

    public static int getInt(String name) {
        Optional<String> property = Optional.of(getString(name));
        return Integer.parseInt(property.orElse("-1"));
    }

    public static double getDouble(String name) {
        Optional<String> property = Optional.of(getString(name));
        return Double.parseDouble(property.orElse("-1"));
    }

    public static boolean getBoolean(String name) {
        Optional<String> property = Optional.of(getString(name));
        return Boolean.valueOf(property.orElse("false"));
    }
}